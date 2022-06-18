package analogdv;
import java.util.ArrayList;

public abstract class Table {

    private StringBuilder table; // string representation of the table, with its elements
    private ArrayList<String> tableElements; // all the individual (filled) cells of the table
    private int numberOfRows;
    private int maxCellSize;

    // ctor

    // ctor 1
    public Table(ArrayList<String> elements, int rows) {

        this.maxCellSize = this.getMaxCellSize(elements); // lazy fix
        Arraylist<String> formattedElements = this.formatCells(elements, maxCellSize);
        this.table = this.formatTable(formattedElements, this.numberOfRows, this.maxCellSize);
        this.tableElements = elements;
        this.numberOfRows = rows;
    }

    // ctor 2
    public Table() {

        this.tableElements = new ArrayList<String>();
        this.maxCellSize = 0;
        this.numberOfRows = 4;
        this.table = new StringBuilder();
    }

    // internal

    private ArrayList<String> formatCells(ArrayList<String> elements, int cellSize) {

        ArrayList<String> formattedElements = new ArrayList<String>();
        for (int i = 0; i < formattedElements.size(); i++) {

            String element = elements.get(i);
            while (element.length() < cellSize - 1) {

                element = element + " ";
            }
            formattedElements.add(element + "|");
        }

        return formattedElements;
    }

    private int getMaxCellSize(ArrayList<String> elements) {

        int cellSize = 0;
        for (String element : elements) {

            if (element.length() > cellSize) {

                cellSize = element.length();
            }
        }

        return cellSize + 4; // for "| " and " |" at beginning and end
    }

    private StringBuilder formatTable(ArrayList<String> elements, int rows, int cellSize) {

        StringBuilder table = new StringBuilder();
        StringBuilder row = new StringBuilder();
        String bar = getBar(cellSize, rows) + "\n";
        table.append(bar);

        for (int i = 0; i < elements.size(); i++) {

            if ((i + 1) == elements.size()) {

                row.append(elements.get(i));
                bar = getBar(row.toString().length(), 1);
                table.append(row.toString() + "\n");
                table.append(bar);
            }
            else if ((i + 1) % rows == 0) {

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

    
}
