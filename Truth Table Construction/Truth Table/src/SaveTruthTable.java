import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class SaveTruthTable {

	File file;
	String[] col_names;
	String[][] truth_Table;
	FileWriter fw;
	BufferedWriter bf;
	/**
	 * Constructor for the class SaveTruthTable
	 * @param truthTable to be saved
	 * @param columnNames the names of the variables and the expression
	 * @param nameFile the name of the file that the user wants.
	 * @throws IOException
	 */
	public SaveTruthTable(String[][] truthTable, String[] columnNames, String nameFile) throws IOException {
		truth_Table = truthTable;
		col_names = columnNames;
		file = new File(nameFile); 
		fw = new FileWriter(file); 
		bf = new BufferedWriter(fw);
	}
	/**
	 * Save every line in the truth table in the file to be saved.
	 * @throws IOException
	 */
	public void save() throws IOException {
		for (int i = 0; i < col_names.length; i++) {
			bf.write(col_names[i] + " ");
		}
		bf.newLine();
		for (int i = 0; i < truth_Table.length; i++) {
			for (int j = 0; j < truth_Table[i].length; j++) {
				bf.write(truth_Table[i][j] + " ");
			}
			bf.newLine();
		}
		bf.close();
	}
	
	
	
	
	

}
