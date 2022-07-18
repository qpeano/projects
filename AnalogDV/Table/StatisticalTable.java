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

    /* INNER CLASS */

    private class Point { // represents a point in the cartetsian plane, used for linear regression

        double xValue;
        double yValue;

        public Point(double x, double y) {

            this.xValue = x;
            this.yValue = y;
        }

        public double getX() {

            return this.xValue;
        }

        public double getY() {

            return this.yValue;
        }
    }

    /* METHODS - constructors */

    /**
     * Constructor
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
    private void addDataPoints(ArrayList<Double> dataList, ArrayList<String> elements) throws Exception {

        for (String element : elements) {

            this.addDataPoint(dataList, element);
        }
    }

    /**
     * also Adds multiple elements to numerical form to the dataPoint list, but it is used while reading file
     *
     * @param dataList list of numerical objects (doubles)
     * @param elements new list of string elements
     * @throws IOException if any element read from file can't be converted to double
     */
     private void addDataPoints(ArrayList<Double> dataList, ArrayList<String> elements, int lineNumber) throws IOException {

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
                    this.addDataPoints(this.dataPoints, elements, lineCounter); // adds to numerical list for calculations
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

    /**
     * Extracts elements of 2 given columns
     *
     * @param dataList the list representation of table
     * @param yColumn the column with dependant values
     * @param xColumn the column with independent values
     * @param columnLimit the max number of columns in a row
     * @throws IOException if columns does not have same amount of values
     * @throws Exception if any column numbers given is greater than the max number of column for a row, or if numbers are negative
     * @return list of points representations of the values
     */
    private ArrayList<Point> getPoints(ArrayList<Double> dataList, int xColumn, int yColumn, int columnLimit) throws IOException, Exception {

        if ((yColumn > columnLimit) || (xColumn > columnLimit)) {

            throw new Exception("COLUMN NUMBERS SHOULD NOT BE GREATER THAN MAX NUMBER OF COLUMNS ON EACH ROW OF THE TABLE");
        }
        else if (yColumn < 1 || xColumn < 1) {

            throw new Exception("COLUMN NUMBERS SHOÚLD NOT BE LESS THAN 1");
        }

        ArrayList<Point> points = new ArrayList<>();

        int indexOfX = xColumn - 1;
        int indexxOfy = yColumn - 1;
        while ((indexOfY < dataList.size()) && (indexOfX < dataList.size())) {

            double xValue = dataList.get(indexOfX);
            double yValue = dataList.get(indexOfY);
            points.add(new Point(xValue, yValue));
            indexOfX += columnLimit;
            indexOfY += columnLimit;
        }

        if (points.size() == 0) {

            throw new IOException("CHECK FILE, COLUMNS MIGHT NOT HAVE EQUAL AMOUNT OF VALUES, OR MIGHT EVEN BE EMPTY");
        }

        return points; 
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

        this.addDataPoints(this.dataPoints, elements);
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

        this.dataPoints.clear();
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

    /**
     * Fetches size of the table, i.e number of elements the table contains
     *
     * @return number of elements
     */
    public int size() {

        return super.size();
    }

    /**
     * Calculates the mean value of the entire table if table isn't empty
     *
     * @return mean value
     * @throws Exception if user wants mean of empty table
     */
    public double getMeanValue() throws Exception {

        if (this.size() == 0) {

            throw new Exception("CANNOT PERFORM OPERATION ON EMPTY SET");
        }

        double tableMean = 0;
        double sumOfElements = 0;
        for (Double value : this.dataPoints) {

            sumOfElements += value.doubleValue();
        }

        double numberOfElements = this.dataPoints.size();
        tableMean = sumOfElements / numberOfElements;
        return tableMean;
    }

    /**
     * Calculates the tableMedian value of the entire table if table isn't empty
     *
     * @return tableMedian value
     * @throws Exception if user wants tableMedian of empty table
     */
    public double getMedianValue() throws Exception {

        if (this.size() == 0) {

            throw new Exception("CANNOT PERFORM OPERATION ON EMPTY SET");
        }

        double tableMedian = 0;
        ArrayList<Double> dataPointsCopy = new ArrayList<Double>(this.dataPoints);
        Collections.sort(dataPointsCopy);
        int numberOfElements = dataPointsCopy.size();

        if (numberOfElements % 2 == 0) {

            double SumOfMiddleElements = dataPointsCopy.get(numberOfElements / 2) + dataPointsCopy.get((numberOfElements / 2) - 1);
            tableMedian = SumOfMiddleElements / 2.0;
        }
        else {

            tableMedian = dataPointsCopy.get(numberOfElements / 2);
        }

        return tableMedian;
    }

    /**
     * Calculates the mode of the entire table if it isn't empty
     *
     * @return first found mode if it exsists, otherwise the first element
     * @throws Exception if user wants mode of empty table
     */
    public double getModeValue() throws Exception {

        if (this.size() == 0) {

            throw new Exception("CANNOT PERFORM OPERATION ON EMPTY SET");
        }

        double tableMode = Double.NaN; // for the case where there is no mode value
        int maxOccurrence = 0;
        for (int i = 0; i < this.dataPoints.size(); i++) {

            int count = 0;
            for (int j = 0; j < this.dataPoints.size(); j++) {

                if (this.dataPoints.get(j) == this.dataPoints.get(i)) {

                    count++;
                }
            }

            if (count > maxOccurrence) {

                maxOccurrence = count;
                tableMode = this.dataPoints.get(i);
            }
        }

        return tableMode;
    }

    /**
     * Calculates the standard deviation of the entire table, if it isn't empty
     *
     * @param setting determines if it is a population or sample data that is being calculated
     * @return standard deviation of table
     * @throws Exception if user wants standard deviation of empty table
     */
    public double getStandardDeviation(int setting) throws Exception {

        if (this.size() == 0) {

            throw new Exception("CANNOT PERFORM OPERATION ON EMPTY SET");
        }

        double variance = 0;
        double meanValue = this.getMeanValue();
        double numberOfElements = this.dataPoints.size();
        double standDev = 0;

        for (Double dataPoint : this.dataPoints) {

            double value = dataPoint.doubleValue();
            variance += Math.pow((value - meanValue), 2);
        }

        if (setting == 0) {

            standDev = Math.sqrt(variance / (numberOfElements - 1)); // sampe data
        }
        else if (setting == 1) {

            standDev = Math.sqrt(variance / numberOfElements); // whole population
        }
        else {

            throw new Exception("CHOOSE BETWEEN\n0: SAMPLE SD\n1: POPULATION SD\n NO OTHER VALUE");
        }

        return standDev;
     }

    /**
     * Calculates the best fit line to 2 columns of the table, where one is the independet variable, and the other one the dependent
     *
     * @param xColumn the column of independent values
     * @param yColumn the column of dependent values
     * @return array with 2 elements a, b to the equation Y = a + b * X
     * @throws IOException if columns doesn't have same number of values
     * @throws Exception if any column numbers given is greater than the max number of column for a row
     */
    public double[] linearRegression(int xColumn, int yColumn) throws IOException, Exception {

        ArrayList<Point> points = this.getPoints(this.dataPoints, xColumn, yColumn, super.getColumns());
        double numberOfElements = points.size();
        double sumOfY = 0;
        double sumOfX = 0;

        double[] constants = new double[2];
        for (Point<Double> p : points) {

            sumOfX += p.getX();
            sumOfY += p.getY();
        }

        double averageOfX = sumOfX / numberOfElements;
        double averageOfY = sumOfY / numberOfElements;

        double numerator = 0;
        double denominator = 0;
        for (Point<Double> p : points) {

            numerator += (p.getX() - averageOfX) * (p.getY() - averageOfY);
            denominator += Math.pow((p.getX() - averageOfX), 2);
        }

        double b = numerator / denominator;
        double a = averageOfY - b * averageOfX;

        constants[0] = a;
        constants[1] = b;
        return constants;
    }
}
