class MMTestCorrect{
	static double[][] matrix1;
	static double[][] matrix2;

	final static int PROBLEM_SIZE = 1000;
	final static int BLOCK_SIZE = 490;

	public static void main(String[] args){
		matrix1 = HelperClass.getRandMatrix(PROBLEM_SIZE);
		matrix2 = HelperClass.getRandMatrix(PROBLEM_SIZE);
		double[][] correct = new double[PROBLEM_SIZE][PROBLEM_SIZE];
		HelperClass.setToZero(correct);
		MMTextbook.start(matrix1,matrix2,correct,PROBLEM_SIZE);

		double[][] toTest = new double[PROBLEM_SIZE][PROBLEM_SIZE];
		HelperClass.setToZero(toTest);
		System.out.print("transposed status: ");
		MMTransposed.start(matrix1,matrix2,toTest,PROBLEM_SIZE);
		if(!HelperClass.compMatrix(correct,toTest)){
			System.out.println("NOT OK");
		}
		else{
			System.out.println("OK");
		}

		HelperClass.setToZero(toTest);
		System.out.print("transposed para status: ");
		MMTransposedPara.start(matrix1,matrix2,toTest,PROBLEM_SIZE,Runtime.getRuntime().availableProcessors());
		if(!HelperClass.compMatrix(correct,toTest)){
			System.out.println("NOT OK");
		}
		else{
			System.out.println("OK");
		}

		HelperClass.setToZero(toTest);
		System.out.print("block status: ");
		MMBlock.start(matrix1,matrix2,toTest,PROBLEM_SIZE,BLOCK_SIZE);
		if(!HelperClass.compMatrix(correct,toTest)){
			System.out.println("NOT OK");
		}
		else{
			System.out.println("OK");
		}

		HelperClass.setToZero(toTest);
		System.out.print("blockv2 status: ");
		MMBlockv2.start(matrix1,matrix2,toTest,PROBLEM_SIZE,BLOCK_SIZE);
		if(!HelperClass.compMatrix(correct,toTest)){
			System.out.println("NOT OK");
		}
		else{
			System.out.println("OK");
		}

		HelperClass.setToZero(toTest);
		System.out.print("blockv2Para status: ");
		MMBlockv2Para.start(matrix1,matrix2,toTest,PROBLEM_SIZE,BLOCK_SIZE,2);
		if(!HelperClass.compMatrix(correct,toTest)){
			System.out.println("NOT OK");
		}
		else{
			System.out.println("OK");
		}

		// HelperClass.printResult(correct, toTest);

	}
}