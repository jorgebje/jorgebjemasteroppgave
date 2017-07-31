import java.util.concurrent.CyclicBarrier;

/* Going further in removing syncronizations by not creating a global_bucket, instead creating two local_buckets */
/* At least two syncs have been removed */
class RadixSortParaV3{
	int bits;
	int[] array_source;
	int[] array_dest;

	int rightshift, mask;

	int[][] local_count;
	int[][] local_bucket;

	boolean open = false;

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
		try{
			CB.await();
		}catch(Exception e){}
		for(int i = 0; i < thread_count; i++){
			try{
				threads[i].join();
			}catch(Exception e){}
		}
	}


	public void sort(int[] array1, int[] array2, int bits){
		this.bits = bits;
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
		local_count = new int[thread_count][size];
		local_bucket = new int[thread_count][size];

		rightshift = 0;
		mask = (1<<bits)-1;

		/* let the threads do their thing */
		waitTaskComp();
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

		int rightshift, mask;

		int[] array_source;
		int[] array_dest;


		Worker(int id){
			this.id = id;
		}

		private void setBounds(int from, int to){
			this.from = from;
			this.to = to;
		}

		public void run(){
			while(true){
				//wait for main to give task
				try{CB.await();}
				catch(Exception e){}
				

				if(!open) break;

				runSetup();

				int steps = 0;

				while(global_max_bit >= rightshift){
					fillCountArray();

					try{
						CBWO.await();
					}catch(Exception e){}

					fillBucketArray();
					sortPart();


					int[] tmp = array_source;
					array_source = array_dest;
					array_dest = tmp;

					steps++;
					rightshift += bits;

					try{
						CBWO.await();
					}catch(Exception e){}
				}

				if(steps % 2 == 1){
					System.arraycopy(array_source,from,array_dest,from,to-from);
				}

				//tell main, thread is done with task
				try{CB.await();}
				catch(Exception e){}
			}
		}

		private void runSetup(){
			rightshift = RadixSortParaV3.this.rightshift;
			mask = RadixSortParaV3.this.mask;

			array_source = RadixSortParaV3.this.array_source;
			array_dest = RadixSortParaV3.this.array_dest;

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

		private void fillCountArray(){
			setZero(local_count[id]);
			for(int i = from; i < to; i++){
				int pos = (array_source[i]>>>rightshift)&mask;
				local_count[id][pos]++;
			}
		}

		private void fillBucketArray(){
			int count = 0;
			for(int i = 0; i < local_count[0].length; i++){
				for(int j = 0; j < local_count.length; j++){
					if(j == id){
						local_bucket[j][i] = count;
					}
					count += local_count[j][i];
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