import java.io.FileWriter;

class CSV{
	public static void arrayToFile(String filename, int[] array){
		arrayToFile(filename,array,false);
	}
	public static void arrayToFile(String filename, int[] array, boolean append){
		FileWriter fw = null;
		try{
			// open file
			fw = new FileWriter(filename,append);

			if(append) fw.write(",");

			//write to file
			for(int i = 0; i < array.length-1; i++){
				fw.write((new Integer(array[i])).toString());
				fw.write(",");
			}
			fw.write((new Integer(array[array.length-1])).toString());

			//close file
			fw.close();
		}catch(Exception e){System.out.println("error writing to file in MatlabPort");}
	}

	public static void arrayToFile(String filename, double[] array){
		arrayToFile(filename,array,false);
	}

	public static void arrayToFile(String filename, double[] array, boolean append){
		FileWriter fw = null;
		try{
			// open file
			fw = new FileWriter(filename,append);

			if(append) fw.write(",");

			//write to file
			for(int i = 0; i < array.length-1; i++){
				fw.write((new Double(array[i])).toString());
				fw.write(",");
			}
			fw.write((new Double(array[array.length-1])).toString());

			//close file
			fw.close();
		}catch(Exception e){System.out.println("error writing to file in MatlabPort");}		

	}
}