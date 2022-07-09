package analogdv;
import java.util.ArrayList;

public abstract class Table {

    /* FIELDS */

    private StringBuilder table; // string representation of table, with elements inserted
    private ArrayList<String> tableElements; // list of elements that occupy cells of table
    private int numberOfColumns; // sets maximum number of columns on each row
    private int maxCellSize; // maximum numbers characters in each cell

    /* METHODS - constructors */

    /**
     * First constructor
     *
     * @param elements ready-made list of elements
     * @param columns maximum number of columns on each row, should be greater than 0
     * @throws Exception if columns < 1
     */
    public Table(ArrayList<String> elements, int columns) throws Exception {

        this.formatTable(elements, columns);
    }

    /**
     * Second constructor, no params
     * @throws Exception if something happens while formatting table
     */
    public Table() throws Exception {

        this.createEmptyTable();
        this.tableElements = new ArrayList<String>(); // needed because call above makes another list filled with white-spaces
    }

    /* METHODS - internal */

    /**
     * Gets everything ready for creation of table
     * @param elements ready-made list of elements
     * @param columns maximum number of columns on each row, should be greater than 0
     * @throws Exception if columns < 1
     */
    private void formatTable(ArrayList<String> elements, int columns) throws Exception {

        this.maxCellSize = this.getMaxCellSize(elements);
        ArrayList<String> formattedElements = this.formatCells(elements, maxCellSize);
        this.testColumnValue(columns);
        this.table = this.buildTable(formattedElements, this.numberOfColumns, this.maxCellSize);
        this.tableElements = elements;
    }

    /**
     * Tests if argument for the columns field is viable (greater than 0) or not
     *
     * @param colum maximum number of columns in each row of the table
     * @throws Exception if columns < 1
     */
    private void testColumnValue(int columns) throws Exception {

        if (columns < 1) {

            String errorMessage = "number of columns on each row should be greater than 0";
            throw new Exception(errorMessage);
        }

        this.numberOfColumns = columns;
    }

    /**
     * Creates an empty table string for aesthetics
     */
    private void createEmptyTable() throws Exception {

        ArrayList<String> elementsDummy = new ArrayList<String>();
        int columnsDummy = 4;
        for (int i = 0; i < 8; i++) {

            elementsDummy.add("");
        }
        this.formatTable(elementsDummy, columnsDummy);
    }

    /**
     * Checks the length of each element of a strings list
     *
     * @param elements list of strings
     * @return maximum length of a cell
     */
    private int getMaxCellSize(ArrayList<String> elements) {

        int cellSize = 0;
        for (String element : elements) {

            if (element.length() > cellSize) {

                cellSize = element.length();
            }
        }

        return cellSize + 4; // for "[ " and " ]" at beginning and end
    }

    /**
     * Formats each element of the string list for fit into the table properly
     *
     * @param elements list of string elements
     * @param cellSize max length of a cell in table
     * @return formatted list of string elements
     */
    private ArrayList<String> formatCells(ArrayList<String> elements, int cellSize) {

        ArrayList<String> formattedElements = new ArrayList<String>();
        for (int i = 0; i < formattedElements.size(); i++) {

            String element = elements.get(i) + "[ ";
            while (element.length() < cellSize - 1) {

                element = element + " ";
            }
            formattedElements.add(element + "]");
        }

        return formattedElements;
    }

    /**
     * Builds the string representation of the table, with all elements insterted
     *
     * @param elements formatted list of string elements
     * @param columns maximum number of columns in each row
     * @param cellSize maximum length of a cell
     * @return string representation of table, as a stringbuilder
     */
    private StringBuilder buildTable(ArrayList<String> elements, int columns, int cellSize) {

        StringBuilder table = new StringBuilder();
        StringBuilder row = new StringBuilder();
        String bar = getBar(cellSize, columns) + "\n";
        table.append(bar);

        for (int i = 0; i < elements.size(); i++) {

            if ((i + 1) == elements.size()) {

                row.append(elements.get(i));
                bar = getBar(row.toString().length(), 1);
                table.append(row.toString() + "\n");
                table.append(bar);
            }
            else if ((i + 1) % columns == 0) {

                row.append(elements.get(i));
                table.append(row.toString() + "\n");
                table.append(bar);
                row.setLength(0);
            }
            else {

                 row.append(elements.get(i));
            }
        }

        return table;
    }

