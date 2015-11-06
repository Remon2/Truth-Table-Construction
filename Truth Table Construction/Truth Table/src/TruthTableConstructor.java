import java.util.ArrayList;

public class TruthTableConstructor {

	ArrayList<String> exp;
	ArrayList<String> variables;
	/**
	 * Constructor for the truth table class.
	 * @param expression the postfix expression
	 * @param v the variables of the expression
	 */
	public TruthTableConstructor(ArrayList<String> expression,
			ArrayList<String> v) {
		exp = expression;
		variables = v;
	}
	/**
	 * Insert zero's at the left in the binary number like '110 = 00110' 
	 * @param i the binary integer 
	 * @return Binary string with zero's if avaible at left. 
	 */
	private String returnBinaryString(int i) {
		String index = Integer.toBinaryString(i);
		int sizeOfIndex = index.length();
		if (index.length() < variables.size()) {
			for (int j = 0; j < variables.size() - sizeOfIndex; j++) {
				index = "0" + index;
			}
			return index;
		} else {
			return index;
		}
	}
	/**
	 * Evaluate the logic expression
	 * @param input the assigned values of each variable
	 * @return 'T' if the result is true.
	 * @return 'F' if the result is false.
	 */
	private char getOutput(String input) {
		ArrayList<String> logicExpression = new ArrayList<String>();
		int i = 0;
		
		while (i < exp.size()) {
			int j = 0; 
			for (j = 0;  j < variables.size(); j++) {				
				if (variables.get(j).equals(exp.get(i))) {
					logicExpression.add(Character.toString(input.charAt(j)));
					j = variables.size();
				}
			}
			if (j == variables.size()) {
				logicExpression.add(exp.get(i));
			}
			i++;
		}
		EvaluateExp e = new EvaluateExp(logicExpression);
		if (e.getResult() == true) {
			return 'T';
		}else {
			return 'F';
		}
	}
	/**
	 * Convert the zero's and one's String to "T" and "F" String
	 * @param input zero's and one's String
	 * @return String with "T" and "F" only.
	 */
	private String toLogicValues(String input) {
		String logicValues = "";
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '0') {
				logicValues =  logicValues + "F";
			}else {
				logicValues =  logicValues + "T";
			}
		}
		return logicValues;
	}
	/**
	 * 
	 * @return An array two dimension containig the input String and output (truth table) 
	 */
	public String[][] getTruthTable() {
		ArrayList<String> truthTable = new ArrayList<String>();
		
		for (int i = 0; i < Math.pow(2, variables.size()) ; i++) {
			String input = returnBinaryString(i);
			input = toLogicValues(input);
			String output = Character.toString(getOutput(input));
			truthTable.add(input + output);
		}
		
		String[][] table = new String[(int) Math.pow(2, variables.size())]
				[variables.size()+1];
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				table[i][j] =  Character.toString(truthTable.get(i).charAt(j));
			}
		}
		return table;
	}
	/**
	 * 
	 * @param expression1 the logic expression number 1
	 * @param v1 the variables of expression number one
	 * @param expression2 the logic expression number 2
	 * @param v2 the variables of expression number two
	 * @return true if the two expression are equivalent
	 * @return false if the two expression are not equivalent
	 */
	public static boolean isEquivalent(ArrayList<String> expression1,
			ArrayList<String> v1,ArrayList<String> expression2,
			ArrayList<String> v2) {
		if (v1.size() != v2.size() ) {
			return false;
		}else {
			for (int i = 0; i < v1.size(); i++) {
				boolean isContained = false;
				for (int j = 0; j < v2.size(); j++) {
					if (v1.get(i).equals(v2.get(j))) {
						j = v2.size();
						isContained = true;
					}
				}
				if (!isContained) {
					return false;
				}
			}
		}
		TruthTableConstructor t1 = new TruthTableConstructor(expression1, v1);
		TruthTableConstructor t2 = new TruthTableConstructor(expression2, v2);
		String[][] table1 = t1.getTruthTable();
		String[][] table2 = t2.getTruthTable();
		int lastIndex = table1[0].length-1;
		// test for output for each truth table
		for (int i = 0; i < table1.length; i++) {
			if (!table1[i][lastIndex].
					equals(table2[i][lastIndex])) {
				return false;
			}
		}
		return true;
	}

	

}
