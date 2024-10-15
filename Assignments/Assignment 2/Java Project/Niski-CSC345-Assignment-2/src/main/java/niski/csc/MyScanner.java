/**
 * Brian Niski
 */

package niski.csc;

import java.io.IOException;
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
     * The last lexeme, so the parse reader can read it when declaring, setting, and reading IDs/Int Literals
     */
    private String lastLexeme = "";

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
        int c = readNextChar();
        String lexeme = "";
        while (c != -1) {
            if (isWhiteSpace(c)) {
                c = readNextChar();
                continue;
            }
            // The last character that was read is the start of a lexeme
            lexeme += (char) c;
            // These lexemes only have one character in them:
            if (c == '+') {
                lastLexeme = lexeme;
                TOKEN_BUFFER.append("+ ");
                return TOKEN.PLUS;
            } else if (c == '=') {
                lastLexeme = lexeme;
                TOKEN_BUFFER.append("= ");
                return TOKEN.EQUALS;
            }
            // If the first character of a lexeme is an integer, the whole lexeme must be an integer
            else if (lexeme.matches("[0-9]")) {
                // Loop until it's not an integer
                while (Character.isDigit(c)) {
                    c = readNextChar();
                    if (Character.isDigit(c)) {
                        lexeme += (char) c;
                    } else {
                        break;
                    }
                } // end of while
                lastLexeme = lexeme;
                TOKEN_BUFFER.append(TOKEN.INTLITERAL + " ");
                return TOKEN.INTLITERAL;
            }
            // If the first character of a lexeme is a letter, then it may be a reserved word
            // An ID may contain both letters and integers, but the lexeme must start with a letter
            else if (lexeme.matches("[a-z|A-Z]")) {
                while (Character.isLetter(c) || Character.isDigit(c)) {
                    c = readNextChar();
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        lexeme += (char) c;
                    } else {
                        break;
                    }
                } // end of while
                lastLexeme = lexeme;
                // Check if the lexeme matches a reserved word, otherwise it's an ID
                // "int" is a reserved word, but the token value is INTDATATYPE, so we must include an if statement
                if (lexeme.equals("int")) {
                    TOKEN_BUFFER.append(TOKEN.INTDATATYPE + " ");
                    return TOKEN.INTDATATYPE;
                } else {
                    // Use a loop to match the lexeme with the token
                    for (TOKEN match : TOKEN.values()) {
                        if (String.valueOf(match).toLowerCase().equals(lexeme.toLowerCase())) {
                            TOKEN_BUFFER.append(match + " ");
                            return match;
                        }
                    }
                    // No matches, must be an ID
                    TOKEN_BUFFER.append(TOKEN.ID + " ");
                    return TOKEN.ID;
                }
            }
        } // end of while
        TOKEN_BUFFER.append(TOKEN.SCANEOF);
        return TOKEN.SCANEOF;
    }

    /**
     * Reads the next character in the PushbackReader and returns it as an int
     * @return
     */
    private int readNextChar() {
        try {
            return INPUT.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * If the character read is a whitespace return true, otherwise return false
     * @param c
     * @return
     */
    private boolean isWhiteSpace(int c) {
        if ((c == 32) || (c == 9) || (c == 10) || (c == 13)) {
            return true;
        }
        return false;
    }

    /**
     * Method to get the token buffer string
     *
     * @return
     */
    public String getTokenBufferString() {
        return TOKEN_BUFFER.toString();
    }

    /**
     * Helper Method to get the last lexeme for the parser symbol table
     */
    public String getLastLexeme() {
        return lastLexeme;
    }

}