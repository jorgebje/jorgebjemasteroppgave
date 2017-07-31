class CHTest{
	static int PROBLEM_SIZE;
	static int JIT_COMP_TEST_RUNS;
	static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	static int TESTS_TO_RUN; // S==1,P==2,U2==4,O2==8,O4==16,O6==32,O8==64
	static int PROBLEM_TYPE; // EgdeSquare == 1, EvenSquare == 2, EdgeCircle == 4. EvenCircle == 8
	static int PRINT_TO_FILE;
	public static void main(String[] args){
		handleArgs(args);
		
		int[][] xy = generatePoints(PROBLEM_SIZE);
		
		//run tests
		double time = runTest(xy);
		HelperClass.printStringPlusNumber("median: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time)));
		System.out.println("ms");

		if(PRINT_TO_FILE != 0){
			//print to file	
			String filename = createFilename();
			CSV.arrayToFile(filename,new double[]{HelperClass.toTimeMillis(time)});
			System.out.println("Result written til file:\n" + filename);
		}
	}

	static void handleArgs(String[] args){
		//intput
		//0: PROBLEM_SIZE
		//1: JIT_COMP_TEST_RUNS
		//2: TESTS_TO_RUN
		//3: PROBLEM_TYPE
		//4: PRINT_TO_FILE

		if(args.length < 5){
			System.out.println("ERROR: NOT ENOUGH ARGUMENTS");
			System.out.println("PROBLEM_SIZE");
			System.out.println("JIT_COMP_TEST_RUNS");
			System.out.println("TESTS_TO_RUN");
			System.out.println("PROBLEM_TYPE");
			System.out.println("PRINT_TO_FILE");
			System.exit(-1);
		}
		PROBLEM_SIZE = Integer.parseInt(args[0]);
		JIT_COMP_TEST_RUNS = Integer.parseInt(args[1]);
		TESTS_TO_RUN = Integer.parseInt(args[2]);
		PROBLEM_TYPE = Integer.parseInt(args[3]);
		PRINT_TO_FILE = Integer.parseInt(args[4]);
		System.out.println("PROBLEM_SIZE:       " + PROBLEM_SIZE);
		System.out.println("JIT_COMP_TEST_RUNS: " + JIT_COMP_TEST_RUNS);
		System.out.println("TESTS_TO_RUN:       " + TESTS_TO_RUN);
		System.out.println("PROBLEM_TYPE:       " + PROBLEM_TYPE);
		System.out.println("PRINT_TO_FILE:      " + PRINT_TO_FILE);
	}

	static int[][] generatePoints(int n){
		int[][] xy = null;
		System.out.println("Generating points");
		if(PROBLEM_TYPE == 1){
			xy = PointGeneration.getEdgeSquare(n);
		}
		else if(PROBLEM_TYPE == 2){
			xy = PointGeneration.getEvenSquare(n);
		}
		else if(PROBLEM_TYPE == 4){
			xy = PointGeneration.getEdgeCircle(n);
		}
		else{ //PROBLEM_TYPE == 8
			xy = PointGeneration.getEvenCircle(n);
		}
		System.out.println("Done generating points");
		return xy;
	}

	static double runTest(int[][] xy){
		double result;
		System.out.println("Running test");
		if(TESTS_TO_RUN == 1){
			//Sequential
			result = migateJITSeq(xy);
		}
		else if(TESTS_TO_RUN == 2){
			//Parallel
			result = migateJITPara(xy,NUMBER_OF_CORES);
		}
		else if(TESTS_TO_RUN == 4){
			//Underbooked(2)
			result = migateJITPara(xy,NUMBER_OF_CORES/2);
		}
		else if(TESTS_TO_RUN == 8){
			//Overbooked(2)
			result = migateJITPara(xy,NUMBER_OF_CORES*2);
		}
		else if(TESTS_TO_RUN == 16){
			//Overbooked(4)
			result = migateJITPara(xy,NUMBER_OF_CORES*4);
		}
		else if(TESTS_TO_RUN == 32){
			//Overbooked(6)
			result = migateJITPara(xy,NUMBER_OF_CORES*6);
		}
		else{ //(TESTS_TO_RUN == 64){
			//Overbooked(8)
			result = migateJITPara(xy,NUMBER_OF_CORES*8);
		}
		System.out.println("Done running test");
		return result;
	}

	static String createFilename(){
		String filename = "";

		if(PROBLEM_TYPE == 1){
			filename += "on_square-";
		}
		if(PROBLEM_TYPE == 2){
			filename += "in_square-";
		}
		if(PROBLEM_TYPE == 3){
			filename += "on_circle-";
		}
		if(PROBLEM_TYPE == 4){
			filename += "in_circle-";
		}


		if(TESTS_TO_RUN == 1){
			//Sequential
			filename += "sequential-";
		}
		if(TESTS_TO_RUN == 2){
			//Parallel
			filename += "parallel-";
		}
		if(TESTS_TO_RUN == 4){
			//Underbooked(2)
			filename += "underbooked-2-";
		}
		if(TESTS_TO_RUN == 8){
			//Overbooked(2)
			filename += "overbooked-2-";
		}
		if(TESTS_TO_RUN == 16){
			//Overbooked(4)
			filename += "overbooked-4-";
		}
		if(TESTS_TO_RUN == 32){
			//Overbooked(6)
			filename += "overbooked-6-";
		}
		if(TESTS_TO_RUN == 64){
			//Overbooked(8)
			filename += "overbooked-8-";
		}

		filename += "" + PROBLEM_SIZE + "-time.csv";
		return filename;
	}

	static long migateJITSeq(int[][] xy){
		long[] time = new long[JIT_COMP_TEST_RUNS];
		System.out.println("Sequential run: start, n = " + xy[0].length);
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			long start = System.nanoTime();
			int[] ret_values = ConvexHull.start(xy[0],xy[1],0,xy[0].length);
			long stop = System.nanoTime();
			time[i] = stop-start;
			HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			System.out.println("ms"  + ", points: " + ret_values.length);
		}
		System.out.println("Sequential run: stop");

		return HelperClass.getMedian(time);
	}

	static long migateJITPara(int[][] xy, int threads){
		long[] time = new long[JIT_COMP_TEST_RUNS];
		ConvexHullPara CHP = new ConvexHullPara();
		CHP.open(threads);
		System.out.println("Parallel run: start, n = " + xy[0].length + ", t = " + threads);
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			long start = System.nanoTime();
			int[] ret_values = CHP.start(xy[0],xy[1]);
			long stop = System.nanoTime();
			time[i] = stop-start;
			HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			System.out.println("ms"  + ", points: " + ret_values.length);
		}
		System.out.println("Parallel run: stop");
		CHP.close();
		return HelperClass.getMedian(time);
	}
}