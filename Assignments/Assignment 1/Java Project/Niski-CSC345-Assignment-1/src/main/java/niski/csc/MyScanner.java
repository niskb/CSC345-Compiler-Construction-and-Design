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
        int readChar = INPUT.read();
        // Read until a whitespace is hit or end of file
        String buffer = "";
        while (readChar != -1) {
            if (Character.isWhitespace(readChar)) {
                break;
            } else {
                buffer = buffer + ((char) readChar);
                readChar = INPUT.read();
            }
        }
        // Expect a reserved word
        if (TOKEN_BUFFER.toString().isEmpty()) { // The first reserved word
            // Declare
            switch (buffer) {
                case "declare" -> {
                    TOKEN_BUFFER.append(TOKEN.DECLARE);
                    return TOKEN.DECLARE;
                }
                // Set
                case "set" -> {
                    TOKEN_BUFFER.append(TOKEN.SET);
                    return TOKEN.SET;
                }
                // Calc
                case "calc" -> {
                    TOKEN_BUFFER.append(TOKEN.CALC);
                    return TOKEN.CALC;
                }
                // Print
                case "print" -> {
                    TOKEN_BUFFER.append(TOKEN.PRINT);
                    return TOKEN.PRINT;
                }
                // If
                case "if" -> {
                    TOKEN_BUFFER.append(TOKEN.IF);
                    return TOKEN.IF;
                }
                // Empty program
                case "" -> {
                    TOKEN_BUFFER.append(TOKEN.SCANEOF);
                    return TOKEN.SCANEOF;
                }
            }
        } else {
            // The second token to expect
            // Declare
            if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.DECLARE))) { // Expect an ID
                TOKEN_BUFFER.append(TOKEN.ID);
                return TOKEN.ID;
                // Set
            } else if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.SET))) { // Expect an ID
                TOKEN_BUFFER.append(TOKEN.ID);
                return TOKEN.ID;
                // Calc
            } else if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.CALC))) { // Expect an ID or Int Literal
                try { // Int Literal
                    int test = Integer.parseInt(buffer);
                    TOKEN_BUFFER.append(TOKEN.INTLITERAL);
                    return TOKEN.INTLITERAL;
                } catch (Exception e) { // ID
                    TOKEN_BUFFER.append(TOKEN.ID);
                    return TOKEN.ID;
                }
                // Print
            } else if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.PRINT))) { // Expect an ID or Int Literal
                try { // Int Literal
                    int test = Integer.parseInt(buffer);
                    TOKEN_BUFFER.append(TOKEN.INTLITERAL);
                    return TOKEN.INTLITERAL;
                } catch (Exception e) { // ID
                    TOKEN_BUFFER.append(TOKEN.ID);
                    return TOKEN.ID;
                }
                // If
            } else if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF))) { // Expect an ID or Int Literal
                try { // Int Literal
                    int test = Integer.parseInt(buffer);
                    TOKEN_BUFFER.append(TOKEN.INTLITERAL);
                    return TOKEN.INTLITERAL;
                } catch (Exception e) { // ID
                    TOKEN_BUFFER.append(TOKEN.ID);
                    return TOKEN.ID;
                }
            }
            // The third token to expect
            // Declare
            if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.DECLARE) + String.valueOf(TOKEN.ID))) { // Expect an Int Datatype
                TOKEN_BUFFER.append(TOKEN.INTDATATYPE);
                return TOKEN.INTDATATYPE;
                // Set
            } else if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.SET) + String.valueOf(TOKEN.ID))) { // Expect an Equal Sign
                TOKEN_BUFFER.append(TOKEN.EQUALS);
                return TOKEN.EQUALS;
                // Calc
            } else if ((TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.CALC) + String.valueOf(TOKEN.INTLITERAL))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.CALC) + String.valueOf(TOKEN.ID)))) { // Expect a Plus Sign
                TOKEN_BUFFER.append(TOKEN.PLUS);
                return TOKEN.PLUS;
                // If
            } else if ((TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.INTLITERAL))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.ID)))) {
                TOKEN_BUFFER.append(TOKEN.EQUALS);
                return TOKEN.EQUALS;
            }
            // The fourth token to expect
            // Set
            if (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.SET) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.EQUALS))) { // Expect an ID or Int Literal
                try { // Int Literal
                    int test = Integer.parseInt(buffer);
                    TOKEN_BUFFER.append(TOKEN.INTLITERAL);
                    return TOKEN.INTLITERAL;
                } catch (Exception e) { // ID
                    TOKEN_BUFFER.append(TOKEN.ID);
                    return TOKEN.ID;
                }
                // Calc
            } else if ((TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.CALC) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.PLUS))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.CALC) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.PLUS)))) { // Expect an ID or Int Literal
                try { // Int Literal
                    int test = Integer.parseInt(buffer);
                    TOKEN_BUFFER.append(TOKEN.INTLITERAL);
                    return TOKEN.INTLITERAL;
                } catch (Exception e) { // ID
                    TOKEN_BUFFER.append(TOKEN.ID);
                    return TOKEN.ID;
                }
                // If
            } else if ((TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.EQUALS))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.EQUALS)))) { // Expect an ID or Int Literal
                try { // Int Literal
                    int test = Integer.parseInt(buffer);
                    TOKEN_BUFFER.append(TOKEN.INTLITERAL);
                    return TOKEN.INTLITERAL;
                } catch (Exception e) { // ID
                    TOKEN_BUFFER.append(TOKEN.ID);
                    return TOKEN.ID;
                }
            }
            // The fifth token to expect
            // If
            if ((TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.INTLITERAL))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.ID))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.ID))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.INTLITERAL)))) {
                TOKEN_BUFFER.append(TOKEN.THEN);
                return TOKEN.THEN;
            }
            // The sixth token to expect
            // If, it's either ENDIF or PRINT
            if ((TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.THEN)) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.THEN))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.THEN))) || (TOKEN_BUFFER.toString().equals(String.valueOf(TOKEN.IF) + String.valueOf(TOKEN.ID) + String.valueOf(TOKEN.EQUALS) + String.valueOf(TOKEN.INTLITERAL) + String.valueOf(TOKEN.THEN))))) {
                // We are now in the if statement, use the PushbackReader until we hit "endif" so we can exit!
                String innerBuffer = buffer;
                int innerReadChar = INPUT.read();
                while (innerReadChar != -1) {
                    innerBuffer = innerBuffer + ((char) innerReadChar);
                    innerReadChar = INPUT.read();
                }
                // Process the inner part of the if statement and get rid of ' ' whitespaces
                innerBuffer = innerBuffer.trim();
                innerBuffer = innerBuffer.substring(0, innerBuffer.length() - 5);
                innerBuffer = innerBuffer.toUpperCase();
                // Parse the command
                innerBuffer = parseInnerBuffer(innerBuffer);
                // Add parsed command to the Token_Buffer
                TOKEN_BUFFER.append(innerBuffer);
                // The last step
                TOKEN_BUFFER.append(TOKEN.ENDIF);
                return TOKEN.ENDIF;
            }
        }
        return TOKEN.SCANEOF;
    }

    /**
     * Method that takes a String buffer and takes its lexemes and turns it into a group of tokens and return as a String
     *
     * @param innerBuffer
     * @return
     */
    private String parseInnerBuffer(String innerBuffer) {
        String parsedString = innerBuffer;
        // Declare ...

        // Set ...

        // Calc ...

        // Print <- This is the only required function on the homework assignment
        if (innerBuffer.contains(String.valueOf(TOKEN.PRINT))) {
            parsedString = parsedString.substring(parsedString.lastIndexOf(String.valueOf(TOKEN.PRINT)) + String.valueOf(TOKEN.PRINT).length() + 1).trim();
            // The rest is the ID or Int Literal
            try { // Int Literal
                int test = Integer.parseInt(parsedString);
                parsedString = "" + TOKEN.PRINT + test;
            } catch (Exception e) { // ID
                parsedString = "" + TOKEN.PRINT + TOKEN.ID;
            }
        }
        // If ...

        // Empty ...
        return parsedString;
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
