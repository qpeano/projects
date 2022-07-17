package analogdv;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;
import java.io.*;

public class StatisticalTable extends Table {

    /* FIELDS */

    private File file; // file that houses the table, can also be seen as a file-rep of the table
    private ArrayList<Double> dataPoints; // needed for calculations

    /* METHODS - constructors */

    /**
     * First constructor
     *
     * @param path where the file is
     * @param columns max number of columns in a row
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     * @throws Exception if something happens while formatting table
     */
    public StatisticalTable(String path, int columns) throws IOException, Exception {

        super(columns);
        this.file = new File(path);
        this.file.createNewFile(); // if file does not exist prior to execution
        this.hasContent(this.file);
        this.printTable(this.file); // prints out empty table, or table with the elements in the file
        this.dataPoints = new ArrayList<>();
    }

    /* METHODS - internal */

    /**
     * Adds element to numerical form to the dataPoint list
     *
     * @param dataList list of numerical objects (doubles)
     * @param element new string element
     * @throws Exception if element can't be converted to double
     */
    private void addDataPoint(ArrayList<Double> dataList, String element) throws Exception {

        double value = Double.parseDouble(element);
        dataList.add(value);
    }

    /**
     * Adds multiple elements to numerical form to the dataPoint list
     *
     * @param dataList list of numerical objects (doubles)
     * @param elements new list of string elements
     * @throws Exception if any element can't be converted to double
     */
    private void addDataPoint(ArrayList<Double> dataList, ArrayList<String> elements) throws Exception {

        for (String element : elements) {

            this.addDataPoint(dataList, element);
        }
    }

    /**
     * also Adds multiple elements to numerical form to the dataPoint list, but it is used while reading file
     *
     * @param dataList list of numerical objects (doubles)
     * @param elements new list of string elements
     * @throws Exception if any element can't be converted to double
     */
     private void addDataPoint(ArrayList<Double> dataList, ArrayList<String> elements, int lineNumber) throws IOException {

        int cellNumber = 0;
        try {

            for (String element : elements) {

                cellNumber++;
                this.addDataPoint(dataList, element);
            }
        }
        catch(Exception e) {

            String message = "CANNOT CONVERT ELEMENT IN CELL" + cellNumber + " TO DOUBLE";
            String errorMessage = this.generateErrorMessage(message, lineNumber, this.file.toString());
            throw new IOException(errorMessage);
        }
     }

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
        Pattern barFormat = Pattern.compile("\\-+");
        Matcher lineMatcher;
        Matcher barMatcher;
        int lineCounter = 0;

        while ((line = br.readLine()) != null) {

            lineCounter++;

            if (lineCounter % 2 != 0) {

                barMatcher = barFormat.matcher(line);

                if (!barMatcher.matches()) {

                   String errorMessage = this.generateErrorMessage("LINE SHOULD BE A DIVIDING BAR", lineCounter, this.file.toString());
                   throw new IOException(errorMessage);
                }
            }
            else {

                lineMatcher = lineFormat.matcher(line);

                if (lineMatcher.matches()) {

                    ArrayList<String> elements = this.splitByElement(line);
                    this.addDataPoint(this.dataPoints, elements, lineCounter); // adds to numerical list for calculations
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
        errorMessage.append("\nLine: " + line);
        errorMessage.append("\nFile: " + file);
        errorMessage.append("\nMessage: " + message);

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

        String table = this.toString();
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

        this.addDataPoint(this.dataPoints, element);
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

        this.addDataPoint(this.dataPoints, elements);
        super.add(elements);
        this.printTable(this.file);
    }

    /**
     * Removes an element with specified index from the table
     *
     * @param cellNumber is where in the table the element is
     * @throws Exception if something happens while formatting table
     * @throws IOException if something happens while writing to file
     */
    public void remove(int cellNumber) throws IOException, Exception {

        super.remove(cellNumber);
        this.dataPoints.remove(cellNumber);
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

    /**
     * Fetches the string representation of the table
     *
     * @return string rep. of table
     */
    public String toString() {

        return super.getTable();
    }
}
