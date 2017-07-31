public class RadixSort{
	/**
	 ** @param array_from   array to sort from
	 ** @param array_to     array to sort to
	 ** @param bucket       bucket used as storage
	 ** @param rightshift   how many bits to rightshift
	 ** @param mask         a mask to remove unwanted numbers
	 **/
	private static void sortPart(int[] array_from, int[] array_to, int[] bucket, int rightshift, int mask){
		//find size of each bucket
		int size = bucket.length;
		int length = array_from.length;
		for(int i = 0; i < length; i++){
			int pos = (array_from[i]>>>rightshift)&mask;
			bucket[pos]++;
		}

		//calc start pos of each bucket
		int sum = 0;
		int tmp = 0;
		for(int i = 0; i < size; i++){
			tmp = bucket[i];
			bucket[i] = sum;
			sum += tmp;
		}

		//move numbers to correct part after consulting with bucket
		for(int i = 0; i < length; i++){
			int pos = (array_from[i]>>>rightshift)&mask;
			array_to[bucket[pos]++] = array_from[i];
		}
	}

	/**
	 ** Sort the array provided by using the radix sorting algorithm
	 ** @param array  the array to sort
	 ** @param bits   number of bits to sort by in each step
	 **/
	public static void sort(int[] array,int[] array_dup, int bits){
		int bits_to_sort = getMSBPos(array);
		// int bits_to_sort = 32;
		int bits_left = bits_to_sort;

		int bucket[] = new int[1<<bits];

		int swaps = 0;

		int rightshift = 0;
		int mask = (1<<bits)-1;
		while(bits_to_sort >= rightshift){
			sortPart(array,array_dup,bucket,rightshift,mask);
			int[] tmp = array;
			array = array_dup;
			array_dup = tmp;
			bits_left -= bits;
			swaps++;
			setZero(bucket);
			rightshift += bits;
		}

		if((swaps&1) == 1){
			//odd number of swaps, copy
			System.arraycopy(array,0,array_dup,0,array.length);
		}
	}

	private static int getMSBPos(int[] array){
		int highest = array[0];
		for(int i = 1; i < array.length; i++){
			if(array[i] > highest) highest = array[i];
		}
		int msb_pos = 0;
		while(highest > 0){
			highest = highest >>> 1;
			msb_pos++;
		}
		return msb_pos;
	}

	private static void setZero(int[] array){
		for(int i = 0; i < array.length; i++) array[i] = 0;
	}
}