package analogdv;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;
import java.io.*;

public class VariableLengthTable extends Table {

    /* FIELDS */

    private File file; // file that houses the table, can also be seen as a file-rep of the table

    /* METHODS - constructors */

    /**
     * First constructor, use if newest elements should be on top of older elements
     *
     * @param elements ready-made list of elements
     * @param columns maximum number of columns on each row, should be greater than 0
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     * @throws Exception if columns < 1
     */
    public VariableLengthTable(String path, ArrayList<String> elements, int columns) throws Exception, Exception {

        super(elements, columns);
        this.file = new File(path);
        this.file.createNewFile(); // if file does not exist prior to execution
        this.hasContent(this.file);
        this.printTable(this.file); // prints out old elements and new elements, newest first
    }

    /**
     * Second constructor, no params
     *
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     * @throws Exception if something happens while formatting table
     */
    public VariableLengthTable(String path) throws Exception, Exception {

        super();
        this.file = new File(path);
        this.file.createNewFile(); // if file does not exist prior to execution
        this.hasContent(this.file);
    }

    /* METHODS - internal */

    /**
     * Reads file and extracts all elements, if there are any and if it is properly formatted
     *
     * @param f a text-file which represents a table
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     */
    private void hasContent(File f) throws IOException, Exception {

        String line;
        boolean result = false;
        BufferedReader br = new BufferedReader(new FileReader(f));

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

             result = true;
        }

        br.close();

        if (result) {

            this.extract(f);
        }
    }

    /**
     * Extracts all the elements of the table
     *
     * @param f a text-file which represents a table
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     */
    private void extract(File f) throws IOException, Exception {

        String line;
        BufferedReader br = new BufferedReader(new FileReader(f));
        Pattern lineFormat = Pattern.compile("(\\[\\s\\w+\\s+\\])+");
        Matcher formatMatcher;
        int lineCounter = 0;

        while ((line = br.readLine()) != null) {

            lineCounter++;
            formatMatcher = lineFormat.matcher(line);

            if (lineCounter % 2 != 0) {

                if (!super.isBar(line)) {

                   String errorMessage = this.generateErrorMessage("LINE SHOULD BE A DIVIDING BAR", lineCounter, this.file.toString());
                   throw new IOException(errorMessage);
                }
            }
            else {

                if (formatMatcher.matches()) {

                    ArrayList<String> elements = this.splitByElement(line);
                    super.add(elements);
                }
                else {

                    String errorMessage = this.generateErrorMessage("FORMATTING ERROR", lineCounter, this.file.toString());
                    throw new IOException(errorMessage);
                }
            }
        }

        br.close();
    }

    /**
     * Generates error messages
     *
     * @param message is what implementer wants to convey to user
     * @param line is where in the text-file it was found
     * @param file is the path to the file the error was found in
     * @return the full error message
     */
    private String generateErrorMessage(String message, int line, String file) {

        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Line: " + line + "\n");
        errorMessage.append("File: " + file + "\n");
        errorMessage.append("Message: " + message);

        return errorMessage.toString();
    }

    /**
     * Splits string read from file into list of elements to be added to table
     *
     * @param line line of text
     * @return list of elements extracted from file
     */
    private ArrayList<String> splitByElement(String line) {

        ArrayList<String> elementList = new ArrayList<String>();
        String line2 = line.substring(2, line.length() - 1).replaceAll("\\s+", ""); // removes 1st and last bracket and whitespaces
        String[] elementArray = line2.split("\\]\\["); // removes all brackets that are left
        Collections.addAll(elementList, elementArray);

        return elementList;
    }

    /**
     * Prints table out to file
     *
     * @param f text-file that table is going to get printed to
     * @throws IOException if something happens while reading from file
     */
    private void printTable(File f) throws IOException {

        String table = super.getTable();
        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // writing mechanism to file
        bw.write(table);
        bw.close();
    }

    /* METHODS - interface */

    /**
     * Lets user add an element to table
     *
     * @param element new element to be insterted to table
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while writing to file
     */
    public void add(String element) throws IOException, Exception {

        super.add(element);
        this.printTable(this.file);
    }

    /**
     * Lets user add a list of elements to table
     *
     * @param elements new elements to be insterted to table
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while writing to file
     */
    public void add(ArrayList<String> elements) throws IOException, Exception {

        super.add(elements);
        this.printTable(this.file);
    }

    /**
     * Removes an element with specified index from the table
     *
     * @param column the column where the element is
     * @param row the row where the element is
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while writing to file
     */
    public void remove(int column, int row) throws IOException, Exception {

        super.remove(column, row);
        this.printTable(this.file);
     }

    /**
     * Lets user change number of columns on each row
     *
     * @param columns new maximum number of columns in a row
     * @throws Exception if columns < 1
     * @throws IOException if something happens while writing to file
     */
    public void setColumns(int columns) throws IOException, Exception {

        super.setColumns(columns);
        this.printTable(this.file);
    }

    /**
     * Checks if a table has the exact same content as this
     *
     * @param otherTable another instance of a class that extends Table
     * @return true if contents are the same, false otherwise
     */
    public boolean equals(Table otherTable) {

        return super.equals(otherTable);
     }

    /**
     * Extracts contents of another table and adds it to this table, iff the tables don't have same content
     *
     * @param otherTable another instance of a class that extendsTable
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while writing to file
     */
    public void merge(Table otherTable) throws IOException, Exception {

        super.merge(otherTable);
        this.printTable(this.file);
    }

    /**
     * Clears the entire table, and its contents are gone forever
     *
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while writing to file
     */
    public void clear() throws IOException, Exception {

        super.clear();
        this.printTable(this.file);
    }

    /* METHODS - other */

    /**
     * Fetches size of the table, i.e number of elements the table contains
     *
     * @return number of elements
     */
    public int size() {

        return super.size();
    }

    /**
     * Fetches the string representation of the table
     *
     * @return string rep. of table
     */
    public String toString() {

        return super.getTable();
    }
}
