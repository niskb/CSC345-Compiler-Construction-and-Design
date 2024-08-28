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
     * INPUT PushbackReader
     */
    private PushbackReader INPUT;

    /**
     * TOKEN BUFFER StringBuilder
     */
    private StringBuilder TOKEN_BUFFER = new StringBuilder();

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
        INPUT = new PushbackReader(input);
    }

}
