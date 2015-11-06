import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * This class is used to run the GUI of the application.
 * 
 * @author Remon
 * 
 */
public class RunFrame extends JFrame {
	private JLabel label;
	private JLabel label2;
	private JTextField textField;
	private JButton showButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton testEquivalence;
	private static JPanel north_Panel;
	private static JPanel center_Panel;
	private String[][] data;
	private String[] columnNames;
	private JTable table;
	private ArrayList<String> parse;
	private ArrayList<String> postfix;
	private String expression;
	private JScrollPane scrollPane;
	private boolean flag;
	private int counter;
	private JFileChooser fileDialog;
	private static JFrame frame;

	public static void setVisibilily(boolean isVisible) {
		north_Panel.setVisible(isVisible);
		frame.add(center_Panel, BorderLayout.SOUTH);
	}

	/**
	 * This is a constructor we use it initialize the GUI components.
	 */
	public RunFrame() {
		frame = this;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		label = new JLabel("Enter The Expression : ");
		label2 = new JLabel("");
		Event event = new Event();
		textField = new JTextField(40);
		showButton = new JButton("Show Table");
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		testEquivalence = new JButton("Test equivalence");
		flag = false;
		north_Panel = new JPanel();
		center_Panel = new JPanel();
		new JPanel();
		north_Panel.add(label);
		north_Panel.add(textField);
		north_Panel.add(showButton);
		north_Panel.add(saveButton);
		north_Panel.add(loadButton);
		north_Panel.add(testEquivalence);
		north_Panel.add(label2);
		showButton.addActionListener(event);
		saveButton.addActionListener(event);
		loadButton.addActionListener(event);
		testEquivalence.addActionListener(event);
		saveButton.setEnabled(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		add(north_Panel, BorderLayout.NORTH);
		add(center_Panel, BorderLayout.CENTER);
		setTitle("Truth Table Construction developed by Mina Makram and Remon Hanna");
		setVisible(true);
		counter = 0;
	}

	/**
	 * This Class is used to implements the ActionListener of the buttons.
	 * 
	 * @author Remon
	 * 
	 */
	private class Event implements ActionListener {
		
		long startTime = System.nanoTime();
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == showButton) {
				showButtonAction();
			} else if (e.getSource() == saveButton) {
				saveButtonAction();
			} else if (e.getSource() == loadButton) {
				loadButtonAction();
			} else if (e.getSource() == testEquivalence) {
				north_Panel.setVisible(false);
				if (scrollPane != null) {
					scrollPane.setVisible(false);
				}
				EquivalenceGui.setFrame(frame);
				EquivalenceGui.run();
				EquivalenceGui.setVisible(true);
			}
			
			long endTime = System.nanoTime();
			System.out.println("Variables = "+ Parsing.getVariables());
			System.out.println("Took "+(endTime - startTime) + " ns"); 
			System.out.println("-----------------------------------------------------------");
		}

		private void loadButtonAction() {
			if (fileDialog == null)
				fileDialog = new JFileChooser();
			fileDialog.setDialogTitle("Select File to be Opened");
			fileDialog.setSelectedFile(null); // No file is initially
												// selected.
			int option = fileDialog.showOpenDialog(null);
			if (option != JFileChooser.APPROVE_OPTION)
				return; // User canceled or clicked the dialog's close box.
			File selectedFile = fileDialog.getSelectedFile();
			String fileName = selectedFile.getName();
			if ((!(fileName.charAt(fileName.length() - 1) == 't'
					&& fileName.charAt(fileName.length() - 2) == 'x'
					&& fileName.charAt(fileName.length() - 3) == 't' && fileName
					.charAt(fileName.length() - 4) == '.'))
					|| fileName.length() < 4) {
				JOptionPane.showMessageDialog(null, "Invalid File!");
				return;
			}
			LoadTable load = new LoadTable(selectedFile.getPath());
			load.loadTruthTable();
			load.setFilePath(selectedFile.getPath());
			textField.setText(load.getExpression());
			data = load.getData();
			showButton.doClick();

		}

