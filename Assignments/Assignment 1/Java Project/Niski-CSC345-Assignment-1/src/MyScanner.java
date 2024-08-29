/**
 * Brian Niski
 */
import java.io.PushbackReader;
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
    public MyScanner(PushbackReader input) {
        /**
         * Fill reserved words list
         */
        this.RESERVED_WORDS.add("declare");
        this.RESERVED_WORDS.add("int");
        this.RESERVED_WORDS.add("print");
        this.RESERVED_WORDS.add("set");
        this.RESERVED_WORDS.add("if");
        this.RESERVED_WORDS.add("then");
        this.RESERVED_WORDS.add("endif");
        this.RESERVED_WORDS.add("calc");

        /**
         * Initialize INPUT PushbackReader
         */
        this.INPUT = input;
    }

    /**
     * Method to scan one token from the input
     * @return
     * @throws Exception
     */
    public TOKEN scan() throws Exception {
        // Not implemented yet, placeholder return statement
        return TOKEN.INTDATATYPE;
    }

    // This method is not tested yet
    /**
     * Method to get the token buffer string
     * @return
     */
    public String getTokenBufferString() {
        return TOKEN_BUFFER.toString();
    }

}