    /**
     * Makes bars to seperate each row of the table
     *
     * @param cellSize maximum length of 1 cell
     * @param columns max number of columns on each row
     * @return string representation of a bar
     */
    private String getBar(int cellSize, int columns) {

        StringBuilder b = new StringBuilder();
        int limit = cellSize * columns;

        for (int i = 0; i < limit; i++) {

            b.append("-");
        }

        return b.toString();
    }

    /* METHODS - interface */

    /**
     * Lets user swap entire content of table for new one
     *
     * @param elements list of new strings
     * @throws Exception if something happens while formatting table
     */
    public void setTable(ArrayList<String> elements) throws Exception {

        this.formatTable(elements, this.numberOfColumns);
    }

    /**
     * Lets user change number of columns on each row
     *
     * @param columns new maximum number of columns in a row
     * @throws Exception if columns < 1
     */
    public void setColumns(int columns) throws Exception {

        this.formatTable(this.tableElements, columns);
    }

    /**
     * Lets user add an element to table
     *
     * @param element new element to be insterted to table
     * @throws Exception if something happens while formatting table
     */
    public void add(String element) throws Exception {

        this.tableElements.add(element);
        this.formatTable(this.tableElements, this.numberOfColumns);
    }

    /**
     * Lets user add a list of elements to table
     *
     * @param elements new elements to be insterted to table
     * @throws Exception if something happens while formatting table
     */
    public void add(ArrayList<String> elements) throws Exception {

        this.tableElements.addAll(elements);
        this.formatTable(this.tableElements, this.numberOfColumns);
    }

    /**
     * Clears the entire table, and its contents are gone forever
     * @throws Exception if something happens while formatting table
     */
    public void clear() throws Exception {

        this.createEmptyTable();
        this.tableElements = new ArrayList<String>(); // needed because call above makes another list filled with white-spaces
    }

    /**
     * Checks if the contents of 2 tables are the same or not
     *
     * @param otherTable another instance of a class that extends Table
     * @return true if contents are the same, false otherwise
     */
    public boolean equals(Table otherTable) {

        boolean result = this.tableElements.equals(otherTable.tableElements);
        return result;
     }

    /**
     * Extracts contents of another table and adds it to this table, iff the tables don't have same content
     *
     * @param otherTable another instance of a class that extendsTable
     * @throws Exception if something happens while formatting table
     */
    public void merge(Table otherTable) throws Exception {

        this.add(otherTable.tableElements);
    }

    // !! TEST THIS METHOD !! //
    /**
     * Removes an element with specified index from the table
     *
     * @param column the column where the element is
     * @param row the row where the element is
     * @throws Exception if something happens while formatting table
     */
    public void remove(int column, int row) throws Exception {

        int cellNumber = row * column - 1;
        this.tableElements.remove(cellNumber);
        this.formatTable(this.tableElements, this.numberOfColumns);
     }

     /**
      * Fetches the string representation of the table
      * @return string representation of table, with elements if table is not empty
      */
    public String getTable() {

        return this.table.toString();
    }

    /**
     * Fetches size of the table, i.e number of elements the table contains
     * @return number of elements
     */
    public int size() {

        return this.tableElements.size();
    }

    /**
     * Used in classes which inherit from this one, checks if a line (from a file) is equal to the bar
     * @param line line in the file
     * @return true if line is the bar, false otherwise
     */
    public boolean isBar(String line) {

        String bar = this.getBar(this.maxCellSize, this.numberOfColumns);
        return bar.equals(line);
    }
}
