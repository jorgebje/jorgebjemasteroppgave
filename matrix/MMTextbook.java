class MMTextbook{
	public static void start(double[][] m1, double[][] m2, double[][] return_matrix, int n){
		//declarations
		// int n = m1.length;
		// int[][] return_matrix = new int[n][n];

		//check input matrices
		if(m1.length != m1[0].length || m2.length != m2[0].length || m1.length != m2.length){
			System.out.println("Width and length of matrix dimensions are not equal");
			return;
		}

		//run matrix multiplication
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < n; k++){
					return_matrix[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
	}
}