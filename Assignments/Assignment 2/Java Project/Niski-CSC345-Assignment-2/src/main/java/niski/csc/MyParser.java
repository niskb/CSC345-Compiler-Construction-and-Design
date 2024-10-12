/**
 * Brian Niski
 */

package niski.csc;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MyParser {

    /**
     * Parsing error codes
     */
    private static final int MATCH_ERROR = -1;
    private static final int DECLARATION_ERROR = -2;
    private static final int UNDECLARED_ERROR = -3;
    private static final int UNDEFINED_ERROR = -4;
    private static final int IF_STATEMENT_NEVER_CLOSED_ERROR = -5;

    /**
     * Type
     */
    private enum TYPE {INTDATATYPE}

    /**
     * SymbolTableItem for the Symbol Table
     */
    private class SymbolTableItem {
        public String name;
        public TYPE type;
    }

    /**
     * This is the lookup table
     */
    private Map<String, SymbolTableItem> symbolTable = new HashMap<String, SymbolTableItem>();

    /**
     * MyScanner
     */
    private MyScanner scanner;

    /**
     * This is used for the next token in the token buffer
     */
    private MyScanner.TOKEN nextToken;

    /**
     * If the expected token has the same value as the next token, print a message stating what token was matched as well as the contents of the token buffer, scan the next token, and return true.
     * Otherwise, it should print an error message and return false.
     * The error message displays both what it expected and what was scanned.
     *
     * @param expectedToken
     * @return
     */
    private boolean match(MyScanner.TOKEN expectedToken) {
        if (nextToken == expectedToken) {
            getNextToken();
            return true;
        }
        generateMatchErrorMessage(expectedToken);
        return false;
    }

    /**
     * Method to parse a program.
     * The program to parse is passed in as a String.
     * It should create a new instance of MyScanner for the program parameter.
     * It should then start a recursive descent parse of the program.
     *
     * @param program
     * @return
     */
    public boolean parse(String program) {
        // Parse program
        scanner = new MyScanner(new PushbackReader(new StringReader(program)));
        nextToken = getNextToken();

        if (nextToken != MyScanner.TOKEN.SCANEOF) {
            program();
        }
        if (match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("Parsing successful.");
            return true;
        } else {
            System.out.println("Parsing failed!");
            return false;
        }
    }

    /**
     * Returns the next token from the scanner
     * @return
     */
    private MyScanner.TOKEN getNextToken() {
        try {
            nextToken = scanner.scan();
            return nextToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMatchErrorMessage(MyScanner.TOKEN expectedToken) {
        System.out.println("Parse Error:");
        System.out.println("Received: " + nextToken);
        String buffer = scanner.getTokenBufferString();
        System.out.println("Token Buffer:\n" + buffer);
        System.out.println("END OF BUFFER");
        System.out.println("Expected: " + expectedToken);
    }

    private void generateDeclarationErrorMessage(String id) {
        System.out.println("Parse Error:");
        System.out.println("Received: " + id);
        System.out.println("Error Message: " + id + " is already declared!");
    }

    private void generateUndeclaredErrorMessage(String id) {
        System.out.println("Parse Error:");
        System.out.println("Received: " + id);
        System.out.println("Error Message: " + id + " is undeclared!");
    }

    private void generateUndefinedErrorMessage(String id) {
        System.out.println("Parse Error:");
        System.out.println("Received: " + id);
        System.out.println("Error Message: " + id + " is undefined!");
    }

    private void generateIfStatementNeverClosedError() {
        System.out.println("Parse Error: ");
        String buffer = scanner.getTokenBufferString();
        System.out.println("Token Buffer:\n" + buffer);
        System.out.println("Error Message: If Statement is never closed!");
    }

    private void program() {
        decls();
        stmts();
    }

    private void decls() {
        decl();
        if (nextToken == MyScanner.TOKEN.DECLARE) {
            decls();
        }
    }

    private void decl() {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            System.exit(MATCH_ERROR);
        }
        String id = scanner.getLastLexeme();
        if (!match(MyScanner.TOKEN.ID)) {
            System.exit(MATCH_ERROR);
        }
        if (!match(MyScanner.TOKEN.INTDATATYPE)) {
            System.exit(MATCH_ERROR);
        }
        declareID(id);
    }

    private void declareID(String id) {
        if (!symbolTable.containsKey(id)) {
            SymbolTableItem newItem = new SymbolTableItem();
            newItem.name = ""; // empty because we are just declaring it
            newItem.type = TYPE.INTDATATYPE;
            symbolTable.put(id, newItem);
            System.out.println(id + " has been declared.");
        } else {
            generateDeclarationErrorMessage(id);
            System.exit(DECLARATION_ERROR);
        }
    }

    private void stmts() {
        if (nextToken != MyScanner.TOKEN.SCANEOF) {
            stmt();
            if ((nextToken == MyScanner.TOKEN.PRINT) || (nextToken == MyScanner.TOKEN.SET) || (nextToken == MyScanner.TOKEN.IF) || (nextToken == MyScanner.TOKEN.CALC)) {
                stmts();
            }
        }
    }

    private void printStatement(String id) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem printID = symbolTable.get(id);
            if (printID.name.isEmpty()) {
                generateUndefinedErrorMessage(id);
                System.exit(UNDEFINED_ERROR);
            } else {
                System.out.println("ID: " + id + "\tValue: " + printID.name);
            }
        }
    }

    private void setID(String id, String intLiteral) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem setID = symbolTable.get(id);
            setID.name = intLiteral;
            System.out.println(id + " has been set to " + intLiteral + ".");
        }
    }

    private boolean ifStatement(String id1, String id2) {
        if (!symbolTable.containsKey(id1)) {
            generateUndeclaredErrorMessage(id1);
            System.exit(UNDECLARED_ERROR);
        } else if (!symbolTable.containsKey(id2)) {
            generateUndeclaredErrorMessage(id2);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem ifID1 = symbolTable.get(id1);
            SymbolTableItem ifID2 = symbolTable.get(id2);
            if (ifID1.name.isEmpty()) {
                generateUndefinedErrorMessage(id1);
                System.exit(UNDEFINED_ERROR);
            } else if (ifID2.name.isEmpty()) {
                generateUndefinedErrorMessage(id2);
                System.exit(UNDEFINED_ERROR);
            } else {
                if (ifID1.name.equals(ifID2.name)) {
                    System.out.println("If Statement: " + id1 + " == " + id2 + " is true.");
                    return true;
                } else {
                    System.out.println("If Statement: " + id1 + " == " + id2 + " is false.");
                }
            }
        }
        return false;
    }

    private void calcID(String id) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem calcSTI = symbolTable.get(id);
            if (calcSTI.name.isEmpty()) {
                generateUndefinedErrorMessage(id);
                System.exit(UNDEFINED_ERROR);
            } else {
                System.out.println("Calc: " + id + "\tCurrent Value: " + calcSTI.name);
            }
        }
    }

    private void valueID(String id) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem valueSTI = symbolTable.get(id);
            if (valueSTI.name.isEmpty()) {
                generateUndefinedErrorMessage(id);
                System.exit(UNDEFINED_ERROR);
            } else {
                System.out.println("\tValue: " + id + "\tAdd: " + valueSTI.name);
            }
        }
    }

    private void stmt() {
        // Print
        if ((nextToken == MyScanner.TOKEN.PRINT)) {
            if (!match(MyScanner.TOKEN.PRINT)) {
                System.exit(MATCH_ERROR);
            }
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            printStatement(id);
        }
        // Set
        else if (nextToken == MyScanner.TOKEN.SET) {
            if (!match(MyScanner.TOKEN.SET)) {
                System.exit(MATCH_ERROR);
            }
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            if (!match(MyScanner.TOKEN.EQUALS)) {
                System.exit(MATCH_ERROR);
            }
            String intLiteral = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.INTLITERAL)) {
                System.exit(MATCH_ERROR);
            }
            setID(id, intLiteral);
        }
        // If
        else if (nextToken == MyScanner.TOKEN.IF) {
            if (!match(MyScanner.TOKEN.IF)) {
                System.exit(MATCH_ERROR);
            }
            String id1 = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            if (!match(MyScanner.TOKEN.EQUALS)) {
                System.exit(MATCH_ERROR);
            }
            String id2 = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            if (!match(MyScanner.TOKEN.THEN)) {
                System.exit(MATCH_ERROR);
            }
            boolean ifStatementValue = ifStatement(id1, id2);
            // Inner Statements - Ignore stmts() if the values are false.
            if (ifStatementValue) {
                stmts();
            } else {
                while (nextToken != MyScanner.TOKEN.ENDIF) {
                    try {
                        if (nextToken == MyScanner.TOKEN.SCANEOF) {
                            generateIfStatementNeverClosedError();
                            System.exit(IF_STATEMENT_NEVER_CLOSED_ERROR);
                        }
                        nextToken = scanner.scan();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            // End of If
            if (!match(MyScanner.TOKEN.ENDIF)) {
                System.exit(MATCH_ERROR);
            }
        }
        // Calc
        else if (nextToken == MyScanner.TOKEN.CALC) {
            if (!match(MyScanner.TOKEN.CALC)) {
                System.exit(MATCH_ERROR);
            }
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            if (!match(MyScanner.TOKEN.EQUALS)) {
                System.exit(MATCH_ERROR);
            }
            calcID(id);
            int calc = sum();
            symbolTable.get(id).name = String.valueOf(calc);
            System.out.println(id + " has been updated to " + symbolTable.get(id).name + ".");
        }
    }

    private int sum() {
        // ADDING NUMBERS RECURSIVELY?! Yes.
        int calc = value();
        calc = sumEnd(calc);
        return calc;
    }

    private int value() {
        if (nextToken == MyScanner.TOKEN.ID) {
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            valueID(id);
            return Integer.parseInt(symbolTable.get(id).name);
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            String intLiteral = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.INTLITERAL)) {
                System.exit(MATCH_ERROR);
            }
            return Integer.parseInt(intLiteral);
        }
        return 0;
    }

    private int sumEnd(int calc) {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            if (!match(MyScanner.TOKEN.PLUS)) {
                System.exit(MATCH_ERROR);
            }
            calc += value();
            calc = sumEnd(calc);
        }
        return calc;
    }

}
