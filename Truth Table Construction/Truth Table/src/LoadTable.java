import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadTable {
	private String expression;
	private String data[][];
	private static String filePath;

	/**
	 * Getter of the expression that has been loaded from the file.
	 * @return
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Getter of data array that has been loaded from the file.
	 * @return
	 */
	public String[][] getData() {
		return data;
	}

	/**
	 * Setter to set the file path that the user want to load.
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		LoadTable.filePath = filePath;
	}

	/**
	 * Constructor with no parameters of LoadTable.
	 */
	public LoadTable() {
		expression = "";
		filePath = "";

	}

	/**
	 * Constructor with one parameter of LoadTable.
	 */
	public LoadTable(String path) {
		expression = "";
		filePath = path;
	}

	/**
	 * This method convert the array list of data to String[][] data.
	 */
	public void loadTruthTable() {
		ArrayList<String> list = loadFile();
		if (list == null)
			return;
		expression = list.get(0);
		int length = list.get(1).length();
		data = new String[list.size() - 1][length];
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < length; j++) {
				data[i][j] = list.get(i).charAt(j) + "";
			}
		}
	}

	/**
	 * This method loads a file that containing an expression and its truth table.
	 * @return --Array list of the data in the file.
	 */
	private static ArrayList<String> loadFile() {
		try {
			File file = new File(filePath);
			Scanner scn = new Scanner(file);
			ArrayList<String> list = new ArrayList<String>();
			int x = 0;
			while (scn.hasNextLine()) {
				list.add(scn.nextLine());
			}
			scn.close();
			return list;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

}
