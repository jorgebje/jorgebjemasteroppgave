class MMBlock{
	
	public static void start(double[][] m1, double[][] m2, double[][] return_val, int n, int block_size){
		if(m1.length != m1[0].length && m2.length != m2[0].length && m1.length != m2.length){
			System.out.println("Width and length of matrix dimensions are not equal");
			return;
		}
		if(n > block_size & n % block_size != 0){
			System.out.println("This function does not support n % block_size != 0");
		}

		//make a duplicate if m1 and m2 are the same matrix
		if(m1 == m2){
			m2 = HelperClass.clone(m2);
		}

		//transpose m2
		HelperClass.transposeMatrix(m2);
		if(block_size > m1.length) block_size = m1.length;

		// int n = m1.length;
		// int[][] return_val = new int[n][n];

		int block_count = n/block_size;

		//start: not my code, borrowed from another student
		outerLoop(
			block_count,
			n,
			block_size,
			return_val,m1,m2);
		//end: not my code

		// transpose m2 again
		HelperClass.transposeMatrix(m2);
	}

	private static void outerLoop(
			int block_count,
			int n,
			int block_size,
			double[][] return_val,	double[][] m1, double[][] m2
	){
		for(int block_i = 0; block_i < block_count; block_i++){
			for(int block_j = 0; block_j < block_count; block_j++){
				middleLoop(n,
					block_size, block_i, block_j,
					return_val,	m1, m2);
			}
		}
	}

	private static void middleLoop(
			int n,
			int block_size, int block_i, int block_j,
			double[][] return_val,	double[][] m1, double[][] m2
	){
		for(int k = 0; k < n; k++){
			innerLoop(block_size,block_i,block_j,k,
				return_val,m1,m2);
		}
	}

	private static void innerLoop(
			int block_size, int block_i, int block_j, int k,
			double[][] return_val,	double[][] m1, double[][] m2
	){
		for(int i = 0; i < block_size; i++){
			for(int j = 0; j < block_size; j++){
				return_val[block_i * block_size + i]
				          [block_j * block_size + j] +=
					m1[block_i * block_size + i][k] * 
					m2[block_j * block_size + j][k];
			}
		}
	}

	private static void allLoop(
			int block_count,
			int n,
			int block_size,
			double[][] return_val,	double[][] m1, double[][] m2
	){
		for(int block_i = 0; block_i < block_count; block_i++){
			for(int block_j = 0; block_j < block_count; block_j++){
				for(int k = 0; k < n; k++){
					for(int i = 0; i < block_size; i++){
						for(int j = 0; j < block_size; j++){
							return_val[block_i * block_size + i]
							          [block_j * block_size + j] +=
								m1[block_i * block_size + i][k] * 
								m2[block_j * block_size + j][k];
						}
					}
				}
			}
		}
	}
}