		private void saveButtonAction() {
			fileDialog = new JFileChooser();
			File selectedFile; // Initially selected file name in the
								// dialog.
			selectedFile = new File("TruthTable" + counter + ".txt");
			fileDialog.setSelectedFile(selectedFile);
			fileDialog.setDialogTitle("Select File to be Saved");
			int option = fileDialog.showSaveDialog(null);

			if (option != JFileChooser.APPROVE_OPTION)
				return; // User canceled or clicked the dialog's close box.
			selectedFile = fileDialog.getSelectedFile();
			if (selectedFile.exists()) { // Ask the user whether to replace
											// the
											// file.
				int response = JOptionPane
						.showConfirmDialog(
								null,
								"The file \""
										+ selectedFile.getName()
										+ "\" already exists.\nDo you want to replace it?",
								"Confirm Save", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (response != JOptionPane.YES_OPTION)
					return; // User does not want to replace the file.
			}

			String nameFile = selectedFile.getPath().toString();
			try {
				SaveTruthTable save = new SaveTruthTable(data,
						convertToArray(parse), nameFile);
				save.save();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		private void showButtonAction() {
			revalidate();
			expression = textField.getText();
			if (isEmptyExpression(expression)) {
				JOptionPane.showMessageDialog(null,
						"Please enter the expression!");
				return;
			}
			parse = Parsing.parseExpression(expression);
			postfix = Parsing.toPostfix(parse);
			if (Parsing.isValid(parse, postfix)) {
				TruthTableConstructor truthTable = new TruthTableConstructor(
						postfix, Parsing.getVariables());
				data = truthTable.getTruthTable();
				System.out.println(parse);
				System.out.println(postfix);
				columnNames = new String[Parsing.getVariables().size() + 1];
				for (int i = 0; i < columnNames.length - 1; i++) {
					columnNames[i] = Parsing.getVariables().get(i);
				}
				columnNames[Parsing.getVariables().size()] = "Output";
				create_Truth_Table();
			} else {
				JOptionPane.showMessageDialog(null, "Not Valid Expression!");
				return;
			}
		}
	}

	/**
	 * This method is used for checking if the user has entered an expression or
	 * not
	 * 
	 * @param expression
	 * @return --false if the text field containing expression.
	 * @return --true if the text field is Empty.
	 */
	public static boolean isEmptyExpression(String expression) {
		if (expression == "") {
			return true;
		}
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) != ' ')
				return false;
		}
		return true;
	}

	/**
	 * This method creates the tables of results in the GUI.
	 */
	private void create_Truth_Table() {
		if (flag) {
			center_Panel.removeAll();
			getContentPane().remove(scrollPane);
		}
		saveButton.setEnabled(true);
		table = new JTable(data, columnNames);
		table.setEnabled(false);
		table.setPreferredScrollableViewportSize(new Dimension(1000, 640));
		scrollPane = new JScrollPane(table);
		center_Panel.add(scrollPane);
		flag = true;
		String[] output = new String[data.length];
		for (int i = 0; i < output.length; i++) {
			output[i] = data[i][data[i].length - 1];
		}
		check_Output_State(output);
		repaint();
		revalidate();
	}

	/**
	 * This method is used to check if the expression is a Tautology or a
	 * contradiction or neither.
	 * 
	 * @param output
	 */
	private void check_Output_State(String[] output) {
		int counter1 = 0;
		int counter2 = 0;
		label2.setText("");
		int length = output.length;
		for (int i = 0; i < output.length; i++) {
			if (output[i].equals("T")) {
				counter1++;
			} else if (output[i].equals("F")) {
				counter2++;
			}
		}
		if (counter1 == length) {
			label2.setText("A Tautology in this expression");
		} else if (counter2 == length) {
			label2.setText("A Contradiction in this expression");
		}
	}

	/**
	 * This method converts arraylist of strings to array of strings.
	 * 
	 * @param list
	 * @return String[]
	 */
	private String[] convertToArray(ArrayList<String> list) {
		int length = list.size();
		String[] array = new String[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}
}
