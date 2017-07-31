import java.util.concurrent.CyclicBarrier;

class Experimental{
	int[] array;
	int[] array_holder;
	int bits;

	int[] startstop;

	boolean open = false;
	int thread_count;
	Worker[] workers;
	Thread[] threads;
	CyclicBarrier CB;

	public void open(int thread_count){
		this.thread_count = thread_count;
		open = true;
		CB = new CyclicBarrier(thread_count+1);
		workers = new Worker[thread_count];
		threads = new Thread[thread_count];
		for(int i = 0; i < thread_count; i++){
			workers[i] = new Worker(i);
			(threads[i] = new Thread(workers[i])).start();
		}
	}

	public void close(){
		open = false;
		try{CB.await();}catch(Exception e){}
		for(int i = 0; i < thread_count; i++){
			try{
				threads[i].join();
			}catch(Exception e){}
		}
	}

	public void sort(int[] array, int bits){
		this.array = array;
		array_holder = new int[array.length];
		this.bits = bits;

		divideTask(array.length);
		runWorkers();
		mergeResults();
	}

	private void divideTask(long n){
		startstop = new int[thread_count+1];
		for(int i = 0; i <= thread_count; i++){
			int from = (int)(i*n/thread_count);
			startstop[i] = from;
		}
		for(int i = 0; i < thread_count; i++){
			workers[i].setBounds(startstop[i],startstop[i+1]);
		}
	}

	private void runWorkers(){
		try{
			CB.await();
			CB.await();
		}catch(Exception e){}
	}

	private void mergeResults(){
		// System.out.println("Before Merge");
		// HelperClass.printArray(array);
		int swaps = 0;
		for(int parts = thread_count; parts > 1; parts /=2){
			for(int i = 0; i < parts; i += 2){
				// System.out.println("Thing1");
				mergeResultsPart(startstop[i],startstop[i+1],startstop[i+2]);
			}
			int j = 0;
			for(int i = 0; i <= parts/2; i++){
				// System.out.println("Thing2");
				startstop[i] = startstop[j];
				j += 2;
			}
			if(parts % 2 == 1){
				// System.out.println("Thing3");
				startstop[parts/2+1] = startstop[parts];
			}
			int[] tmp = array;
			array = array_holder;
			array_holder = tmp;
			swaps++;
		}
		if(swaps % 2 == 1){
			// System.out.println("Thing4");
			System.arraycopy(array,0,array_holder,0,array.length);
		}
	}

	private void mergeResultsPart(int start, int mid, int stop){
		int i;
		int a = start;
		int b = stop;
		int c = start;
		int ec = mid;
		int d = mid;
		int ed = stop;
		for(i = a; i < b; i++){
			// System.out.println("Thing0");
			if(array[c] < array[d]){
				array_holder[i] = array[c++];
				if(c == ec){
					// System.out.println("Break1");
					i++;
					break;	
				} 
			}
			else{
				array_holder[i] = array[d++];
				if(d == ed){
					// System.out.println("Break2");
					i++;
					break;
				}
			}
		}
		while(c < ec){
			// System.out.println("Thing1");
			array_holder[i++] = array[c++];
		}
		while(d < ed){
			// System.out.println("Thing2");
			array_holder[i++] = array[d++];
		}
	}

	class Worker implements Runnable{
		int id;
		int from, to;

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
			while(open){
				try{CB.await();}
				catch(Exception e){}
				if(!open) break;

				sortPart();

				try{CB.await();}
				catch(Exception e){}
			}
		}

		private void sortPart(){
			array_source = array;
			array_dest = array_holder;

			int bits_to_sort = getMSBPos();
			// int bits_to_sort = 32;
			int bits_left = bits_to_sort;

			int bucket[] = new int[(int)Math.pow(2,bits)];

			int leftshift = 32;
			int rightshift = 32-bits;

			int swaps = 0;
			while(bits_left >= bits){
				leftshift -= bits;

				sortPart(bucket,leftshift,rightshift);
				int[] tmp = array_source;
				array_source = array_dest;
				array_dest = tmp;

				bits_left -= bits;
				swaps++;
				setZero(bucket);
			}

			if(bits_left != 0){
				// System.out.println("runs");
				sortPart(bucket,32-bits_to_sort,32-bits_left);
				int[] tmp = array_source;
				array_source = array_dest;
				array_dest = tmp;
				swaps++;
			}

			if(swaps%2 == 1){
				// System.out.println("swapped");
				System.arraycopy(array_source,from,array_dest,from,to-from);
			}
		}

		private void sortPart(int[] bucket, int leftshift, int rightshift){
			//find size of each bucket
			int size = bucket.length;
			for(int i = from; i < to; i++){
				int pos = (array_source[i]<<leftshift)>>>rightshift;
				bucket[pos]++;
			}

			//calc start pos of each bucket
			int sum = from;
			int tmp = 0;
			for(int i = 0; i < size; i++){
				tmp = bucket[i];
				bucket[i] = sum;
				sum += tmp;
			}

			//move numbers to correct part after consulting with bucket
			for(int i = from; i < to; i++){
				int pos = (array_source[i]<<leftshift)>>>rightshift;
				array_dest[bucket[pos]++] = array_source[i];
			}
		}

		private int getMSBPos(){

			int highest = array[from];
			for(int i = from+1; i < to; i++){
				if(array[i] > highest) highest = array[i];
			}
			int msb_pos = 0;
			while(highest > 0){
				highest = highest >>> 1;
				msb_pos++;
			}
			return msb_pos;
		}

		private void setZero(int[] array){
			for(int i = 0; i < array.length; i++) array[i] = 0;
		}
	}
}