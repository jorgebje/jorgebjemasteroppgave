import java.util.Random;

class PointGeneration{
	public static int dist = 40000;
	public static int[][] getEdgeSquare(int N){

		boolean[][] b = new boolean[dist][dist];
		int[][] points = new int[2][N];

		Random R = new Random(123);

		int i = 0;
		while(i < N){
			int x,y;
			double num1 = R.nextDouble();
			int num2 = (int)(R.nextDouble()*dist);
			if(num1 < 0.25){
				x = num2;
				y = 0;
			}
			else if(num1 < 0.5){
				x = num2;
				y = dist-1;
			}
			else if(num1 < 0.75){
				x = 0;
				y = num2;
			}
			else{
				x = dist-1;
				y = num2;
			}

			if(!b[x][y]){
				points[0][i] = x;
				points[1][i] = y;
				b[x][y] = true;
				i++;
			}
		}
		return points;
	}

	public static int[][] getEvenSquare(int N){
		Random R = new Random(123);
		boolean[][] b = new boolean[dist][dist];
		int[][] points = new int[2][N];

		int i = 0;
		while(i < N){
			int x = (int)(dist*R.nextDouble());
			int y = (int)(dist*R.nextDouble());
			if(!b[x][y]){
				points[0][i] = x;
				points[1][i] = y;
				b[x][y] = true;
				i++;
			}
		}
		return points;
	}

	public static int[][] getEdgeCircle(int N){
		Random R = new Random(123);
		boolean[][] b = new boolean[dist][dist];
		int[][] points = new int[2][N];

		int i = 0;
		while(i < N){
			int x,y;
			double angle_rad = R.nextDouble()*2*Math.PI;
			x = (int)(dist/2+Math.cos(angle_rad)*dist/2);
			y = (int)(dist/2+Math.sin(angle_rad)*dist/2);
			if(!b[x][y]){
				points[0][i] = x;
				points[1][i] = y;
				b[x][y] = true;
				i++;
			}
		}
		return points;
	}

	public static int[][] getUnevenCircle(int N){
		Random R = new Random(123);
		boolean[][] b = new boolean[dist][dist];
		int[][] points = new int[2][N];

		int i = 0;
		while(i < N){
			double angle_rad = R.nextDouble()*2*Math.PI;
			int radius = (int)(R.nextDouble()*dist/2);
			int x = (int)(dist/2+Math.cos(angle_rad)*radius);
			int y = (int)(dist/2+Math.sin(angle_rad)*radius);
			if(!b[x][y]){
				points[0][i] = x;
				points[1][i] = y;
				b[x][y] = true;
				i++;
			}
		}
		return points;
	}

	public static int[][] getEvenCircle(int N){
		Random R = new Random(123);
		boolean[][] b = new boolean[dist][dist];
		int[][] points = new int[2][N];

		int i = 0;
		while(i < N){
			int x = (int)(dist*R.nextDouble());
			int y = (int)(dist*R.nextDouble());
			if(getDist(x,y,dist/2,dist/2) <= dist/2 && !b[x][y]){
				points[0][i] = x;
				points[1][i] = y;
				b[x][y] = true;
				i++;
			}
		}
		return points;
	}

	private static double getDist(int x1, int y1, int x2, int y2){
		int a = x2-x1;
		int b = y2-y1;
		a = a*a;
		b = b*b;
		int c = a+b;
		return Math.sqrt(c);
	}
}