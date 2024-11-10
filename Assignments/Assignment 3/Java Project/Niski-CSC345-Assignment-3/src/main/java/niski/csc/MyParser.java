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
     * Parsing Error Codes
     */
    private static final int MATCH_ERROR = -1;
    private static final int DECLARATION_ERROR = -2;
    private static final int UNDECLARED_ERROR = -3;
    private static final int UNDEFINED_ERROR = -4;

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
     * This is the lookup table for variable information
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
            System.out.println("Lexeme Scanned: " + scanner.getLastLexeme());
            System.out.println("Token, \"" + nextToken + "\" matches with expected token, \"" + expectedToken + "\"");
            System.out.println("Token Buffer:\n" + scanner.getTokenBufferString());
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
        // Initialize scanner and get the next token
        scanner = new MyScanner(new PushbackReader(new StringReader(program)));
        nextToken = getNextToken();

        // Parse program
        if (nextToken != MyScanner.TOKEN.SCANEOF) {
            program();
        }
        if (match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("\nParsing successful.");
            return true;
        } else {
            System.out.println("\nParsing failed!");
            return false;
        }
    }

    /**
     * Returns the next token from the scanner
     *
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

    /**
     * Recursive Descent Parsing Methods
     */
    /**
     * <Program>
     */
    private void program() {
        decls();
        stmts(true);
    }

    /**
     * <Decls>
     */
    private void decls() {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <Decls>
            decl();
            if (nextToken == MyScanner.TOKEN.DECLARE) {
                decls();
            }
        }
    }

    /**
     * <Decl>
     */
    private void decl() {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            System.exit(MATCH_ERROR);
        }
        String id = scanner.getLastLexeme();
        if (!match(MyScanner.TOKEN.ID)) {
            System.exit(MATCH_ERROR);
        }
        declareID(id);
    }


    /**
     * <Stmts>
     * This method has a parameter for when the if statement is either true or false
     * This parameter is important because we do not want to execute the statements inside the if statement when it is false
     *
     * @param statementTrue
     */
    private void stmts(boolean statementTrue) {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <Stmts>
            stmt(statementTrue);
            if ((nextToken == MyScanner.TOKEN.PRINT) || (nextToken == MyScanner.TOKEN.SET) || (nextToken == MyScanner.TOKEN.IF) || (nextToken == MyScanner.TOKEN.CALC)) {
                stmts(statementTrue);
            }
        }
    }

    /**
     * <Stmt>
     * If the statementTrue parameter is true, the block is able to execute the statements, otherwise just parse the statement
     *
     * @param statementTrue
     */
    private void stmt(boolean statementTrue) {
        // Parse Print <Stmt>
        if ((nextToken == MyScanner.TOKEN.PRINT)) {
            if (!match(MyScanner.TOKEN.PRINT)) {
                System.exit(MATCH_ERROR);
            }
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            if (statementTrue) {
                printStatement(id);
            }
        }
        // Parse Set <Stmt>
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
            if (statementTrue) {
                setID(id, intLiteral);
            }
        }
        // Parse If <Stmt>
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
            // Inner Statements
            // Determines whether to execute the statements in the if statement block at the parser level
            if (ifStatement(id1, id2) && statementTrue) { // If the statement is true and if the previous statement was true
                // Parse <Stmts>
                stmts(true);
            } else { // The if statement is false
                // We still have to parse the inside block of the if statement
                // Parse <Stmts>
                stmts(false);
            }
            // End of If
            if (!match(MyScanner.TOKEN.ENDIF)) {
                System.exit(MATCH_ERROR);
            }
        }
        // Parse Calc <Stmt>
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
            // Calculate the sum at the parser level; these recursive descent parser methods have an "int" return value
            // We can calculate the sum recursively
            if (statementTrue) {
                calcID(id);
                // Parse <Sum>, then update ID
                int calc = sum();
                // Update ID - Do not update the value! "There is no need to do actual calculations during parsing at this point"
                /*symbolTable.get(id).name = String.valueOf(calc);*/
                System.out.println(id + " has been calculated as " + symbolTable.get(id).name + ".\nSymbol Table has not been updated!\nCalc parse successful.");
            } else { // Just run <Sum> and do not update the ID because the if statement/block was false
                calcID(id);
                sum();
                /*System.out.println("No ID has been updated because the if statement was false!");*/
            }
        }
    }

    /**
     * <Sum>
     * Returns the sum calculation recursively
     *
     * @return
     */
    private int sum() {
        // Parse <Value>
        int calc = value();
        // Parse <SumEnd>
        calc = sumEnd(calc);
        return calc;
    }

    /**
     * <Value>
     * Returns a parsing error if there is a Match error between ID or INTLITERAL
     * Returns the value of the ID or INTLITERAL
     *
     * @return
     */
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

    /**
     * <SumEnd>
     * Continues to parse the program recursively
     * Returns the sum calculation
     *
     * @param calc
     * @return
     */
    private int sumEnd(int calc) {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <SumEnd>
            if (nextToken == MyScanner.TOKEN.PLUS) {
                if (!match(MyScanner.TOKEN.PLUS)) {
                    System.exit(MATCH_ERROR);
                }
                // Parse <Value>
                calc += value();
                // Parse <SumEnd>
                calc = sumEnd(calc);
            }
            return calc;
        }
        return calc;
    }

    /**
     * Helper Methods
     */

    /**
     * Executes when "declare id" is parsed
     * Declares a new ID
     * Returns a parsing error if the ID is already inside the symbol table
     *
     * @param id
     */
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

    /**
     * Executes when "print id" is parsed
     * Prints the ID
     * Returns a parsing error if the ID is not inside the symbol table or the ID is undefined
     *
     * @param id
     */
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

    /**
     * Executes when "set id = intliteral" is parsed
     * Defines an ID
     * Returns a parsing error if the ID is not in the symbol table
     *
     * @param id
     * @param intLiteral
     */
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

    /**
     * Executes when the parsed if statement to check if it's either true or false
     * We still have to parse the inner statements when it returns false
     * Returns a parsing error if the IDs are not in the symbol table or one or both are undefined
     *
     * @param id1
     * @param id2
     * @return
     */
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
                    System.out.println("If Statement: " + id1 + " = " + id2 + " is true.");
                    return true;
                } else {
                    System.out.println("If Statement: " + id1 + " = " + id2 + " is false.");
                }
            }
        }
        return false;
    }

    /**
     * Executes when "calc id = <Sum>" is parsed
     * Returns a parsing error if the ID is undeclared
     * If this ID is undefined, that ID must not be used in <Sum>
     *
     * @param id
     */
    private void calcID(String id) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem calcSTI = symbolTable.get(id);
            if (calcSTI.name.isEmpty()) {
                generateCalcWarningMessage(id);
            }
            System.out.println("Calc: " + id + "\tCurrent Value: " + calcSTI.name);
        }
    }

    /**
     * Executes when "<Value>" is parsed
     * Returns a parsing error if the ID is undeclared or undefined
     *
     * @param id
     */
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

    /**
     * Parsing Error Messages
     */
    /**
     * Match Error
     *
     * @param expectedToken
     */
    private void generateMatchErrorMessage(MyScanner.TOKEN expectedToken) {
        System.out.println("\nParse Error");
        System.out.println("Received: " + nextToken + "\tBuffer " + scanner.getLastLexeme());
        System.out.println("Expected: " + expectedToken);
    }

    /**
     * Declaration Error
     *
     * @param id
     */
    private void generateDeclarationErrorMessage(String id) {
        System.out.println("\nParse Error");
        System.out.println("Received: " + MyScanner.TOKEN.ID + "\tBuffer " + id);
        System.out.println("Error Message: " + id + " is already declared!");
    }

    /**
     * Undeclared ID Error
     *
     * @param id
     */
    private void generateUndeclaredErrorMessage(String id) {
        System.out.println("\nParse Error");
        System.out.println("Received: " + MyScanner.TOKEN.ID + "\tBuffer " + id);
        System.out.println("Error Message: " + id + " is undeclared!");
    }

    /**
     * Undefined ID Error
     *
     * @param id
     */
    private void generateUndefinedErrorMessage(String id) {
        System.out.println("\nParse Error");
        System.out.println("Received: " + MyScanner.TOKEN.ID + "\tBuffer " + id);
        System.out.println("Error Message: " + id + " is undefined!");
    }

    /**
     * Parsing Warning Messages
     */
    /**
     * Undefined ID in Calculation
     *
     * @param id
     */
    private void generateCalcWarningMessage(String id) {
        System.out.println("\nParse Warning");
        System.out.println("Received: " + MyScanner.TOKEN.ID + "\tBuffer " + id);
        System.out.println("Warning Message: " + id + " being used in calc function is undefined!\n");
    }

}
