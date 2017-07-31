import java.util.Arrays;

class PartTest{
	// static int N = 100; //100
	// static int N = 1000; //1K
	// static int N = 10000; //10K
	// static int N = 100000; //100K
	// static int N = 1000000; //1M
	// static int N = 10000000; //10M
	static int N = 100000000; //100M
	// static int N = 134217728;
	// static int N = 1000000000; //1B
	// static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	static int NUMBER_OF_CORES = 256;
	static int JIT_COMP_TEST_RUNS = 15;
	public static void main(String[] args){
		System.out.println("NUMBER_OF_CORES:" + NUMBER_OF_CORES);
		//create points
		int[][] xy = PointGeneration.getEvenSquare(N);

		int[] correct_res = ConvexHull.start(xy[0],xy[1],0,N);

		//init test method
		ConvexHullPara CHP = new ConvexHullPara();
		CHP.open(NUMBER_OF_CORES);

		long[] time = new long[JIT_COMP_TEST_RUNS];



		//test point div time
		System.out.println("testStart:");
		for(int i = 0; i < NUMBER_OF_CORES; i++){
			long start = System.nanoTime();
			testStart(xy[0],xy[1],CHP);
			long stop = System.nanoTime();
			time[i] = stop-start;
			HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			System.out.println("ms");
		}
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(HelperClass.getMedian(time))));
		System.out.println("ms");


		// test parallel execution time
		System.out.println("testThread:");
		for(int i = 0; i < NUMBER_OF_CORES; i++){
			long start = System.nanoTime();
			testThread(CHP);
			long stop = System.nanoTime();
			time[i] = stop-start;
			HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			System.out.println("ms");
		}
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(HelperClass.getMedian(time))));
		System.out.println("ms");

		// test different types of merging
		int[] tm1 = null,tm2 = null,tm3 = null,tm4 = null;

		//test merge time
		System.out.println("testMerge1:");
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			long start = System.nanoTime();
			tm1 = testMerge1(CHP);
			long stop = System.nanoTime();
			time[i] = stop-start;
			// HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			// System.out.println("ms");
		}
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(HelperClass.getMedian(time))));
		System.out.println("ms");


		//test merge time
		System.out.println("testMerge2:");
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			long start = System.nanoTime();
			tm2 = testMerge2(CHP);
			long stop = System.nanoTime();
			time[i] = stop-start;
			// HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			// System.out.println("ms");
		}
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(HelperClass.getMedian(time))));
		System.out.println("ms");


		//test merge time
		System.out.println("testMerge3:");
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			long start = System.nanoTime();
			tm3 = testMerge3(CHP);
			long stop = System.nanoTime();
			time[i] = stop-start;
			// HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			// System.out.println("ms");
		}
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(HelperClass.getMedian(time))));
		System.out.println("ms");


		//test merge time
		System.out.println("testMerge4:");
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			long start = System.nanoTime();
			tm4 = testMerge4(CHP);
			long stop = System.nanoTime();
			time[i] = stop-start;
			// HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			// System.out.println("ms");
		}
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(HelperClass.getMedian(time))));
		System.out.println("ms");

		CHP.close();

		//verify all test results
		for(int i = 0; i < tm1.length; i++){
			if(correct_res[i] != tm1[i]){
				System.out.println("TM1 verify failed");
				break;
			}
		}
		for(int i = 0; i < tm1.length; i++){
			if(correct_res[i] != tm2[i]){
				System.out.println("TM2 verify failed");
				break;
			}
		}
		for(int i = 0; i < tm1.length; i++){
			if(correct_res[i] != tm3[i]){
				System.out.println("TM3 verify failed");
				break;
			}
		}
		for(int i = 0; i < tm1.length; i++){
			if(correct_res[i] != tm4[i]){
				System.out.println("TM4 verify failed");
				break;
			}
		}
	}

	private static void testStart(int[] x, int[] y, ConvexHullPara CHP){
		//split points between workers
		CHP.x = x;
		CHP.y = y;
		for(int i = 0; i < CHP.t; i++){
			int start = (int)(i*(x.length/(double)CHP.t));
			int stop = (int)((i+1)*(x.length/(double)CHP.t));
			int size = stop-start;

			CHP.workers[i].offset = start;
			CHP.workers[i].size = size;
		}
	}

	private static void testThread(ConvexHullPara CHP){
		//start workers
		try{
			CHP.CB.await();
		}catch(Exception e){}

		//wait for workers
		try{
			CHP.CB.await();
		}catch(Exception e){}

	}

	private static int[] testMerge(ConvexHullPara CHP){

		/* merge all at once */
		int[] id = CHP.mergeConvexHulls(CHP.id,CHP.x,CHP.y);
		System.out.println("id.length: " + id.length);
		// return id;

		/* merge by new call to createConvexHull */
		int xy_length = 0;
		for(int i = 0; i < CHP.id.length; i++){
			xy_length += CHP.id[i].length;
		}
		System.out.println("xy_length: " + xy_length);

		int[][] xy = new int[3][xy_length];
		int xy_pos = 0;
		for(int i = 0; i < CHP.id.length; i++){
			for(int j = 0; j < CHP.id[i].length; j++){
				xy[0][xy_pos] = CHP.x[CHP.id[i][j]];
				xy[1][xy_pos] = CHP.y[CHP.id[i][j]];
				xy[2][xy_pos++] = CHP.id[i][j];
			}
		}

		int[] id2 = ConvexHull.start(xy[0],xy[1],0,xy_length);

		int[] id3 = new int[id2.length];

		for(int i = 0; i < id2.length; i++){
			id3[i] = xy[2][id2[i]];
		}
		System.out.println("id3.length: " + id3.length);

		System.out.println("\n merge:");
		for(int i = 0; i < id.length; i++){
			System.out.println("id[" + i + "] = " + id[i] + ", x: " + CHP.x[id[i]] + ", y: " + CHP.y[id[i]]);
		}

		System.out.println("\n redo:");
		for(int i = 0; i < id3.length; i++){
			System.out.println("id3[" + i + "] = " + id3[i] + ", x: " + CHP.x[id3[i]] + ", y: " + CHP.y[id3[i]]);
		}

		return id3;

	}

	private static int[] testMerge0(ConvexHullPara CHP){
		return CHP.id[0];
	}

	private static int[] testMerge1(ConvexHullPara CHP){
		int xy_length = 0;
		for(int i = 0; i < CHP.id.length; i++){
			xy_length += CHP.id[i].length;
		}

		int[][] xy = new int[3][xy_length];
		int xy_pos = 0;
		for(int i = 0; i < CHP.id.length; i++){
			for(int j = 0; j < CHP.id[i].length; j++){
				xy[0][xy_pos] = CHP.x[CHP.id[i][j]];
				xy[1][xy_pos] = CHP.y[CHP.id[i][j]];
				xy[2][xy_pos++] = CHP.id[i][j];
			}
		}

		int[] id2 = ConvexHull.start(xy[0],xy[1],0,xy_length);
		int[] id3 = new int[id2.length];

		for(int i = 0; i < id2.length; i++){
			id3[i] = xy[2][id2[i]];
		}

		return id3;

	}

	private static int[] testMerge2(ConvexHullPara CHP){
		int[] id = CHP.id[0];
		for(int i = 1; i < CHP.id.length; i++){
			int[][] to_merge = new int[][]{id,CHP.id[i]};
			id = CHP.mergeNHulls(to_merge,CHP.x,CHP.y);
		}
		return id;

	}

	private static int[] testMerge3(ConvexHullPara CHP){
		/* merge binary */
		int[][] id = CHP.id;
		int[][] new_id;
		while(id.length > 1){
			if(id.length % 2 == 0){
				new_id = new int[id.length/2][];
			}
			else{
				new_id = new int[id.length/2+1][];
			}
			for(int i = 0; i < new_id.length; i++){
				if(i*2+1 == id.length){
					new_id[i] = id[i*2];
				}
				else{
					int[][] to_merge = new int[][]{id[i*2],id[i*2+1]};
					new_id[i] = CHP.mergeNHulls(to_merge,CHP.x,CHP.y);
				}
			}
			id = new_id;
		}
		return id[0];
		/* END merge binary */
	}

	private static int[] testMerge4(ConvexHullPara CHP){
		return CHP.mergeNHulls(CHP.id,CHP.x,CHP.y);
	}
}