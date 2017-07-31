import java.util.Random;
class RadixVarThreadTest{
	static int PROBLEM_SIZE;
	static int JIT_COMP_TEST_RUNS;
	static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
	// static int NUMBER_OF_CORES = 4;
	// static int TESTS_TO_RUN; // S==1,P==2,U2==4,O2==8,O4==16,O6==32,O8==64
	static int BITS_TO_SET;
	static int PRINT_TO_FILE;
	public static void main(String[] args){
		handleArgs(args);
		
		int[] numbers0 = generateNumbers(PROBLEM_SIZE);
		
		//run tests
		double time[] = runTest(numbers0);
		for(int i = 0; i < time.length; i++){
			HelperClass.printStringPlusNumber("threads: " + (i*2+2) + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			System.out.println("ms");
		}

		// if(PRINT_TO_FILE != 0){
		// 	//print to file	
		// 	String filename = createFilename();
		// 	CSV.arrayToFile(filename,new double[]{HelperClass.toTimeMillis(time)});
		// 	System.out.println("Result written to file:\n" + filename);
		// }
	}

	static void handleArgs(String[] args){
		//intput
		//0: PROBLEM_SIZE
		//1: JIT_COMP_TEST_RUNS
		//2: BITS_TO_SET
		//3: PRINT_TO_FILE

		if(args.length < 4){
			System.out.println("ERROR: NOT ENOUGH ARGUMENTS");
			System.out.println("PROBLEM_SIZE");
			System.out.println("JIT_COMP_TEST_RUNS");
			System.out.println("BITS_TO_SET");
			System.out.println("PRINT_TO_FILE");
			System.exit(-1);
		}
		PROBLEM_SIZE = Integer.parseInt(args[0]);
		JIT_COMP_TEST_RUNS = Integer.parseInt(args[1]);
		BITS_TO_SET = Integer.parseInt(args[2]);
		PRINT_TO_FILE = Integer.parseInt(args[3]);
		System.out.println("PROBLEM_SIZE:       " + PROBLEM_SIZE);
		System.out.println("JIT_COMP_TEST_RUNS: " + JIT_COMP_TEST_RUNS);
		System.out.println("BITS_TO_SET:       " + BITS_TO_SET);
		System.out.println("PRINT_TO_FILE:      " + PRINT_TO_FILE);
	}

	static int[] generateNumbers(int n){
		int[] numbers = new int[n];
		Random R = new Random(123);
		for(int i = 0; i < n; i++){
			numbers[i] = (int)(R.nextDouble()*(1<<(BITS_TO_SET-1)));
		}
		return numbers;
	}

	static double[] runTest(int[] numbers){
		double result[] = new double[128];
		System.out.println("Running test");

		int pos = 0;
		for(int i = 2; i <= 256; i+=2){
			result[pos++] = migateJITPara(numbers,i);
		}


		System.out.println("Done running test");
		return result;
	}

	static String createFilename(){
		String filename = "" + BITS_TO_SET + "-";
		// if(TESTS_TO_RUN == 1){
		// 	//Sequential
		// 	filename += "sequential-";
		// }
		// if(TESTS_TO_RUN == 2){
		// 	//Parallel
		// 	filename += "parallel-";
		// }
		// if(TESTS_TO_RUN == 4){
		// 	//Underbooked(2)
		// 	filename += "underbooked-2-";
		// }
		// if(TESTS_TO_RUN == 8){
		// 	//Overbooked(2)
		// 	filename += "overbooked-2-";
		// }
		// if(TESTS_TO_RUN == 16){
		// 	//Overbooked(4)
		// 	filename += "overbooked-4-";
		// }
		// if(TESTS_TO_RUN == 32){
		// 	//Overbooked(6)
		// 	filename += "overbooked-6-";
		// }
		// if(TESTS_TO_RUN == 64){
		// 	//Overbooked(8)
		// 	filename += "overbooked-8-";
		// }

		filename += "" + PROBLEM_SIZE + "-time.csv";
		return filename;
	}

	static long migateJITPara(int[] numbers, int threads){
		long[] time = new long[JIT_COMP_TEST_RUNS];
		int[] workspace1 = new int[numbers.length];
		int[] workspace2 = new int[numbers.length];
		RadixSortParaV3 RSP3 = new RadixSortParaV3();
		RSP3.open(threads);
		System.out.println("Parallel run: start, n = " + numbers.length + ", t = " + threads);
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			for(int j = 0; j < numbers.length; j++){
				workspace1[j] = numbers[j]; //reset the numbers position
			}
			long start = System.nanoTime();
			RSP3.sort(workspace1,workspace2,8);
			long stop = System.nanoTime();
			time[i] = stop-start;
			HelperClass.printStringPlusNumber("\t run nr: " + i + ", time: ",HelperClass.toTwoDecimal(HelperClass.toTimeMillis(time[i])));
			System.out.println("ms");
		}
		RSP3.close();
		System.out.println("Parallel run: stop");
		return HelperClass.getMedian(time);
	}
}