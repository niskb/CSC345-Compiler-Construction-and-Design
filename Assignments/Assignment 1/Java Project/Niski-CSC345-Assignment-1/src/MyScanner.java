import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MyScanner {

    /**
     * Tokens
     */
    private enum TOKEN {
        SCANEOF, ID, INTLITERAL, INTDATATYPE, DECLARE, PRINT, SET, EQUALS, IF, THEN, ENDIF, CALC, PLUS
    }

    /**
     * Reserved Words
     */
    private List<String> RESERVED_WORDS = new ArrayList<String>();

    /**
     * Input PushbackReader
     */
    private PushbackReader INPUT;

    /**
     * Constructor for MyScanner
     */
    public MyScanner(Reader input) {
        /**
         * Fill reserved words list
         */
        RESERVED_WORDS.add("declare");
        RESERVED_WORDS.add("int");
        RESERVED_WORDS.add("print");
        RESERVED_WORDS.add("set");
        RESERVED_WORDS.add("if");
        RESERVED_WORDS.add("then");
        RESERVED_WORDS.add("endif");
        RESERVED_WORDS.add("calc");

        /**
         * Initialize INPUT PushbackReader
         */
        input = new PushbackReader(input);
    }

}
