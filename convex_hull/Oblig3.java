class Oblig3{
	int N = 100;
	int n = N;
	int[] x;
	int[] y;

	int MAX_X = 100;
	int MAX_Y = 100;

	int T = 16;

	public static void main(String[] args){
		Oblig3 O3 = new Oblig3();
		O3.run();
	}
	private void run(){

		PointGeneration.dist = 100;
		int[][] xy = PointGeneration.getEvenSquare(N);
		// int[][] xy = PointGeneration.getEvenCircle(N);
		// int[][] xy = PointGeneration.getUnevenCircle(N);

		x = xy[0];
		y = xy[1];

		/*seq run */
		// System.out.println("START RUN");
		// int[] id = ConvexHull.start(x,y,0,n);
		// System.out.println("END RUN");
		// for(int i = 0; i < id.length; i++){
		// 	// System.out.println("id: " + id[i] + ", x: " + x[id[i]] + ", y: " + y[id[i]]);
		// 	System.out.println("id: " + id[i]);
		// }
		/*seq run end */

		/*para run*/
		ConvexHullPara CHP = new ConvexHullPara(T);
		System.out.println("START RUN");
		int[] id = CHP.start(x,y);
		System.out.println("END RUN");
		for(int i = 0; i < id.length; i++){
			System.out.println("id: " + id[i] + ", x: " + x[id[i]] + ", y: " + y[id[i]]);
		}
		/*para run end*/

		/*DRAW*/
		DrawCH DCH = new DrawCH(this, new IntList(id));
		System.out.println("id.length: " + id.length);


		while(true){
			try{
				Thread.sleep(1000);
			}catch(Exception e){}
		}
	}
}

class IntList{
	int[] map;

	IntList(int[] map){
		this.map = map;
	}

	public int get(int pos){
		return map[pos];
	}

	public int size(){
		return map.length;
	}
}