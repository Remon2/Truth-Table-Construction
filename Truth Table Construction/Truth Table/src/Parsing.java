import java.util.ArrayList;
import java.util.Stack;

/**
 * This class is used for parsing the input expression and validate it for evaluation.
 * @author Remon
 *
 */
public class Parsing {
	private static ArrayList<String> variables;

	/**
	 * This method is used to return array list of the variables in the input expression.
	 * @return --ArrayList of expression variables.
	 */
	public static ArrayList<String> getVariables() {
		return variables;
	}

	/**
	 * This method parsing the input expression in array list.
	 * @param expression
	 * @return --Array list of Strings of the infix expression.
	 */
	static ArrayList<String> parseExpression(String expression) {
		ArrayList<String> infix_Expression = new ArrayList<String>();
		int length = expression.length();
		for (int i = 0; i < length; i++) {
			if ((expression.charAt(i) >= 'a' && expression.charAt(i) <= 'z')
					|| (expression.charAt(i) >= 'A' && expression.charAt(i) <= 'Z')
					|| expression.charAt(i) == '^' || expression.charAt(i) == 'v'
					|| expression.charAt(i) == '~' || expression.charAt(i) == '('
					|| expression.charAt(i) == ')') {
				infix_Expression.add(expression.charAt(i) + "");
			} else if (expression.charAt(i) == ' ') {
				// We Accept any number of spaces
				continue;
			} else if ((i + 2) < length && expression.charAt(i) == '-'
					&& expression.charAt(i + 1) == '-' && expression.charAt(i + 2) == '>') {
				infix_Expression.add("-->");
				i = i + 2;
			} else if ((i + 3) < length && expression.charAt(i) == '<'
					&& expression.charAt(i + 1) == '-' && expression.charAt(i + 2) == '-'
					&& expression.charAt(i + 3) == '>') {
				infix_Expression.add("<-->");
				i = i + 3;
			} else {
				// invalid Input
				return null;
			}
		}
		return infix_Expression;
	}

	/**
	 * This method convert the infix expression to the postfix.
	 * @param infix
	 * @return --Array list of strings of the postfix expression.
	 */
	public static ArrayList<String> toPostfix(ArrayList<String> infix) {
		variables = new ArrayList<String>();
		ArrayList<String> postfix = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		if (infix == null) {
			return null;
		}
		int length = infix.size();
		for (int i = 0; i < length; i++) {
			if (infix.get(i).equals("~")) {
				stack.push("~");
			} else if (infix.get(i).equals("^")) {
				if (stack.isEmpty() || stack.peek().equals("(")) {
					stack.push("^");
				} else {
					while (!stack.isEmpty()&& (stack.peek().equals("~") || stack.peek().equals("^"))) {
						postfix.add(stack.pop());
					}
					stack.push("^");
				}
			} else if (infix.get(i).equals("v")) {
				if (stack.isEmpty() || stack.peek().equals("(")) {
					stack.push("v");
				} else {
					while (!stack.isEmpty()&& (stack.peek().equals("~")|| stack.peek().equals("^") || stack.peek().equals("v"))) {
						postfix.add(stack.pop());
					}
					stack.push("v");
				}
			} else if (infix.get(i).equals("-->")) {
				if (stack.isEmpty() || stack.peek().equals("(")) {
					stack.push("-->");
				} else {
					while (!stack.isEmpty()&& (stack.peek().equals("~")|| stack.peek().equals("^")|| stack.peek().equals("v") || stack.peek().equals("-->"))) {
						postfix.add(stack.pop());
					}
					stack.push("-->");
				}
			} else if (infix.get(i).equals("<-->")) {
				if (stack.isEmpty() || stack.peek().equals("(")) {
					stack.push("<-->");
				} else {
					while (!stack.isEmpty()&& (stack.peek().equals("~")|| stack.peek().equals("^")|| stack.peek().equals("v")|| stack.peek().equals("-->") || stack.peek().equals("<-->"))) {
						postfix.add(stack.pop());
					}
					stack.push("<-->");
				}
			} else if (infix.get(i).equals("(")) {
				stack.push(infix.get(i));
			} else if (infix.get(i).equals(")")) {
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					postfix.add(stack.pop());
				}
				if (!stack.isEmpty())
					stack.pop();
			} else {
				postfix.add(infix.get(i));
				if (!isExist_In_Variables(infix.get(i))) {
					variables.add(infix.get(i) + "");
				}

			}
		}

