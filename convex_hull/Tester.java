class Tester{
	public static void main(String[] args){
		PointGeneration.dist = 40000;
		int[][] xy = PointGeneration.getEvenCircle(100000);
		int[] id = ConvexHull.start(xy[0],xy[1],0,xy[0].length);
		new DrawCH(id,xy[0],xy[1]);
	}
}