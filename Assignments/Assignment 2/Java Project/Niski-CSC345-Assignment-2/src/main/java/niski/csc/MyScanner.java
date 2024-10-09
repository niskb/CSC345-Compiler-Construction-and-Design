/**
 * Brian Niski
 */

package niski.csc;

import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

public class MyScanner {

    /**
     * Tokens
     */
    public enum TOKEN {
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
        int c;
        c = INPUT.read();
        while (c != -1) {
            if (Character.isWhitespace(c)) {
                c = INPUT.read();
                continue;
            } else if (Character.isDigit(c)) {
                c = INPUT.read();
                while (Character.isDigit(c)) {
                    c = INPUT.read();
                }
                TOKEN_BUFFER.append(TOKEN.INTLITERAL + "\n");
                return TOKEN.INTLITERAL;
            } else if (c == '+') {
                TOKEN_BUFFER.append(TOKEN.PLUS + "\n");
                return TOKEN.PLUS;
            } else if (c == '=') {
              TOKEN_BUFFER.append(TOKEN.EQUALS + "\n");
              return TOKEN.EQUALS;
            } else if (Character.isLetter(c)) {
                StringBuilder buffer = new StringBuilder().append((char) c);
                while (Character.isLetter(c) || Character.isDigit(c) {
                    c = INPUT.read();
                    if (Character.isLetter(c) || Character.isDigit(c) {
                        buffer.append((char) c);
                    } else {
                        break;
                    }
                }
                // Check if buffer matches a reserved word
                for (TOKEN match : TOKEN.values()) {
                    if (buffer.toString().equals(String.valueOf(match).toLowerCase())) {
                        TOKEN_BUFFER.append(match + "\n");
                        return match;
                    }
                }
                // Check the last token to figure out what token to return for this
                String lastToken = TOKEN_BUFFER.toString().substring(0, TOKEN_BUFFER.toString().lastIndexOf('\n'));
                lastToken = lastToken.substring(lastToken.lastIndexOf('\n') + 1, lastToken.length());
                // The only time we return a DATATYPE token is when the last token was ID
                if (lastToken.equals(String.valueOf(TOKEN.ID))) {
                    TOKEN_BUFFER.append(TOKEN.INTDATATYPE + "\n");
                    return TOKEN.INTDATATYPE;
                } else {
                    TOKEN_BUFFER.append(TOKEN.ID + "\n");
                    return TOKEN.ID;
                }
            }
        }
        TOKEN_BUFFER.append(TOKEN.SCANEOF);
        return TOKEN.SCANEOF;
    }

    /**
     * Method to get the token buffer string
     *
     * @return
     */
    public String getTokenBufferString() {
        return TOKEN_BUFFER.toString();
    }

}
