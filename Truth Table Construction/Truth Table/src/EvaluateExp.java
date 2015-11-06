import java.util.ArrayList;
import java.util.Stack;

public class EvaluateExp {

	/**
	 * This variable to store the postFix expression to be evaluated
	 */
	ArrayList<String> expression;

	/**
	 * The constructor to assign the postFix expression
	 * 
	 * @param logicExpression is the postFix expression.
	 */
	public EvaluateExp(ArrayList<String> logicExpression) {
		expression = logicExpression;
	}

	/**
	 * 
	 * @return true if the result of the expression is "T"
	 * @return false if the result of the expression is "F"
	 */
	public boolean getResult() {
		boolean result = false;

		Stack<Boolean> s = new Stack<Boolean>();
		for (int i = 0; i < expression.size(); i++) {
			if (expression.get(i).equals("T")) {
				s.push(true);
			} else if (expression.get(i).equals("F")) {
				s.push(false);
			} else {
				if (expression.get(i).equals("~")) {
					s.push(!s.pop());
				} else {
					result = evaluateOperation(s.pop(), s.pop(),
							expression.get(i));
					s.push(result);
				}
			}
		}
		return s.pop();
	}

	/**
	 * 
	 * @param operand2 the second operand
	 * @param operand1 the first operand
	 * @param operator will be AND, OR, NOT, implies, biconditional  
	 * @return true if the result of the expression is "T"
	 * @return false if the result of the expression is "F"
	 */
	public boolean evaluateOperation(boolean operand2, boolean operand1,
			String operator) {
		boolean result = false;
		if (operator.equals("^")) {
			result = operand1 & operand2;
		} else if (operator.equals("v")) {
			result = operand1 | operand2;
		} else if (operator.equals("-->")) {
			result = (!operand1) | operand2;
		} else if (operator.equals("<-->")) {
			result = (!operand1 & !operand2) || (operand1 & operand2);
		}
		return result;
	}

}
