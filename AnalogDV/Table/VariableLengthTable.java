package analogdv;
import java.util.ArrayList;
import java.io.*;

public class VariableLengthTable extends Table {

    /* FIELDS */

    private File file; // file that houses the table

    /* METHODS - constructors */

    /**
     * First constructor
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
    }

    /**
     * Second constructor, no params
     *
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     * @throws Exception if something happens while formatting table
     */
    public VariableLengthTable(String path) throws Exception, Exception {

        super();
        this.file.createNewFile(); // if file does not exist prior to execution
        this.file = new File(path);
        this.hasContent(this.file);
    }

    /* METHODS - internal */

    /**
     * Reads file and extracts all elements, if there are any and if it is properly formatted
     *
     * @param f a text-file
     * @throws IOException if something happens while reading from file or if file is not formatted properly
     */
    private boolean hasContent(File f) throws IOException {


    }

}
