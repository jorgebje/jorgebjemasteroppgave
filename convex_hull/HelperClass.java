import java.util.Arrays;

class HelperClass{
	public static long getMedian(long[] values){
		Arrays.sort(values);
		
		return values[(int)(values.length/2)];
	}

	public static double toTwoDecimal(double d){
		d = (double)((long)(d*1000));
		if(d%10 < 5){
			d = (d-d%10)/1000;
		}
		else{
			d = (10+d-d%10)/1000;
		}
		return d;
	} 

	public static void printStringPlusNumber(String s, double d){
		System.out.print(s + "" + toTwoDecimal(d));
	}

	public static double[] toTimeMillis(long[] l){
		double[] d = new double[l.length];
		for(int i = 0; i < l.length; i++){
			d[i] = l[i]/1000000.0;
		}
		return d;
	}

	public static double toTimeMillis(long l){
		return l/1000000.0;
	}

	public static double[] toTimeMillis(double[] d){
		for(int i = 0; i < d.length; i++){
			d[i] = d[i]/1000000.0;
		}
		return d;
	}

	public static double toTimeMillis(double d){
		return d/1000000.0;
	}
}