class MMTextbookPara implements Runnable{
	int from,to,n;
	double[][] m1,m2,return_matrix;

	MMTextbookPara(
		int from, int to, 
		double[][] m1, double[][] m2, double[][] return_matrix
		){
		this.from = from;
		this.to = to;
		this.n = m1.length;
		this.m1 = m1;
		this.m2 = m2;
		this.return_matrix = return_matrix;
	}

	public void run(){
		//run matrix multiplication for this threads range
		for(int i = from; i < to; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < n; k++){
					return_matrix[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
	}

	public static void start(double[][] m1,double[][] m2,double[][] return_matrix,int n,int thread_count){
		//declarations
		// int n = m1.length;
		// int[][] return_matrix = new int[n][n];
		MMTextbookPara[] worker = new MMTextbookPara[thread_count];
		Thread[] worker_thread = new Thread[thread_count];
		
		//check input matrices
		if(m1.length != m1[0].length || m2.length != m2[0].length || m1.length != m2.length){
			System.out.println("Width and length of matrix dimensions are not equal");
			return;
		}

		//create threads
		for(int i = 0; i < thread_count; i++){
			worker[i] = new MMTextbookPara(
				(int)(i*(n/(double)thread_count)),
				(int)((i+1)*(n/(double)thread_count)),
				m1,m2,return_matrix
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
	}
}