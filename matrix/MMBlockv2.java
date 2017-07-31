class MMBlockv2{
	
	public static void start(double[][] m1, double[][] m2, double[][] return_val, int n, int block_size){
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

		// int n = m1.length;
		// int[][] return_val = new int[n][n];

		int block_count = n/block_size;

		int start_y = 0;
		int stop_y = block_size;

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
					innerLoop(start_y,stop_y,n,start_x,stop_x,start_calc,stop_calc,m1,m2,return_val);
					start_calc += block_size;
					stop_calc += block_size;
				}
				start_x += block_size;
				stop_x += block_size;
			}
			start_y += block_size;
			stop_y += block_size;
		}

		//transpose m2 again
		HelperClass.transposeMatrix(m2);
	}

	private static void outerLoop(){

	}

	private static void middleLoop(){

	}

	private static void innerLoop(
		int start_y, int stop_y, int n,
		int start_x, int stop_x,
		int start_calc, int stop_calc,
		double[][] m1, double[][] m2,
		double[][] return_val
		){
		for(int i = start_y; i < stop_y && i < n; i++){
			for(int j = start_x; j < stop_x && j < n; j++){
				for(int k = start_calc; k < stop_calc && k < n; k++){
					return_val[i][j] += 
						m1[i][k] * 
						m2[j][k];
				}
			}
		}
	}
}

		// //current result block y
		// for(int block_y = 0; block_y <= block_count; block_y++){
		// 	int start_x = 0;
		// 	int stop_x = block_size;

		// 	//current result block x
		// 	for(int block_x = 0; block_x <= block_count; block_x++){
		// 		int start_calc = 0;
		// 		int stop_calc = block_size;

		// 		//current calculation block
		// 		for(int block_calc = 0; block_calc <= block_count; block_calc++){

		// 			//calculate all the results from current calculation block and store them in the current result block
		// 			for(int i = start_y; i < stop_y && i < n; i++){
		// 				for(int j = start_x; j < stop_x && j < n; j++){
		// 					for(int k = start_calc; k < stop_calc && k < n; k++){
		// 						return_val[i][j] += 
		// 							m1[i][k] * 
		// 							m2[j][k];
		// 					}
		// 				}
		// 			}
		// 			start_calc += block_size;
		// 			stop_calc += block_size;
		// 		}
		// 		start_x += block_size;
		// 		stop_x += block_size;
		// 	}
		// 	start_y += block_size;
		// 	stop_y += block_size;
		// }