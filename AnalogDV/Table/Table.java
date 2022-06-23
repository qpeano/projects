package analogdv;
import java.util.ArrayList;

public abstract class Table {

    private StringBuilder table; // string representation of the table, with its elements
    private ArrayList<String> tableElements; // all the individual (filled) cells of the table
    private int numberOfColumns; // maximum number of columns on each row
    private int maxCellSize;

    /* CONSTRUCTORS */

    // ctor 1, takes a list of strings, and number of columns in table
    public Table(ArrayList<String> elements, int columns) throws Exception {

        this.formatTable(elements, columns);
    }

    // ctor 2, no parameters
    public Table() {

        this.createEmptyTable();
        this.tableElements = new ArrayList<String>();
    }

    /* INTERNAL */

    // method gets everything ready for creation of table
    private void formatTable(ArrayList<String> elements, int columns) throws Exception {

        this.maxCellSize = this.getMaxCellSize(elements);
        Arraylist<String> formattedElements = this.formatCells(elements, maxCellSize);
        this.numberOfColumns = this.testColumnValue(columns);
        this.table = this.buildTable(formattedElements, this.numberOfColumns, this.maxCellSize);
        this.tableElements = elements;
    }

    // tests if argument for the column field is viable (greater than 0) or not
    private void testColumValue(int columns) throws Exception {

        if (columns < 1) {

            String errorMessage = "number of columns on each row should be greater than 0";
            throw
        }

        this.numberOfColumns = columns;
    }

    // creates an empty table for aesthetics, if user instantiates object with ctor 2
    public void createEmptyTable() throws Exception {

        ArrayList<String> elementsDummy = new ArrayList<String>();
        int columnsDummy = 4;
        for (int i = 0; i < 8; i++) {

            elementsDummy.add("");
        }
        this.formatTable(elementsDummy, columnsDummy);
    }

    // method checks length of each string in list, and returns highest number
    private int getMaxCellSize(ArrayList<String> elements) {

        int cellSize = 0;
        for (String element : elements) {

            if (element.length() > cellSize) {

                cellSize = element.length();
            }
        }

        return cellSize + 4; // for "[ " and " ]" at beginning and end
    }

    // method formats each string of the list in a specific way to fit into the table
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

    // method builds and returns stringbuilder representation of the table, with elements
    private StringBuilder createTable(ArrayList<String> elements, int columns, int cellSize) {

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

    /* INTERFACE */

    // method lets user swap entire content of table for new one
    public void setTable(ArrayList<String> elements) {

        this.formatTable(elements, this.numberOfRows);
    }

    // method lets user change number of columns on each row
    public void setColumns(int columns) throws Exception {

        this.formatTable(this.tableElements, columns);
    }

    // method lets user add elements to table
    public void add(String element) {

        this.tableElements.add(element);
        this.formatTable(this.tableElements, this.numberOfColumns);
    }

    // method lets user clear table
    public void clear() {

        this.createEmptyTable();
        this.tableElements = new ArrayList<String>();
    }

    // method lets user merge two table together into one
    public void merge(Table otherTable) {

        this.tableElements.addAll(otherTable.tableElements);
        this.formatTable(this.tableElements, this.numberOfColumns);
    }

    public void remove(int cellNumber) throws Exception {

        
    }
}
