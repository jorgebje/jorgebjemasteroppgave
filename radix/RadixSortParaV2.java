import java.util.concurrent.CyclicBarrier;

/* Improved version of RadixSortPara, minimizing the number of synchronizations */
/* At least two syncs have been removed */
class RadixSortParaV2{
	int[] array_source;
	int[] array_dest;

	int rightshift, mask;

	int[] global_bucket;
	int[][] local_bucket;

	boolean open = false;
	int task = 0;

	int thread_count;
	Worker[] workers;
	Thread[] threads;
	CyclicBarrier CB;
	CyclicBarrier CBWO;

	public void open(int thread_count){
		this.thread_count = thread_count;
		open = true;
		CB = new CyclicBarrier(thread_count+1);
		CBWO = new CyclicBarrier(thread_count);
		workers = new Worker[thread_count];
		threads = new Thread[thread_count];
		for(int i = 0; i < thread_count; i++){
			workers[i] = new Worker(i);
			(threads[i] = new Thread(workers[i])).start();
		}
	}

	public void close(){
		open = false;
		task = 0;
		waitTaskComp();
		for(int i = 0; i < thread_count; i++){
			try{
				threads[i].join();
			}catch(Exception e){}
		}
	}


	public void sort(int[] array1, int[] array2, int bits){
		array_source = array1;
		array_dest = array2;

		int n = array_source.length;

		//divide the array between the threads
		for(int i = 0; i < thread_count; i++){
			int from = (int)(i*(n/(double)thread_count));
			int to = (int)((i+1)*(n/(double)thread_count));
			workers[i].setBounds(from,to);
		}

		//setup buckets
		int size = (int)Math.pow(2,bits);
		global_bucket = new int[size];
		local_bucket = new int[thread_count][size];

		//give each thread a master bucket slot
		for(int i = 0; i < thread_count; i++){
			workers[i].mbucket_from = i*global_bucket.length/thread_count;
			workers[i].mbucket_to = (i+1)*global_bucket.length/thread_count;
		}


		//find max bit in parallel
		task = 1;
		waitTaskComp();

		rightshift = 0;
		mask = (1<<bits)-1;

		int steps = 0;
		int bits_left = workers[0].global_max_bit;
		while(workers[0].global_max_bit >= rightshift){

			//each one fill out its own bucket, parallel
			task = 2;
			waitTaskComp();

			//sum each of the buckets into a master bucket, parallel
			// task = 3;
			// waitTaskComp();
			
			//finalize the master bucket, sequentially
			int sum = 0;
			for(int i = 0; i < global_bucket.length; i++){
				int tmp = global_bucket[i];
				global_bucket[i] = sum;
				sum += tmp;
			}

			//finalize the threads position buckets, parallel
			task = 4;
			waitTaskComp();
			
			//each thread has its own bucket, containing the start pos for each number, for this thread only.
			//go through this threads part of the list and move its values to the correct position in the destination array
			// task = 5;
			// waitTaskComp();

			//swap arrays
			int[] tmp = array_source;
			array_source = array_dest;
			array_dest = tmp;

			//empty global bucket
			setZero(global_bucket);

			//update steps and bits_left counters
			steps++;
			rightshift += bits;
		}

		//check for uneven array swaps, array_dub is the actual we want the results in array
		if(steps % 2 == 1){
			System.arraycopy(array_source,0,array_dest,0,array_source.length);
		}

	}

	private void waitTaskComp(){

		try{
			CB.await(); //resume threads
			CB.await(); //wait for threads to complete
		}catch(Exception e){}
	}

	class Worker implements Runnable{
		int id;
		int from, to;
		int ored_num;
		int global_max_bit;

		int mbucket_from, mbucket_to;


		Worker(int id){
			this.id = id;
		}

		private void setBounds(int from, int to){
			this.from = from;
			this.to = to;
		}

		public void run(){
			while(open){
				//wait for main to give task
				try{CB.await();}
				catch(Exception e){}

				switch(task){
					case 1: getMSBPos(); break;
					case 2: countDigits(); try{CBWO.await();}catch(Exception e){}
					case 3: sumDigitPart(); break;
					case 4: createFinalBuckets(); try{CBWO.await();}catch(Exception e){}
					case 5: sortPart(); break;
				}

				//tell main, thread is done with task
				try{CB.await();}
				catch(Exception e){}
			}
		}

		private void getMSBPos(){
			ored_num = array_source[from];
			for(int i = from+1; i < to; i++){
				ored_num |= array_source[i];
			}
			//await all threads to be done ORing
			try{
				CBWO.await();
			}catch(Exception e){}

			int ored_num_g = workers[0].ored_num;
			for(int i = 1; i < workers.length; i++){
				ored_num_g |= workers[i].ored_num;
			}

			global_max_bit = 0;
			while(ored_num_g > 0){
				ored_num_g = ored_num_g >>> 1;
				global_max_bit++;
			}
		}

		private void countDigits(){
			setZero(local_bucket[id]);
			for(int i = from; i < to; i++){
				int pos = (array_source[i]>>>rightshift)&mask;
				local_bucket[id][pos]++;
			}
		}

		private void sumDigitPart(){
			for(int i = 0; i < thread_count; i++){
				for(int j = mbucket_from; j < mbucket_to; j++){
					global_bucket[j] += local_bucket[i][j];
				}
			}
		}

		private void createFinalBuckets(){
			for(int i = mbucket_from; i < mbucket_to; i++){
				int sum = global_bucket[i];
				for(int j = 0; j < thread_count; j++){
					int tmp = local_bucket[j][i];
					local_bucket[j][i] = sum;
					sum += tmp;
				}
			}
		}

		private void sortPart(){
			for(int i = from; i < to; i++){
				int pos = (array_source[i]>>>rightshift)&mask;
				array_dest[local_bucket[id][pos]++] = array_source[i];
			}
		}


	}

	private static void setZero(int[] array){
		for(int i = 0; i < array.length; i++) array[i] = 0;
	}
}