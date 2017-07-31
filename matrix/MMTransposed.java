class MMTransposed{
	public static void start(double[][] m1, double[][] m2, double[][] return_matrix, int n){
		//declarations
		// int n = m1.length;

		//check input matrices
		if(m1.length != m1[0].length || m2.length != m2[0].length || m1.length != m2.length){
			System.out.println("Width and length of matrix dimensions are not equal");
			return;
		}

		//make a duplicate if m1 and m2 are the same matrix
		if(m1 == m2){
			m2 = HelperClass.clone(m2);
		}

		//transpose m2
		HelperClass.transposeMatrix(m2);

		//run matrix multiplication
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < n; k++){
					return_matrix[i][j] += m1[i][k] * m2[j][k];
				}
			}
		}

		//transpose m2 again
		HelperClass.transposeMatrix(m2);

		//return
		return;
	}
}