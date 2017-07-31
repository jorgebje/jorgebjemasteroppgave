import java.util.concurrent.CyclicBarrier;

class MMBlockv2Para implements Runnable{
	int thread_from,thread_to,n,block_size;
	double[][] m1,m2,return_matrix;

	CyclicBarrier CB;

	MMBlockv2Para(
		int thread_from, int thread_to, int block_size,
		double[][] m1, double[][] m2, double[][] return_matrix,
		CyclicBarrier CB
		){
		this.thread_from = thread_from;
		this.thread_to = thread_to;
		this.n = m1.length;
		this.block_size = block_size;
		this.m1 = m1;
		this.m2 = m2;
		this.return_matrix = return_matrix;
		this.CB = CB;
	}

	public void run(){
		//run matrix multiplication for this threads range
		int block_count = n/block_size;
		outerLoop(block_count);
	}

	private void outerLoop(int block_count){

		int start_y = thread_from;
		int stop_y = thread_to;

		//current result block y
		for(int block_y = 0; block_y <= block_count; block_y++){
			int start_x = 0;
			int stop_x = block_size;

			//current result block x
			for(int block_x = 0; block_x <= block_count; block_x++){
				int start_calc = 0;
				int stop_calc = block_size;

				//current calculation block
				for(int block_calc = 0; block_calc <= block_count; block_calc++){

					//calculate all the results from current calculation block and store them in the current result block
					innerLoop(start_y,stop_y,n,start_x,stop_x,start_calc,stop_calc,m1,m2,return_matrix);
					start_calc += block_size;
					stop_calc += block_size;
					//wait for this thread to be done with the current calculation block
					// try{	
					// 	CB.await();
					// }catch(Exception e){}

				}
				start_x += block_size;
				stop_x += block_size;
			}
			start_y += block_size;
			stop_y += block_size;
		}
	}

	private void innerLoop(
		int start_y, int stop_y, int n,
		int start_x, int stop_x,
		int start_calc, int stop_calc,
		double[][] m1, double[][] m2,
		double[][] return_matrix
		){
		for(int i = start_y; i < stop_y && i < n; i++){
			for(int j = start_x; j < stop_x && j < n; j++){
				for(int k = start_calc; k < stop_calc && k < n; k++){
					return_matrix[i][j] += 
						m1[i][k] * 
						m2[j][k];
				}
			}
		}
	}


	public static void start(double[][] m1,double[][] m2, double[][] return_matrix, int n, int block_size, int thread_count){
		//declarations
		// int n = m1.length;
		MMBlockv2Para[] worker = new MMBlockv2Para[thread_count];
		Thread[] worker_thread = new Thread[thread_count];
		
		//check input matrices
		if(m1.length != m1[0].length && m2.length != m2[0].length && m1.length != m2.length){
			System.out.println("Width and length of matrix dimensions are not equal");
			return;
		}

		//make a duplicate if m1 and m2 are the same matrix
		if(m1 == m2){
			m2 = HelperClass.clone(m2);
		}

		//transpose m2
		HelperClass.transposeMatrix(m2);

		//create CyclicBarrier
		CyclicBarrier CB = new CyclicBarrier(thread_count);

		
		//create threads
		for(int i = 0; i < thread_count; i++){
			int thread_from = i*block_size/thread_count;
			int thread_to = (i+1)*block_size/thread_count;

			worker[i] = new MMBlockv2Para(
				thread_from, thread_to, block_size,
				m1,m2,return_matrix, CB
			);
		}

		//start threads
		for(int i = 0; i < thread_count; i++){
			(worker_thread[i] = new Thread(worker[i])).start();
		}

		//await threads
		for(int i = 0; i < thread_count; i++){
			try{
				worker_thread[i].join();
			}catch(Exception e){}
		}

		//transpose m2 again
		HelperClass.transposeMatrix(m2);

		return;
	}
}