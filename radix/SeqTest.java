import java.util.Arrays;

class SeqTest{
	// static int[] test_size = {100};
	// static int[] test_size = {1000000000};
	static int[] test_size = {1000,10000,100000,1000000,10000000,100000000};
	// static int[] test_size = {10000000}; //10M

	static int CORE_COUNT = Runtime.getRuntime().availableProcessors();
	// static int CORE_COUNT = 1;
	static int JIT_SIZE = 15;
	public static void main(String[] args){

		RadixSortPara RSP = new RadixSortPara();
		RSP.open(CORE_COUNT);

		RadixSortParaV2 RSP2 = new RadixSortParaV2();
		RSP2.open(CORE_COUNT);

		RadixSortParaV3 RSP3 = new RadixSortParaV3();
		RSP3.open(CORE_COUNT);

		for(int i = 0; i < test_size.length; i++){
		  long[] time1 = new long[JIT_SIZE];
		  long[] time2 = new long[JIT_SIZE];
		  long[] time3 = new long[JIT_SIZE];
		  long[] time4 = new long[JIT_SIZE];
		  long[] time5 = new long[JIT_SIZE];
		  long[] time6 = new long[JIT_SIZE];

		  long[] taken1 = new long[JIT_SIZE];
		  long[] taken2 = new long[JIT_SIZE];
		  long[] taken3 = new long[JIT_SIZE];
		  long[] taken4 = new long[JIT_SIZE];
		  long[] taken5 = new long[JIT_SIZE];

		  int[] numbers0 = new int[test_size[i]];
		  int[] numbers1 = new int[test_size[i]];
		  int[] numbers2 = new int[test_size[i]];
		  int[] numbers3 = new int[test_size[i]];
		  int[] numbers4 = new int[test_size[i]];
		  int[] numbers5 = new int[test_size[i]];

		  int[] numbers_dummy = new int[test_size[i]];

		  for(int j = 0; j < numbers0.length; j++){
				numbers0[j] = (int)(Math.random()*(1<<23));
		  }

		  for(int j = 0; j < JIT_SIZE; j++){
			for(int k = 0; k < numbers1.length; k++){
				numbers1[k] = numbers0[k];
				numbers2[k] = numbers0[k];
				numbers3[k] = numbers0[k];
				numbers4[k] = numbers0[k];
				numbers5[k] = numbers0[k];
			}
			time1[j] = System.nanoTime();
			// Arrays.sort(numbers1);
			time2[j] = System.nanoTime();
			RadixSort.sort(numbers2,numbers_dummy,8);
			time3[j] = System.nanoTime();
			RSP.sort(numbers3,numbers_dummy,8);
			time4[j] = System.nanoTime();
			RSP2.sort(numbers4,numbers_dummy,8);
			time5[j] = System.nanoTime();
			RSP3.sort(numbers5,numbers_dummy,8);
			time6[j] = System.nanoTime();

			// // test sequential implementation
			// for(int k = 0; k < numbers1.length; k++){
			// 	if(numbers1[j] != numbers2[j]){
			// 		System.out.println("Error at k = " + k);
			// 		System.out.println("numbers1[k]:   " + numbers1[k]   + ", numbers2[k]:   " + numbers2[k]);
			// 		System.out.println("numbers1[k+1]: " + numbers1[k+1] + ", numbers2[k+1]: " + numbers2[k+1]);
			// 		System.exit(-1);
			// 	}
			// }

			// // test parallel implementation
			for(int k = 0; k < numbers1.length; k++){
				if(numbers2[k] != numbers3[k]){
					System.out.println("Error at k = " + k);
					System.out.println("numbers2[k]:   " + numbers2[k]   + ", numbers3[k]:   " + numbers3[k]);
					System.out.println("numbers2[k+1]: " + numbers2[k+1] + ", numbers3[k+1]: " + numbers3[k+1]);
					System.exit(-1);
				}
			}

			// test parallel implementation
			for(int k = 0; k < numbers3.length; k++){
				if(numbers3[k] != numbers4[k]){
					System.out.println("Error at k = " + k);
					System.out.println("numbers3[k]:   " + numbers3[k]   + ", numbers4[k]:   " + numbers4[k]);
					System.out.println("numbers3[k+1]: " + numbers3[k+1] + ", numbers4[k+1]: " + numbers4[k+1]);
					System.exit(-1);
				}
			}

			// test parallel implementation
			for(int k = 0; k < numbers4.length; k++){
				if(numbers4[k] != numbers5[k]){
					System.out.println("Error at k = " + k);
					System.out.println("numbers4[k]:   " + numbers4[k]   + ", numbers5[k]:   " + numbers5[k]);
					System.out.println("numbers4[k+1]: " + numbers4[k+1] + ", numbers5[k+1]: " + numbers5[k+1]);
					System.exit(-1);
				}
			}

			taken1[j] = time2[j]-time1[j];
			taken2[j] = time3[j]-time2[j];
			taken3[j] = time4[j]-time3[j];
			taken4[j] = time5[j]-time4[j];
			taken5[j] = time6[j]-time5[j];
			System.out.print(".");
		  }

		  Arrays.sort(taken1);
		  Arrays.sort(taken2);
		  Arrays.sort(taken3);
		  Arrays.sort(taken4);
		  Arrays.sort(taken5);

		  System.out.println("test_size = " + test_size[i] + ", threads: " + CORE_COUNT + ", Correct Result");
		  System.out.println("time Array.sort:      " + taken1[(int)(JIT_SIZE/2)]);
		  System.out.println("time RadixSort:       " + taken2[(int)(JIT_SIZE/2)]);
		  System.out.println("time RadixSortPara:   " + taken3[(int)(JIT_SIZE/2)]);
		  System.out.println("time RadixSortParaV2: " + taken4[(int)(JIT_SIZE/2)]);
		  System.out.println("time RadixSortParaV3: " + taken5[(int)(JIT_SIZE/2)] + "\n");
		}

		RSP.close();
		RSP2.close();
		RSP3.close();
	}
}