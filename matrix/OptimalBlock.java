class OptimalBlock{
	static int BLOCK_SIZE;


	static double[][] a;
	static double[][] b;
	static double[][] c;

	static int JIT_COMP_TEST_RUNS = 5;
	static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args){

		BLOCK_SIZE = Integer.parseInt(args[0]);

		a = HelperClass.getRandMatrix(4096);
		b = HelperClass.getRandMatrix(4096);
		c = new double[4096][4096];

		double time = jitTest(4096,BLOCK_SIZE);

		System.out.println("BLOCK_SIZE: " + args[0] + ", time: " + (int)(time/1000000));


		// CSV.arrayToFile("finding_block_size", HelperClass.toTimeMillis(time));
	}

	static long jitTest(int n, int block_size){
		long[] time_used = new long[JIT_COMP_TEST_RUNS];
		for(int i = 0; i < JIT_COMP_TEST_RUNS; i++){
			HelperClass.setToZero(c);
			time_used[i] = -1 * System.nanoTime();
			MMBlockv2Para.start(a,b,c,n,block_size,NUMBER_OF_CORES);
			time_used[i] += System.nanoTime();
		}
		
		return HelperClass.getMedianTimeUsed(time_used);
	}
}