		while (!stack.isEmpty()) {
			postfix.add(stack.pop());
		}
		return postfix;
	}

	/**
	 * This method takes one parameter String variable and check if this variable exists in the variables array or not.
	 * @param variable
	 * @return --true if it is a valid variable.
	 * @return --false if it is not a valid variable.
	 */
	private static boolean isExist_In_Variables(String variable) {
		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).equals(variable))
				return true;
		}
		return false;
	}

	/**
	 * This method check the validation of the postfix expression and the infix also.
	 * @param infix
	 * @param postfix
	 * @return --true if the infix and the postfix are valid expressions.
	 * @return --false if the infix and the postfix are not valid expressions.
	 */
	static boolean isValid(ArrayList<String> infix, ArrayList<String> postfix) {
		Stack<String> stack = new Stack<String>();
		if (infix == null || infix.size() == 0) {
			return false;
		}
		int counter = 0;
		int length = infix.size();
		for (int i = 0; i < length; i++) {
			if (isVariable(infix.get(i))) {
				for (int index = i; index < length; index++) {
					if (isOperator(infix.get(index))
							|| infix.get(index).equals("~")) {
						if (infix.get(index).equals("~")) {
							return false;
						} else {
							break;
						}
					}
				}
			}

			if ((i + 1) < length && isVariable(infix.get(i))
					&& isVariable(infix.get(i + 1))) {
				return false;
			}else if((i+1)<length&&infix.get(i).equals(")")&&infix.get(i+1).equals("(")){
				return false;
			} else if ((i + 1) < length && infix.get(i).equals("(")
					&& infix.get(i + 1).equals(")")) {
				return false;
			} else if (infix.get(i).equals("(")) {
				stack.push("(");
				counter++;
			} else if (infix.get(i).equals(")")) {
				counter--;
				if (stack.isEmpty()) {
					return false;
				} else {
					stack.pop();
				}
			}else if((i+1)<length&&isVariable(infix.get(i))&&infix.get(i+1).equals("(")){
				return false;
			}else if((i-1)<length&&(i-1)>=0&&isVariable(infix.get(i))&&infix.get(i-1).equals(")")){
				return false;
			}

			if ((infix.get(i).equals("^") || infix.get(i).equals("v")
					|| infix.get(i).equals("-->") || infix.get(i)
					.equals("<-->"))
					&& (i + 1) < length
					&& isOperator(infix.get(i + 1))) {
				return false;
			}
			if (infix.get(i).equals("~") && (i + 1) < length
					&& !isVariable(infix.get(i + 1))
					&& !infix.get(i + 1).equals("(")) {
				return false;
			}
			if (infix.get(i).equals("~") && (i + 1) == length) {
				return false;
			}

		}
		if (counter != 0) {
			return false;
		}
		if(!postfixIsValid(postfix)){
			return false;
		}
		return true;
	}
	

	/**
	 * This method check the validation of the postfix expression.
	 * @param postfix
	 * @return --true  if the postfix is a valid expression.
	 * @return --false if the postfix is not a valid expression.
	 */
	private static boolean postfixIsValid(ArrayList<String> postfix) {
		Stack<String>stack = new Stack<String>();
		int length = postfix.size();
		for (int i = 0; i < length; i++) {
			if (postfix.get(i).equals("^") || postfix.get(i).equals("v")
					|| postfix.get(i).equals("-->")
					|| postfix.get(i).equals("<-->")) {
				if (stack.size() >= 2) {
					stack.pop();
				} else {
					return false;
				}
			} else if (postfix.get(i).equals("~")) {
				if (!(stack.size() >= 1)) {
					return false;
				}
			} else if (isVariable(postfix.get(i))) {
				stack.push(postfix.get(i));
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method check the validation of the operator.
	 * @param operator
	 * @return --true  if the operator is a valid operator.
	 * @return --false if the operator is not a valid operator.
	 */
	private static boolean isOperator(String operator) {
		if (operator.equals("^") || operator.equals("v") || operator.equals("-->")
				|| operator.equals("<-->"))
			return true;
		return false;
	}

	
	/**
	 * This method takes one parameter String variable and check if it is variable or not.
	 * @param var
	 * @return --true if it is a valid variable.
	 * @return --false if it is not a valid variable.
	 */
	private static boolean isVariable(String var) {
		if (var.length() > 1) {
			return false;
		}
		char variable = var.charAt(0);
		if (variable == 'v') {
			return false;
		}
		if (!((variable >= 'A' && variable <= 'Z') || (variable >= 'a' && variable <= 'z'))) {
			return false;
		}
		return true;
	}
}
