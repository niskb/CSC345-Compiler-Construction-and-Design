/**
 * Brian Niski
 */

package niski.csc;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyParser {

    /**
     * Abstract Syntax Tree Class Object
     */
    AbstractSyntaxTree abstractSyntaxTree = new AbstractSyntaxTree();

    /**
     * Parsing Error Codes
     */
    private static final int MATCH_ERROR = -1;
    private static final int DECLARATION_ERROR = -2;
    private static final int UNDECLARED_ERROR = -3;
    private static final int UNDEFINED_ERROR = -4;
    private static final int CALC_EXPECTED_ID_OR_INT_LITERAL_ERROR = -5;
    private static final int UNKNOWN_ERROR = -6;

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
        AbstractSyntaxTree.NodeProgram nodeProgram = null;
        if (nextToken != MyScanner.TOKEN.SCANEOF) {
            nodeProgram = program();
        }
        if (match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("\nParsing successful.\n");
            // Display Abstract Syntax Tree
            System.out.println("Abstract Syntax Tree:");
            abstractSyntaxTree.display();
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
    private AbstractSyntaxTree.NodeProgram program() {
        AbstractSyntaxTree.NodeDecls nodeDecls = decls();
        abstractSyntaxTree.getRoot().nodeDecls = nodeDecls;
        abstractSyntaxTree.getRoot().nodeStmts = stmts();
        return abstractSyntaxTree.getRoot();
    }

    /**
     * <Decls>
     */
    private AbstractSyntaxTree.NodeDecls decls() {
        AbstractSyntaxTree.NodeDecls outer = abstractSyntaxTree.new NodeDecls();
        if (nextToken != MyScanner.TOKEN.SCANEOF) {
            AbstractSyntaxTree.NodeId nodeId = decl();
            outer.nodeDeclList.add(nodeId);
            if (nextToken == MyScanner.TOKEN.DECLARE) {
                AbstractSyntaxTree.NodeDecls inner = decls();
                for (int i = 0; i < inner.nodeDeclList.size(); i++) {
                    outer.nodeDeclList.add(inner.nodeDeclList.get(i));
                }
            }
        }
        return outer;

    }

    /**
     * <Decl>
     */
    private AbstractSyntaxTree.NodeId decl() {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            System.exit(MATCH_ERROR);
        }
        String id = scanner.getLastLexeme();
        if (!match(MyScanner.TOKEN.ID)) {
            System.exit(MATCH_ERROR);
        }
        declareID(id);

        AbstractSyntaxTree.NodeId nodeId = abstractSyntaxTree.new NodeId(id);
        return nodeId;
    }


    /**
     * <Stmts>
     */
    private AbstractSyntaxTree.NodeStmts stmts() {
        AbstractSyntaxTree.NodeStmts outer = abstractSyntaxTree.new NodeStmts();
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <Stmts>
            AbstractSyntaxTree.NodeStmt nodeStmt = stmt();
            outer.nodeStmtList.add(nodeStmt);
            if ((nextToken == MyScanner.TOKEN.PRINT) || (nextToken == MyScanner.TOKEN.SET) || (nextToken == MyScanner.TOKEN.IF) || (nextToken == MyScanner.TOKEN.CALC)) {
                AbstractSyntaxTree.NodeStmts inner = stmts();
                for (int i = 0; i < inner.nodeStmtList.size(); i++) {
                    outer.nodeStmtList.add(inner.nodeStmtList.get(i));
                }
            }
        }
        return outer;
    }

    /**
     * <Stmt>
     */
    private AbstractSyntaxTree.NodeStmt stmt() {
        // Parse Print <Stmt>
        if ((nextToken == MyScanner.TOKEN.PRINT)) {
            if (!match(MyScanner.TOKEN.PRINT)) {
                System.exit(MATCH_ERROR);
            }
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            printStatement(id);

            AbstractSyntaxTree.NodeId nodeId = abstractSyntaxTree.new NodeId(id);
            AbstractSyntaxTree.NodePrint nodePrint = abstractSyntaxTree.new NodePrint(nodeId);
            return nodePrint;
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
            setID(id, intLiteral);

            AbstractSyntaxTree.NodeId nodeId = abstractSyntaxTree.new NodeId(id);
            AbstractSyntaxTree.NodeIntLiteral nodeIntLiteral = abstractSyntaxTree.new NodeIntLiteral(Integer.parseInt(intLiteral));
            AbstractSyntaxTree.NodeSet nodeSet = abstractSyntaxTree.new NodeSet(nodeId, nodeIntLiteral);
            return nodeSet;
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
            // Parse <Stmts>
            AbstractSyntaxTree.NodeStmts nodeInnerStmts = stmts();
            // End of If
            if (!match(MyScanner.TOKEN.ENDIF)) {
                System.exit(MATCH_ERROR);
            }

            AbstractSyntaxTree.NodeId nodeIdLeft = abstractSyntaxTree.new NodeId(id1);
            AbstractSyntaxTree.NodeId nodeIdRight = abstractSyntaxTree.new NodeId(id2);
            AbstractSyntaxTree.NodeIf nodeIf = abstractSyntaxTree.new NodeIf(nodeIdLeft, nodeIdRight, nodeInnerStmts);
            return nodeIf;
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
            calcID(id);
            AbstractSyntaxTree.NodePlus nodePlus = sum();

            AbstractSyntaxTree.NodeId nodeId = abstractSyntaxTree.new NodeId(id);
            AbstractSyntaxTree.NodeCalc nodeCalc = abstractSyntaxTree.new NodeCalc(nodeId, nodePlus);
            return nodeCalc;
        }
        AbstractSyntaxTree.NodeStmt temp = abstractSyntaxTree.new NodeStmt() {
            @Override
            public void display() {
                System.out.println("unknown\n");
                generateUnknownErrorMessage();
                System.exit(UNKNOWN_ERROR);
            }
        };
        return temp;
    }

    /**
     * <Sum>
     */
    private AbstractSyntaxTree.NodePlus sum() {
        // Left-hand Side
        AbstractSyntaxTree.NodeExpr nodePlusLeft = value();
        // Right-hand Side
        AbstractSyntaxTree.NodePlus nodePlusRight = sumEnd();

        AbstractSyntaxTree.NodePlus nodePlus = abstractSyntaxTree.new NodePlus(nodePlusLeft, nodePlusRight);
        return nodePlus;
    }

    /**
     * <Value>
     * This is a special expression, it can be either an id or int literal
     */
    private AbstractSyntaxTree.NodeExpr value() {
        if (nextToken == MyScanner.TOKEN.ID) {
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            valueID(id);
            AbstractSyntaxTree.NodeId nodeId = abstractSyntaxTree.new NodeId(id);
            return nodeId;
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            String intLiteral = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.INTLITERAL)) {
                System.exit(MATCH_ERROR);
            }
            AbstractSyntaxTree.NodeIntLiteral nodeIntLiteral = abstractSyntaxTree.new NodeIntLiteral(Integer.parseInt(intLiteral));
            return nodeIntLiteral;
        }
        AbstractSyntaxTree.NodeExpr temp = abstractSyntaxTree.new NodeExpr() {
            @Override
            public void display() {
                System.out.println("unknown\n");
                generateCalcExpectedErrorMessage();
                System.exit(CALC_EXPECTED_ID_OR_INT_LITERAL_ERROR);
            }
        };
        return temp;
    }

    /**
     * <SumEnd>
     */
    private AbstractSyntaxTree.NodePlus sumEnd() {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <SumEnd>
            if (nextToken == MyScanner.TOKEN.PLUS) {
                if (!match(MyScanner.TOKEN.PLUS)) {
                    System.exit(MATCH_ERROR);
                }
                // Left-hand Side
                AbstractSyntaxTree.NodeExpr nodeExprLeft = value();
                // Right-hand Side
                AbstractSyntaxTree.NodePlus nodePlusRight = sumEnd();
                AbstractSyntaxTree.NodePlus nodePlus = abstractSyntaxTree.new NodePlus(nodeExprLeft, nodePlusRight);
                return nodePlus;
            }
            return null;
        }
        return null;
    }

    /**
     * Parsing Helper Methods
     * These methods detect additional parsing errors
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
        } else {
            generateDeclarationErrorMessage(id);
            System.exit(DECLARATION_ERROR);
        }
    }

    /**
     * Executes when "print id" is parsed
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
        }
    }

    /**
     * Executes when "if id = id then" is parsed
     * Returns a parsing error if the IDs are not in the symbol table or one or both are undefined
     *
     * @param id1
     * @param id2
     */
    private void ifStatement(String id1, String id2) {
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
            }
        }
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
     * No ID or Int Literal After '+' Error
     */
    private void generateCalcExpectedErrorMessage() {
        System.out.println("\nParse Error");
        System.out.println("Received: +\tBuffer +");
        System.out.println("Error Message: No id or int literal after +!");
    }

    /**
     * Unknown Error
     */
    private void generateUnknownErrorMessage() {
        System.out.println("\nParse Error");
        System.out.println("Fatal Error Message: Unknown Error!");
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
