import java.util.Arrays;

class HelperClass{
	public static double[][] clone(double[][] in){
		int n = in.length, m = in[0].length;
		double[][] out = new double[n][m];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++){
				out[i][j] = in[i][j];
			}
		}
		return out;
	}

	public static double[][] transposeMatrixWithClone(double[][] in_matrix){
		double[][] out_matrix = new double[in_matrix[0].length][in_matrix.length];
		for(int i = 0; i < out_matrix.length; i++){
			for(int j = 0; j < out_matrix[0].length; j++){
				out_matrix[i][j] = in_matrix[j][i];
			}
		}
		return out_matrix;
	}

	public static void transposeMatrix(double[][] matrix){
		int n = matrix.length;
		if(n != matrix[0].length){
			System.out.println("In transposeMatrix: input got form int[n][m] not int[n][n]");
			return;
		}
		for(int i = 0; i < n; i++){
			for(int j = 0; j < i; j++){
				double temp = matrix[i][j];
				matrix[i][j] = matrix[j][i];
				matrix[j][i] = temp;
			}
		}
	}

	public static void printResult(double[][] matrix1,double[][] matrix2){
		for(int i = 0; i < matrix1.length; i++){
			for(int j = 0; j < matrix1[0].length; j++){
				double out = ((int)(100*matrix1[i][j]))/100.0;
				System.out.print(out + " ");
			}
			if(i == (int)(matrix1.length/2)){ System.out.print("=> ");}
			else {System.out.print("   ");}

			for(int j = 0; j < matrix2[0].length; j++){
				double out = ((int)(100*matrix2[i][j]))/100.0;
				System.out.print(out + " ");
			}
			System.out.println();
		}
	}

	public static double[][] getRandMatrix(int n){
		double[][] return_val = new double[n][n];

		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				return_val[i][j] = Math.random();
			}
		}
		return return_val;
	}

	public static long getMedianTimeUsed(long[] time_used){
		Arrays.sort(time_used);
		
		return time_used[(int)(time_used.length/2)];
	}

	public static boolean compMatrix(double[][] matrix1, double[][] matrix2){
		if(matrix1 == null || matrix2 == null)
			return false;
		if(matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length)
			return false;
		int n = matrix1.length;
		int m = matrix1[0].length;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++){
				if(matrix1[i][j] != matrix2[i][j]) return false;
			}
		}
		return true;
	}

	public static double toTwoDecimal(double d){
		d = (double)((int)(d*1000));
		if(d%10 < 5){
			d = (d-d%10)/1000;
		}
		else{
			d = (10+d-d%10)/1000;
		}
		return d;
	} 

	public static void printStringPlusNumber(String s, double d){
		System.out.println(s + "" + toTwoDecimal(d));
	}

	public static double[] toTimeMillis(long[] l){
		double[] d = new double[l.length];
		for(int i = 0; i < l.length; i++){
			d[i] = l[i]/1000000.0;
		}
		return d;
	}

	public static double toTimeMillis(long l){
		return l/1000000.0;
	}

	public static double[] toTimeMillis(double[] d){
		for(int i = 0; i < d.length; i++){
			d[i] = d[i]/1000000.0;
		}
		return d;
	}

	public static double toTimeMillis(double d){
		return d/1000000.0;
	}


	public static void setToZero(double[][] a){
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[i].length; j++){
				a[i][j] = 0;
			}
		}
	}

}