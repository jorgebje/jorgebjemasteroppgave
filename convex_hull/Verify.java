class Verify{
	static int N = 100000;

	static int[] x;
	static int[] y;

	public static void main(String[] args){
		// sequential verification
		int[][] xy = PointGeneration.getEvenSquare(N);
		double co_max_dist = PointGeneration.dist*4;
		x = xy[0];
		y = xy[1];
		int[] co_hull_id = ConvexHull.start(x,y,0,N);

		// dist around
		double co_dist = distAround(co_hull_id,x,y);
		if(co_dist > co_max_dist) System.out.println("Dist Test FAILED");
		else System.out.println("Dist Test SUCCESS");

		// no illegal angles
		boolean angles = checkAngles(co_hull_id,x,y);
		if(angles) System.out.println("Angle Test SUCCESS");
		else System.out.println("Angle Test FAILED");

		// visual inspection on small number of points
		// if time, write a zoomable visual representation to view any number of points
		DrawCH DCH = new DrawCH(co_hull_id,xy[0],xy[1]);

		// parallel verification
		ConvexHullPara CHP = new ConvexHullPara();
		CHP.open(100);
		int[] co_hull_id_para = CHP.start(x,y);
		CHP.close();
		// compare to sequental solution
		if(co_hull_id.length != co_hull_id_para.length) System.out.println("Parallel Test FAILED");
		else{
			int i;
			for(i = 0; i < co_hull_id.length; i++){
				if(co_hull_id[i] != co_hull_id_para[i]){
					System.out.println("Parallel Test FAILED");
					break;
				}
			}
			if(i == co_hull_id.length) System.out.println("Parallel Test SUCCESS");
		}
	}

	private static double distAround(int[] id, int[] x, int[] y){
		double dist = 0;
		for(int i = 1; i < id.length; i++){
			dist += getDist(x[id[i]],y[id[i]],x[id[i-1]],y[id[i-1]]);
		}
		dist += getDist(x[id[id.length-1]],y[id[id.length-1]],x[id[0]],y[id[0]]);
		return dist;
	}

	private static double getDist(int x1, int y1, int x2, int y2){
		int a = x2-x1;
		int b = y2-y1;
		a = a*a;
		b = b*b;
		int c = a+b;
		return Math.sqrt(c);
	}

	private static boolean checkAngles(int[] id, int[] x, int[] y){
		for(int i = 2; i < id.length; i++){
			int dist_from_line = getDistFromLine(x[id[i-2]],y[id[i-2]],x[id[i-1]],y[id[i-1]],x[id[i]],y[id[i]]);
			if(dist_from_line > 0) return false;
			else if(dist_from_line == 0){
				double dist1 = getDist(x[id[i-2]],y[id[i-2]],x[id[i-1]],y[id[i-1]]);
				double dist2 = getDist(x[id[i-2]],y[id[i-2]],x[id[i]],y[id[i]]);
				if(dist2 < dist1) return false;
			}
		}
		int dist_from_line = getDistFromLine(x[id[id.length-2]],y[id[id.length-2]],x[id[id.length-1]],y[id[id.length-1]],x[id[0]],y[id[0]]);
		if(dist_from_line > 0) return false;
		else if(dist_from_line == 0){
			double dist1 = getDist(x[id[id.length-2]],y[id[id.length-2]],x[id[id.length-1]],y[id[id.length-1]]);
			double dist2 = getDist(x[id[id.length-2]],y[id[id.length-2]],x[id[0]],y[id[0]]);
			if(dist2 < dist1) return false;
		}

		dist_from_line = getDistFromLine(x[id[id.length-1]],y[id[id.length-1]],x[id[0]],y[id[0]],x[id[1]],y[id[1]]);
		if(dist_from_line > 0) return false;
		else if(dist_from_line == 0){
			double dist1 = getDist(x[id[id.length-1]],y[id[id.length-1]],x[id[0]],y[id[0]]);
			double dist2 = getDist(x[id[id.length-1]],y[id[id.length-1]],x[id[1]],y[id[1]]);
			if(dist2 < dist1) return false;
		}

		return true;
	}

	private static int getDistFromLine(int x1, int y1, int x2, int y2, int x3, int y3){
		int a = y1 - y2; 
		int b = x2 - x1; 
		int c = y2 * x1 - y1 * x2;

		//in our case there is no need to calculate Math.sqrt(a*a+b*b)
		int dist_from_line = a*x3+b*y3+c;

		return dist_from_line;
	}
}