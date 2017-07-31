import java.util.Arrays;
import java.util.Random;

class FindOptimalRadix{
	static int test_size = 1000000; //1M
	// static int test_size = 10000000; //10M
	// static int test_size = 100000000; //100M

	static int JIT_SIZE = 15;
	public static void main(String[] args){
		int[] numbers_dummy = new int[test_size];
		int[] numbers0 = new int[test_size];
		int[] numbers1 = new int[test_size];
		Random R = new Random(123);
		for(int i = 0; i < numbers0.length; i++){
			numbers0[i] = (int)(R.nextDouble()*Integer.MAX_VALUE);
		}

		for(int radix = 1; radix <= 16; radix++){
			long[] time1 = new long[JIT_SIZE];
			long[] time2 = new long[JIT_SIZE];
			long[] taken = new long[JIT_SIZE];

			for(int i = 0; i < JIT_SIZE; i++){
				for(int j = 0; j < numbers0.length; j++){
					numbers1[j] = numbers0[j];
				}
				time1[i] = System.nanoTime();
				RadixSort.sort(numbers1,numbers_dummy,radix);
				time2[i] = System.nanoTime();
				taken[i] = time2[i]-time1[i];
				System.out.print(".");
			}
	
			Arrays.sort(taken);
	
			System.out.println("\ntest_size = " + test_size + "radix: " + radix);
			System.out.println("time RadixSort:      " + taken[(int)(JIT_SIZE/2)] + "\n");
		}
	}
}