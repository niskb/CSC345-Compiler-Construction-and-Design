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
     *
     * @return
     * @throws Exception
     */
    public TOKEN scan() throws Exception {
        int read = INPUT.read();
        // we can use a while loop and exit when -1

        if (read != -1) { // return token if match found
            char readChar = (char) read;
            String readToken = "" + readChar;
            // if statement needed for first char in token is a digit otherwise not INTLITERAL


            // need to check next characters after then build token
            // if read matches token, stop and return that token
        } else { // no character has been read or empty program
            INPUT.close();
        }
        return TOKEN.SCANEOF;
    }

    // This method is not tested yet

    /**
     * Method to get the token buffer string
     *
     * @return
     */
    public String getTokenBufferString() {
        return TOKEN_BUFFER.toString();
    }

}