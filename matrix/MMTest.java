class MMTest{
	static int JIT_COMP_TEST_RUNS;
	static int PROBLEM_SIZE;
	static int NUMBER_OF_CORES;
	static int TESTS_TO_RUN;
	static boolean PRINT_TO_FILE;

	static int BLOCK_SIZE = 512;

	static double[][] matrix1;
	static double[][] matrix2;
	static double[][] result;

	public static void main(String[] args){
		NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
		try{
			System.out.println("PROBLEM_SIZE:       " + args[0]);
			System.out.println("JIT_COMP_TEST_RUNS: " + args[1]);
			System.out.println("TESTS_TO_RUN:       " + args[2]);
			System.out.println("PRINT_TO_FILE:      " + args[3]);
			System.out.println("BLOCK_SIZE:         " + args[4]);
			for(int i = 5; i < args.length; i++){
				System.out.println("Others(unused): " + args[i]);
			}
		}catch(Exception e){}

		try{
			PROBLEM_SIZE = Integer.parseInt(args[0]);
		}catch(Exception e){
			System.out.println("Please specify the size of the problem");
			return;
		}
		try{
			JIT_COMP_TEST_RUNS = Integer.parseInt(args[1]);
		}catch(Exception e){
			JIT_COMP_TEST_RUNS = 1;
		}

		try{
			TESTS_TO_RUN = Integer.parseInt(args[2]);			
		}catch(Exception e){
			System.out.println("Please select test(s) to run");
			System.exit(-1);
		}

		try{
			if(Integer.parseInt(args[3]) == 1){
				PRINT_TO_FILE = true;
			}
			else{
				PRINT_TO_FILE = false;
			}
		}catch(Exception e){
			PRINT_TO_FILE = false;
		}

		try{
			BLOCK_SIZE = Integer.parseInt(args[4]);
		}catch(Exception e){}


		matrix1 = HelperClass.getRandMatrix(PROBLEM_SIZE);
		matrix2 = HelperClass.getRandMatrix(PROBLEM_SIZE);
		result = new double[PROBLEM_SIZE][PROBLEM_SIZE];
		
		long time_textbook = 0L;
		long time_textbook_para = 0L;
		long time_textbook_u2 = 0L;
		long time_textbook_o2 = 0L;
		long time_textbook_o4 = 0L;
		long time_textbook_o8 = 0L;
		long time_textbook_o12 = 0L;

		long time_transposed = 0L;
		long time_transposed_para = 0L;
		long time_transposed_u2 = 0L;
		long time_transposed_o2 = 0L;
		long time_transposed_o4 = 0L;
		long time_transposed_o8 = 0L;
		long time_transposed_o12 = 0L;

		long time_block = 0L;
		long time_block_para = 0L;
		long time_block_u2 = 0L;
		long time_block_o2 = 0L;
		long time_block_o4 = 0L;
		long time_block_o8 = 0L;
		long time_block_o12 = 0L;


		double speedup_textbook_para;
		double speedup_textbook_u2;
		double speedup_textbook_o2;
		double speedup_textbook_o4;
		double speedup_textbook_o8;
		double speedup_textbook_o12;

		double speedup_transposed;
		double speedup_transposed_para;
		double speedup_transposed_u2;
		double speedup_transposed_o2;
		double speedup_transposed_o4;
		double speedup_transposed_o8;
		double speedup_transposed_o12;

		double speedup_block;
		double speedup_block_para;
		double speedup_block_u2;
		double speedup_block_o2;
		double speedup_block_o4;
		double speedup_block_o8;
		double speedup_block_o12;

		// Warmup
		// if(JIT_COMP_TEST_RUNS <= 1){
		// 	if((TESTS_TO_RUN & 1) > 0){
		// 		//text
		// 		System.out.println("Warmup MMTextbook");
		// 		MMTextbook.start(matrix1, matrix2, result, PROBLEM_SIZE);
		// 	}
		// 	if((TESTS_TO_RUN & 2) > 0){
		// 		//text para
		// 		System.out.println("Warmup MMTextbookPara");
		// 		MMTextbookPara.start(matrix1, matrix2, result, PROBLEM_SIZE,NUMBER_OF_CORES);
		// 	}
		// 	if((TESTS_TO_RUN & 4) > 0){
		// 		//text u2
		// 		System.out.println("Warmup MMTextbookPara(1/2)");
		// 		MMTextbookPara.start(matrix1, matrix2, result, PROBLEM_SIZE,(int)(NUMBER_OF_CORES/2));
		// 	}
		// 	if((TESTS_TO_RUN & 8) > 0){
		// 		//text o2
		// 		System.out.println("Warmup MMTextbookPara(2)");
		// 		MMTextbookPara.start(matrix1, matrix2, result, PROBLEM_SIZE,NUMBER_OF_CORES*2);
		// 	}
		// 	if((TESTS_TO_RUN & 16) > 0){
		// 		//text o4
		// 		System.out.println("Warmup MMTextbookPara(4)");
		// 		MMTextbookPara.start(matrix1, matrix2, result, PROBLEM_SIZE,NUMBER_OF_CORES*4);
		// 	}
		// 	if((TESTS_TO_RUN & 32) > 0){
		// 		//text o8
		// 		System.out.println("Warmup MMTextbookPara(8)");
		// 		MMTextbookPara.start(matrix1, matrix2, result, PROBLEM_SIZE,NUMBER_OF_CORES*8);
		// 	}
		// 	if((TESTS_TO_RUN & 64) > 0){
		// 		//text o12
		// 		System.out.println("Warmup MMTextbookPara(12)");
		// 		MMTextbookPara.start(matrix1, matrix2, result, PROBLEM_SIZE,NUMBER_OF_CORES*12);
		// 	}
		// 	if((TESTS_TO_RUN & 128) > 0){
		// 		//transposed
		// 		System.out.println("Warmup MMTransposed");
		// 		MMTransposed.start(matrix1, matrix2, result, PROBLEM_SIZE);
		// 	}
		// 	if((TESTS_TO_RUN & 256) > 0){
		// 		//transposed para
		// 		System.out.println("Warmup MMTransposedPara");
		// 		MMTransposedPara.start(matrix1,matrix2, result, PROBLEM_SIZE, NUMBER_OF_CORES);
		// 	}
		// 	if((TESTS_TO_RUN & 512) > 0){
		// 		//transposed u2
		// 		System.out.println("Warmup MMTransposedPara(1/2)");
		// 		MMTransposedPara.start(matrix1,matrix2, result, PROBLEM_SIZE, (int)(NUMBER_OF_CORES/2));
		// 	}
		// 	if((TESTS_TO_RUN & 1024) > 0){
		// 		//transposed o2
		// 		System.out.println("Warmup MMTransposedPara(2)");
		// 		MMTransposedPara.start(matrix1,matrix2, result, PROBLEM_SIZE, NUMBER_OF_CORES*2);
		// 	}
		// 	if((TESTS_TO_RUN & 2048) > 0){
		// 		//transposed o4
		// 		System.out.println("Warmup MMTransposedPara(4)");
		// 		MMTransposedPara.start(matrix1,matrix2, result, PROBLEM_SIZE, NUMBER_OF_CORES*4);
		// 	}
		// 	if((TESTS_TO_RUN & 4096) > 0){
		// 		//transposed o8
		// 		System.out.println("Warmup MMTransposedPara(8)");
		// 		MMTransposedPara.start(matrix1,matrix2, result, PROBLEM_SIZE, NUMBER_OF_CORES*8);
		// 	}
		// 	if((TESTS_TO_RUN & 8192) > 0){
		// 		//transposed o12
		// 		System.out.println("Warmup MMTransposedPara(12)");
		// 		MMTransposedPara.start(matrix1,matrix2, result, PROBLEM_SIZE, NUMBER_OF_CORES*12);
		// 	}
		// 	if((TESTS_TO_RUN & 16384) > 0){
		// 		//block
		// 		System.out.println("Warmup MMBlockv2");
		// 		MMBlockv2.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE);
		// 	}
		// 	if((TESTS_TO_RUN & 32768) > 0){
		// 		//block para
		// 		System.out.println("Warmup MMBlockv2Para");
		// 		MMBlockv2Para.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES);
		// 	}
		// 	if((TESTS_TO_RUN & 65536) > 0){
		// 		//block u2
		// 		System.out.println("Warmup MMBlockv2Para(1/2)");
		// 		MMBlockv2Para.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE,(int)(NUMBER_OF_CORES/2));
		// 	}
		// 	if((TESTS_TO_RUN & 131072) > 0){
		// 		//block o2
		// 		System.out.println("Warmup MMBlockv2Para(2)");
		// 		MMBlockv2Para.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*2);
		// 	}
		// 	if((TESTS_TO_RUN & 262144) > 0){
		// 		//block o4
		// 		System.out.println("Warmup MMBlockv2Para(4)");
		// 		MMBlockv2Para.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*4);
		// 	}
		// 	if((TESTS_TO_RUN & 524288) > 0){
		// 		//block o8
		// 		System.out.println("Warmup MMBlockv2Para(8)");
		// 		MMBlockv2Para.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*8);
		// 	}
		// 	if((TESTS_TO_RUN & 1048576) > 0){
		// 		//block o12
		// 		System.out.println("Warmup MMBlockv2Para(12)");
		// 		MMBlockv2Para.start(matrix1,matrix2,result,PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*12);
		// 	}
		// }

		//run the tests

		if((TESTS_TO_RUN & 1) > 0){
			//text
			time_textbook = jitMMTextbook(PROBLEM_SIZE);
		}
		if((TESTS_TO_RUN & 2) > 0){
			//text para
			time_textbook_para = jitMMTextbookPara(PROBLEM_SIZE,NUMBER_OF_CORES);
		}
		if((TESTS_TO_RUN & 4) > 0){
			//text u2
			time_textbook_u2 = jitMMTextbookPara(PROBLEM_SIZE,(int)(NUMBER_OF_CORES/2));
		}
		if((TESTS_TO_RUN & 8) > 0){
			//text o2
			time_textbook_o2 = jitMMTextbookPara(PROBLEM_SIZE,NUMBER_OF_CORES*2);
		}
		if((TESTS_TO_RUN & 16) > 0){
			//text o4
			time_textbook_o4 = jitMMTextbookPara(PROBLEM_SIZE,NUMBER_OF_CORES*4);
		}
		if((TESTS_TO_RUN & 32) > 0){
			//text o8
			time_textbook_o8 = jitMMTextbookPara(PROBLEM_SIZE,NUMBER_OF_CORES*8);
		}
		if((TESTS_TO_RUN & 64) > 0){
			//text o12
			time_textbook_o12 = jitMMTextbookPara(PROBLEM_SIZE,NUMBER_OF_CORES*12);
		}
		if((TESTS_TO_RUN & 128) > 0){
			//transposed
			time_transposed = jitMMTransposed(PROBLEM_SIZE);
		}
		if((TESTS_TO_RUN & 256) > 0){
			//transposed para
			time_transposed_para = jitMMTransposedPara(PROBLEM_SIZE,NUMBER_OF_CORES);
		}
		if((TESTS_TO_RUN & 512) > 0){
			//transposed u2
			time_transposed_u2 = jitMMTransposedPara(PROBLEM_SIZE,NUMBER_OF_CORES/2);
		}
		if((TESTS_TO_RUN & 1024) > 0){
			//transposed o2
			time_transposed_o2 = jitMMTransposedPara(PROBLEM_SIZE,NUMBER_OF_CORES*2);
		}
		if((TESTS_TO_RUN & 2048) > 0){
			//transposed o4
			time_transposed_o4 = jitMMTransposedPara(PROBLEM_SIZE,NUMBER_OF_CORES*4);
		}
		if((TESTS_TO_RUN & 4096) > 0){
			//transposed o8
			time_transposed_o8 = jitMMTransposedPara(PROBLEM_SIZE,NUMBER_OF_CORES*8);
		}
		if((TESTS_TO_RUN & 8192) > 0){
			//transposed o12
			time_transposed_o12 = jitMMTransposedPara(PROBLEM_SIZE,NUMBER_OF_CORES*12);
		}
		if((TESTS_TO_RUN & 16384) > 0){
			//block
			time_block = jitMMBlockv2(PROBLEM_SIZE,BLOCK_SIZE);
		}
		if((TESTS_TO_RUN & 32768) > 0){
			//block para
			time_block_para = jitMMBlockv2Para(PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES);
		}
		if((TESTS_TO_RUN & 65536) > 0){
			//block u2
			time_block_u2 = jitMMBlockv2Para(PROBLEM_SIZE,BLOCK_SIZE,(int)(NUMBER_OF_CORES/2));
		}
		if((TESTS_TO_RUN & 131072) > 0){
			//block o2
			time_block_o2 = jitMMBlockv2Para(PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*2);
		}
		if((TESTS_TO_RUN & 262144) > 0){
			//block o4
			time_block_o4 = jitMMBlockv2Para(PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*4);
		}
		if((TESTS_TO_RUN & 524288) > 0){
			//block o8
			time_block_o8 = jitMMBlockv2Para(PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*8);
		}
		if((TESTS_TO_RUN & 1048576) > 0){
			//block o12
			time_block_o12 = jitMMBlockv2Para(PROBLEM_SIZE,BLOCK_SIZE,NUMBER_OF_CORES*12);
		}

		//select reference case
		long reference;
		if((TESTS_TO_RUN & 1) > 0){
			reference = time_textbook;
		}
		else if((TESTS_TO_RUN & 128) > 0){
			reference = time_transposed;
		}
		else{
			reference = time_block;
		}


		//calculate speedup
		speedup_textbook_para            = reference/(double)time_textbook_para;
		speedup_textbook_u2              = reference/(double)time_textbook_u2;
		speedup_textbook_o2              = reference/(double)time_textbook_o2;
		speedup_textbook_o4              = reference/(double)time_textbook_o4;
		speedup_textbook_o8              = reference/(double)time_textbook_o8;
		speedup_textbook_o12             = reference/(double)time_textbook_o12;

		speedup_transposed               = reference/(double)time_transposed;
		speedup_transposed_para          = reference/(double)time_transposed_para;
		speedup_transposed_u2            = reference/(double)time_transposed_u2;
		speedup_transposed_o2            = reference/(double)time_transposed_o2;
		speedup_transposed_o4            = reference/(double)time_transposed_o4;
		speedup_transposed_o8            = reference/(double)time_transposed_o8;
		speedup_transposed_o12           = reference/(double)time_transposed_o12;

		speedup_block                    = reference/(double)time_block;
		speedup_block_para               = reference/(double)time_block_para;
		speedup_block_u2                 = reference/(double)time_block_u2;
		speedup_block_o2                 = reference/(double)time_block_o2;
		speedup_block_o4                 = reference/(double)time_block_o4;
		speedup_block_o8                 = reference/(double)time_block_o8;
		speedup_block_o12                = reference/(double)time_block_o12;

		System.out.println("\n");


		//print, time
		if((TESTS_TO_RUN & 1) > 0){
			//text
			HelperClass.printStringPlusNumber("time_textbook:                  ", time_textbook/1000000.0);
		}
		if((TESTS_TO_RUN & 2) > 0){
			//text para
			HelperClass.printStringPlusNumber("time_textbook_para:             ", time_textbook_para/1000000.0);
		}
		if((TESTS_TO_RUN & 4) > 0){
			//text u2
			HelperClass.printStringPlusNumber("time_textbook_u2:               ", time_textbook_u2/1000000.0);
		}
		if((TESTS_TO_RUN & 8) > 0){
			//text o2
			HelperClass.printStringPlusNumber("time_textbook_o2:               ", time_textbook_o2/1000000.0);
		}
		if((TESTS_TO_RUN & 16) > 0){
			//text o4
			HelperClass.printStringPlusNumber("time_textbook_o4:               ", time_textbook_o4/1000000.0);
		}
		if((TESTS_TO_RUN & 32) > 0){
			//text o8
			HelperClass.printStringPlusNumber("time_textbook_o8:               ", time_textbook_o8/1000000.0);
		}
		if((TESTS_TO_RUN & 64) > 0){
			//text o12
			HelperClass.printStringPlusNumber("time_textbook_o12:               ", time_textbook_o12/1000000.0);
		}

		if((TESTS_TO_RUN & 128) > 0){
			//transposed
			HelperClass.printStringPlusNumber("time_transposed:                ", time_transposed/1000000.0);
		}
		if((TESTS_TO_RUN & 256) > 0){
			//transposed para
			HelperClass.printStringPlusNumber("time_transposed_para:           ", time_transposed_para/1000000.0);
		}
		if((TESTS_TO_RUN & 512) > 0){
			//transposed u2
			HelperClass.printStringPlusNumber("time_transposed_underbooked(2): ", time_transposed_u2/1000000.0);
		}
		if((TESTS_TO_RUN & 1024) > 0){
			//transposed o2
			HelperClass.printStringPlusNumber("time_transposed_overbooked(2):  ", time_transposed_o2/1000000.0);
		}
		if((TESTS_TO_RUN & 2048) > 0){
			//transposed o4
			HelperClass.printStringPlusNumber("time_transposed_overbooked(4):  ", time_transposed_o4/1000000.0);
		}
		if((TESTS_TO_RUN & 4096) > 0){
			//transposed o8
			HelperClass.printStringPlusNumber("time_transposed_overbooked(8):  ", time_transposed_o8/1000000.0);
		}
		if((TESTS_TO_RUN & 8192) > 0){
			//transposed o12
			HelperClass.printStringPlusNumber("time_transposed_overbooked(12): ", time_transposed_o12/1000000.0);
		}

		if((TESTS_TO_RUN & 16384) > 0){
			//block
			HelperClass.printStringPlusNumber("time_block:                     ", time_block/1000000.0);
		}
		if((TESTS_TO_RUN & 32768) > 0){
			//block para
			HelperClass.printStringPlusNumber("time_block_para:                ", time_block_para/1000000.0);
		}
		if((TESTS_TO_RUN & 65536) > 0){
			//block u2
			HelperClass.printStringPlusNumber("time_block_u2:                  ", time_block_u2/1000000.0);
		}
		if((TESTS_TO_RUN & 131072) > 0){
			//block o2
			HelperClass.printStringPlusNumber("time_block_o2:                  ", time_block_o2/1000000.0);
		}
		if((TESTS_TO_RUN & 262144) > 0){
			//block o4
			HelperClass.printStringPlusNumber("time_block_o4:                  ", time_block_o4/1000000.0);
		}
		if((TESTS_TO_RUN & 524288) > 0){
			//block o8
			HelperClass.printStringPlusNumber("time_block_o8:                  ", time_block_para/1000000.0);
		}
		if((TESTS_TO_RUN & 1048576) > 0){
			//block o12
			HelperClass.printStringPlusNumber("time_block_o12:                 ", time_block_para/1000000.0);
		}
		
		System.out.println("\n");


		//print, speedup
		if((TESTS_TO_RUN & 1) > 0){
			//text
		}
		if((TESTS_TO_RUN & 2) > 0){
			//text para
			HelperClass.printStringPlusNumber("speedup_textbook_para:             ", speedup_textbook_para);
		}
		if((TESTS_TO_RUN & 4) > 0){
			//text u2
			HelperClass.printStringPlusNumber("speedup_textbook_u2:               ", speedup_textbook_u2);
		}
		if((TESTS_TO_RUN & 8) > 0){
			//text o2
			HelperClass.printStringPlusNumber("speedup_textbook_o2:               ", speedup_textbook_o2);
		}
		if((TESTS_TO_RUN & 16) > 0){
			//text o4
			HelperClass.printStringPlusNumber("speedup_textbook_o4:               ", speedup_textbook_o4);
		}
		if((TESTS_TO_RUN & 32) > 0){
			//text o8
			HelperClass.printStringPlusNumber("speedup_textbook_o8:               ", speedup_textbook_o8);
		}
		if((TESTS_TO_RUN & 64) > 0){
			//text o12
			HelperClass.printStringPlusNumber("speedup_textbook_o12:              ", speedup_textbook_o12);
		}
		if((TESTS_TO_RUN & 128) > 0){
			//transposed
			HelperClass.printStringPlusNumber("speedup_transposed:                ", speedup_transposed);
		}
		if((TESTS_TO_RUN & 256) > 0){
			//transposed para
			HelperClass.printStringPlusNumber("speedup_transposed_para:           ", speedup_transposed_para);
		}
		if((TESTS_TO_RUN & 512) > 0){
			//transposed u2
			HelperClass.printStringPlusNumber("speedup_transposed_underbooked(2): ", speedup_transposed_u2);
		}
		if((TESTS_TO_RUN & 1024) > 0){
			//transposed o2
			HelperClass.printStringPlusNumber("speedup_transposed_overbooked(2):  ", speedup_transposed_o2);
		}
		if((TESTS_TO_RUN & 2048) > 0){
			//transposed o4
			HelperClass.printStringPlusNumber("speedup_transposed_overbooked(4):  ", speedup_transposed_o4);
		}
		if((TESTS_TO_RUN & 4096) > 0){
			//transposed o8
			HelperClass.printStringPlusNumber("speedup_transposed_overbooked(8):  ", speedup_transposed_o8);
		}
		if((TESTS_TO_RUN & 8192) > 0){
			//transposed o12
			HelperClass.printStringPlusNumber("speedup_transposed_overbooked(12): ", speedup_transposed_o12);
		}
		if((TESTS_TO_RUN & 16384) > 0){
			//block
			HelperClass.printStringPlusNumber("speedup_block:                     ", speedup_block);
		}
		if((TESTS_TO_RUN & 32768) > 0){
			//block para
			HelperClass.printStringPlusNumber("speedup_block_para:                ", speedup_block_para);
		}
		if((TESTS_TO_RUN & 65536) > 0){
			//block u2
			HelperClass.printStringPlusNumber("speedup_block_u2:                  ", speedup_block_u2);
		}
		if((TESTS_TO_RUN & 131072) > 0){
			//block o2
			HelperClass.printStringPlusNumber("speedup_block_o2:                  ", speedup_block_o2);
		}
		if((TESTS_TO_RUN & 262144) > 0){
			//block o4
			HelperClass.printStringPlusNumber("speedup_block_o4:                  ", speedup_block_o4);
		}
		if((TESTS_TO_RUN & 524288) > 0){
			//block o8
			HelperClass.printStringPlusNumber("speedup_block_o8:                  ", speedup_block_o8);
		}
		if((TESTS_TO_RUN & 1048576) > 0){
			//block o12
			HelperClass.printStringPlusNumber("speedup_block_o12:                 ", speedup_block_o12);
		}





		//print to file
		if(PRINT_TO_FILE){
			if((TESTS_TO_RUN & 1) > 0){
				//text
				CSV.arrayToFile("textbook-" + PROBLEM_SIZE + "-time.csv",                 new double[]{HelperClass.toTimeMillis(time_textbook)});
			}
			if((TESTS_TO_RUN & 2) > 0){
				//text para
				CSV.arrayToFile("textbook-para-" + PROBLEM_SIZE + "-time.csv",            new double[]{HelperClass.toTimeMillis(time_textbook_para)});
			}
			if((TESTS_TO_RUN & 4) > 0){
				//text u2
				CSV.arrayToFile("textbook-u2-" + PROBLEM_SIZE + "-time.csv",            new double[]{HelperClass.toTimeMillis(time_textbook_u2)});
			}
			if((TESTS_TO_RUN & 8) > 0){
				//text o2
				CSV.arrayToFile("textbook-o2-" + PROBLEM_SIZE + "-time.csv",            new double[]{HelperClass.toTimeMillis(time_textbook_o2)});
			}
			if((TESTS_TO_RUN & 16) > 0){
				//text o4
				CSV.arrayToFile("textbook-o4-" + PROBLEM_SIZE + "-time.csv",            new double[]{HelperClass.toTimeMillis(time_textbook_o4)});
			}
			if((TESTS_TO_RUN & 32) > 0){
				//text o8
				CSV.arrayToFile("textbook-o8-" + PROBLEM_SIZE + "-time.csv",            new double[]{HelperClass.toTimeMillis(time_textbook_o8)});
			}
			if((TESTS_TO_RUN & 64) > 0){
				//text o12
				CSV.arrayToFile("textbook-o12-" + PROBLEM_SIZE + "-time.csv",            new double[]{HelperClass.toTimeMillis(time_textbook_o12)});
			}
			if((TESTS_TO_RUN & 128) > 0){
				//transposed
				CSV.arrayToFile("transposed-" + PROBLEM_SIZE + "-time.csv",               new double[]{HelperClass.toTimeMillis(time_transposed)});
			}
			if((TESTS_TO_RUN & 256) > 0){
				//transposed para
				CSV.arrayToFile("transposed-para-" + PROBLEM_SIZE + "-time.csv",          new double[]{HelperClass.toTimeMillis(time_transposed_para)});
			}
			if((TESTS_TO_RUN & 512) > 0){
				//transposed u2
				CSV.arrayToFile("transposed-u2-" + PROBLEM_SIZE + "-time.csv", new double[]{HelperClass.toTimeMillis(time_transposed_u2)});
			}
			if((TESTS_TO_RUN & 1024) > 0){
				//transposed o2
				CSV.arrayToFile("transposed-o2-" + PROBLEM_SIZE + "-time.csv",  new double[]{HelperClass.toTimeMillis(time_transposed_o2)});
			}
			if((TESTS_TO_RUN & 2048) > 0){
				//transposed o4
				CSV.arrayToFile("transposed-o4-" + PROBLEM_SIZE + "-time.csv",  new double[]{HelperClass.toTimeMillis(time_transposed_o4)});
			}
			if((TESTS_TO_RUN & 4096) > 0){
				//transposed o8
				CSV.arrayToFile("transposed-o8-" + PROBLEM_SIZE + "-time.csv",  new double[]{HelperClass.toTimeMillis(time_transposed_o8)});
			}
			if((TESTS_TO_RUN & 8192) > 0){
				//transposed o12
				CSV.arrayToFile("transposed-o12-" + PROBLEM_SIZE + "-time.csv", new double[]{HelperClass.toTimeMillis(time_transposed_o12)});
			}
			if((TESTS_TO_RUN & 16384) > 0){
				//block
				CSV.arrayToFile("block-" + PROBLEM_SIZE + "-time.csv" ,           new double[]{HelperClass.toTimeMillis(time_block)});
			}
			if((TESTS_TO_RUN & 32768) > 0){
				//block para
				CSV.arrayToFile("block-para-" + PROBLEM_SIZE + "-time.csv" ,       new double[]{HelperClass.toTimeMillis(time_block_para)});
			}
			if((TESTS_TO_RUN & 65536) > 0){
				//block u2
				CSV.arrayToFile("block-u2-" + PROBLEM_SIZE + "-time.csv" ,       new double[]{HelperClass.toTimeMillis(time_block_u2)});
			}
			if((TESTS_TO_RUN & 131072) > 0){
				//block o2
				CSV.arrayToFile("block-o2-" + PROBLEM_SIZE + "-time.csv" ,       new double[]{HelperClass.toTimeMillis(time_block_o2)});
			}
			if((TESTS_TO_RUN & 262144) > 0){
				//block o4
				CSV.arrayToFile("block-o4-" + PROBLEM_SIZE + "-time.csv" ,       new double[]{HelperClass.toTimeMillis(time_block_o4)});
			}
			if((TESTS_TO_RUN & 524288) > 0){
				//block o8
				CSV.arrayToFile("block-o8-" + PROBLEM_SIZE + "-time.csv" ,       new double[]{HelperClass.toTimeMillis(time_block_o8)});
			}
			if((TESTS_TO_RUN & 1048576) > 0){
			//block o12
				CSV.arrayToFile("block-o12-" + PROBLEM_SIZE + "-time.csv" ,       new double[]{HelperClass.toTimeMillis(time_block_o12)});
			}
		}
	}

	private static long jitMMTextbook(int n){
		System.out.println("jitMMTextbook, n = " + n);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];	
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMTextbook.start(matrix1,matrix2,result,n);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMTextbook returning");

		return HelperClass.getMedianTimeUsed(time_used);
	}

	private static long jitMMTextbookPara(int n, int threads){
		System.out.println("jitMMTextbookPara, n = " + n + ", threads = " + threads);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMTextbookPara.start(matrix1,matrix2,result,n,threads);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMTextbookPara returning");

		return HelperClass.getMedianTimeUsed(time_used);
	}

	private static long jitMMTransposed(int n){
		System.out.println("jitMMTransposed, n = " + n);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMTransposed.start(matrix1,matrix2,result,n);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMTransposed returning");

		return HelperClass.getMedianTimeUsed(time_used);
	}

	private static long jitMMTransposedPara(int n, int threads){
		System.out.println("jitMMTransposedPara, n = " + n + ", threads = " + threads);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMTransposedPara.start(matrix1,matrix2,result,n,threads);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMTransposedPara returning");

		return HelperClass.getMedianTimeUsed(time_used);
	}	

	private static long jitMMBlock(int n, int block_size){
		System.out.println("jitMMBlock, n = " + n + ", block_size = " + block_size);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMBlock.start(matrix1,matrix2,result,n,block_size);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMBlock returning");

		return HelperClass.getMedianTimeUsed(time_used);
	}

	private static long jitMMBlockv2(int n, int block_size){
		System.out.println("jitMMBlockv2, n = " + n + ", block_size = " + block_size);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		// int[][] result = null;
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMBlockv2.start(matrix1,matrix2,result,n,block_size);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMBlockv2 returning");
		
		return HelperClass.getMedianTimeUsed(time_used);
	}

	private static long jitMMBlockv2Para(int n, int block_size, int threads){
		System.out.println("jitMMBlockv2Para, n = " + n + ", block_size = " + block_size + ", threads = " + threads);
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		// int[][] result = null;
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(result);
			System.out.print("  run number " + i);
			time_used[i] = -1 * System.nanoTime();
			MMBlockv2Para.start(matrix1,matrix2,result,n,block_size,threads);
			time_used[i] += System.nanoTime();
			System.out.println(", time used: " + HelperClass.toTwoDecimal(time_used[i]/(double)1000000) + "ms");
		}
		System.out.println("jitMMBlockv2Para returning");

		return HelperClass.getMedianTimeUsed(time_used);
	}
}