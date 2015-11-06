import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class EquivalenceGui {

	static JButton equivalent;
	static JButton back;
	static JTextField textField1;
	static JTextField textField2;
	static JFrame frame;
	static JPanel north_Panel;
	static JPanel center_Panel;
	static JScrollPane scrollPane1;
	static JScrollPane scrollPane2;

	/**
	 * set the frame of the equivalent page
	 * 
	 * @param f
	 */
	public static void setFrame(JFrame f) {
		frame = f;
	}

	/**
	 * set the panels of the frame visible
	 * 
	 * @param isVisible
	 */
	public static void setVisible(boolean isVisible) {
		north_Panel.setVisible(isVisible);
		center_Panel.setVisible(isVisible);
	}

	/**
	 * Run the frame of equivalent page
	 */
	public static void run() {
		textField1 = new JTextField(40);
		textField2 = new JTextField(40);

		equivalent = new JButton("Test equivalence");
		back = new JButton("Back");

		north_Panel = new JPanel();
		center_Panel = new JPanel();

		north_Panel.add(new JLabel("Enter two Expressions : "));
		north_Panel.add(textField1);
		north_Panel.add(textField2);
		north_Panel.add(equivalent);
		north_Panel.add(back);

		frame.setTitle("Test equivalence");
		frame.add(north_Panel, BorderLayout.NORTH);
		set_Back_Function();
		set_equivalent_Function();
	}

	/**
	 * set the functionality of the back button
	 */
	public static void set_Back_Function() {
		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				north_Panel.setVisible(false);

				if (scrollPane1 != null) {
					scrollPane1.setVisible(false);
				}
				if (scrollPane2 != null) {
					scrollPane2.setVisible(false);
				}
				center_Panel.setVisible(false);
				RunFrame.setVisibilily(true);
			}
		});
	}

	/**
	 * set the functionality of the test equivalent button
	 */
	public static void set_equivalent_Function() {
		equivalent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				center_Panel.removeAll();
				ArrayList<String> parse1 = return_Parse(textField1);
				ArrayList<String> postfix1 = Parsing.toPostfix(parse1);
				ArrayList<String> variables1 = Parsing.getVariables();

				ArrayList<String> parse2 = return_Parse(textField2);
				ArrayList<String> postfix2 = Parsing.toPostfix(parse2);
				ArrayList<String> variables2 = Parsing.getVariables();

				if (Parsing.isValid(parse1, postfix1)
						&& Parsing.isValid(parse2, postfix2)) {

					scrollPane1 = new JScrollPane(create_Table(postfix1,
							variables1));
					scrollPane2 = new JScrollPane(create_Table(postfix2,
							variables2));

					frame.getContentPane().add(scrollPane1, BorderLayout.WEST);
					frame.getContentPane().add(scrollPane2, BorderLayout.EAST);

					conclusionOfTest(postfix1, postfix2, variables1, variables2);
				} else {
					testValiadation(postfix1, postfix2, parse1, parse2);
				}
			}
		});
	}

	/**
	 * Test the validation of each expression written in the text field
	 * if the expression is false a message dialog will appear
	 * @param postfix1
	 * @param postfix2
	 * @param parse1
	 * @param parse2
	 */
	public static void testValiadation(ArrayList<String> postfix1,
			ArrayList<String> postfix2, ArrayList<String> parse1,
			ArrayList<String> parse2) {
		if (!Parsing.isValid(parse1, postfix1)
				&& Parsing.isValid(parse2, postfix2)) {
			JOptionPane.showMessageDialog(null, "Expression #1 is not valid!");
		} else if (Parsing.isValid(parse1, postfix1)
				&& !Parsing.isValid(parse2, postfix2)) {
			JOptionPane.showMessageDialog(null, "Expression #2 is not valid!");
		} else {
			JOptionPane
					.showMessageDialog(null, "Two Expression are not valid!");
		}
		return;
	}
	/**
	 * Write in a JLabel if the two truth tables are equivalent or not.
	 * @param postfix1
	 * @param postfix2
	 * @param variables1
	 * @param variables2
	 */
	public static void conclusionOfTest(ArrayList<String> postfix1,
			ArrayList<String> postfix2, ArrayList<String> variables1,
			ArrayList<String> variables2) {
		if (TruthTableConstructor.isEquivalent(postfix1, variables1, postfix2,
				variables2)) {
			JLabel isEquivalent = new JLabel("The 2 expressions are equivalent");
			center_Panel.add(isEquivalent);
		} else {
			JLabel isNotEquivalent = new JLabel(
					"The 2 expressions are not equivalent");
			center_Panel.add(isNotEquivalent);
		}

		frame.add(center_Panel, BorderLayout.SOUTH);
		center_Panel.revalidate();
	}

	public static ArrayList<String> return_Parse(JTextField t) {
		return Parsing.parseExpression(t.getText());
	}
	/**
	 * Create the truth table
	 * @param postfix
	 * @param variables
	 * @return a table containing the truth table
	 */
	public static JTable create_Table(ArrayList<String> postfix,
			ArrayList<String> variables) {
		TruthTableConstructor truthTable = new TruthTableConstructor(postfix,
				variables);
		String[][] data = truthTable.getTruthTable();
		String[] columnNames = new String[variables.size() + 1];
		for (int i = 0; i < columnNames.length - 1; i++) {
			columnNames[i] = variables.get(i);
		}
		columnNames[variables.size()] = "Output";
		JTable table = new JTable(data, columnNames);
		table.setEnabled(false);
		table.setPreferredScrollableViewportSize(new Dimension(600, 70));
		return table;
	}
}
