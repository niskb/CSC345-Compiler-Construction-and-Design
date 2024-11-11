/**
 * Brian Niski
 */

package niski.csc;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyParser {

    /**
     * Abstract Syntax Tree
     */
    private AbstractSyntaxTree outer = new AbstractSyntaxTree();
    private AbstractSyntaxTree.NodeDecls nodeDecls;
    private AbstractSyntaxTree.NodeStmts nodeStmts;

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

        AbstractSyntaxTree.NodeProgram nodeProgram = null;
        // Parse program
        if (nextToken != MyScanner.TOKEN.SCANEOF) {
            nodeProgram = program();
        }
        if (match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("\nParsing successful.");
            // Abstract Syntax Tree Display
            System.out.println();
            // Reverse the list before displaying because of .addAll()
            outer.astRoot.nodeDecls.nodeIdList = outer.astRoot.nodeDecls.nodeIdList.reversed();
            // Display AST
            outer.display();
            System.out.println();
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
        decls();
        List<AbstractSyntaxTree.NodeStmt> nodeStmtsList = outer.new NodeStmts().nodeStmtList;
        stmts(nodeStmtsList);
        AbstractSyntaxTree.NodeProgram nodeProgram = outer.astRoot;
        return nodeProgram;
    }

    /**
     * <Decls>
     *
     */
    private AbstractSyntaxTree.NodeDecls decls() {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <Decls>
            AbstractSyntaxTree.NodeId nodeId = decl();
            if (nextToken == MyScanner.TOKEN.DECLARE) {
                decls();
            }
        }
        return outer.astRoot.nodeDecls;
    }

    /**
     * <Decl>
     *
     */
    private AbstractSyntaxTree.NodeId decl() {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            System.exit(MATCH_ERROR);
        }
        String id = scanner.getLastLexeme();
        if (!match(MyScanner.TOKEN.ID)) {
            System.exit(MATCH_ERROR);
        }
        return declareID(id);
    }

    /**
     * <Stmts>
     */
    private AbstractSyntaxTree.NodeStmts stmts(List<AbstractSyntaxTree.NodeStmt> nodeStmtsList) {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <Stmts>
            AbstractSyntaxTree.NodeStmt nodeStmt = stmt();
            if ((nextToken == MyScanner.TOKEN.PRINT) || (nextToken == MyScanner.TOKEN.SET) || (nextToken == MyScanner.TOKEN.IF) || (nextToken == MyScanner.TOKEN.CALC)) {
                stmts(nodeStmtsList);
            }
            nodeStmtsList.add(nodeStmt);
        }
        outer.astRoot.nodeStmts.nodeStmtList.addAll(nodeStmtsList);
        AbstractSyntaxTree.NodeStmts statements = outer.new NodeStmts();
        statements.nodeStmtList.addAll(nodeStmtsList);
        return statements;
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
            return printStatement(id);
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
            // Inner Statements <Stmts>
            List<AbstractSyntaxTree.NodeStmt> nodeStmtsList = outer.new NodeStmts().nodeStmtList; // Keep track of statements in if statement
            AbstractSyntaxTree.NodeStmts ifNodeStmts = stmts(nodeStmtsList);
            // End of If
            if (!match(MyScanner.TOKEN.ENDIF)) {
                System.exit(MATCH_ERROR);
            }
            ifStatement(id1, id2);
            // Abstract Syntax Tree <If>
            AbstractSyntaxTree.NodeId leftHandSide = outer.new NodeId();
            leftHandSide.variableName = id1;
            AbstractSyntaxTree.NodeId rightHandSide = outer.new NodeId();
            rightHandSide.variableName = id2;
            AbstractSyntaxTree.NodeIf nodeIf = outer.new NodeIf(leftHandSide, ifNodeStmts, rightHandSide);
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
            sum();
            // Abstract Syntax Tree <Calc>
            AbstractSyntaxTree.NodeId nodeId = outer.new NodeId();
            nodeId.variableName = id;
            AbstractSyntaxTree.NodeExpr sumExpr = outer.new NodeExpr() {
                @Override
                public void display() {
                    System.out.println("AST sum");
                }
            };
            AbstractSyntaxTree.NodeCalc nodeCalc = outer.new NodeCalc(nodeId, sumExpr);
            return nodeCalc;
        }
        return null;
    }

    /**
     * <Sum>
     */
    private AbstractSyntaxTree.NodeExpr sum() {
        AbstractSyntaxTree.NodeExpr valueExpr = value();
        sumEnd();
        return valueExpr;
    }

    /**
     * <Value>
     */
    private AbstractSyntaxTree.NodeExpr value() {
        if (nextToken == MyScanner.TOKEN.ID) {
            String id = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.ID)) {
                System.exit(MATCH_ERROR);
            }
            valueID(id);
            // Abstract Syntax Tree (Id)
            // Left side is Id, so right side is '+'
            AbstractSyntaxTree.NodeId nodeId = outer.new NodeId();
            nodeId.variableName = id;
            // Is next token PLUS?
            if (nextToken == MyScanner.TOKEN.PLUS) {
                // Sum
                AbstractSyntaxTree.NodeExpr sumExpr = outer.new NodeExpr() {
                    @Override
                    public void display() {
                        System.out.println("AST sum");
                    }
                };
                AbstractSyntaxTree.NodePlus nodePlus = outer.new NodePlus(nodeId, sumExpr);
                return nodePlus;
            } else {
                AbstractSyntaxTree.NodePlus nodePlus = outer.new NodePlus(nodeId, null);
                return nodePlus;
                // We can save space on the AST if the sum was adding a value on the left-hand side on the previous expression plus the left-hand side on the left-hand side and the other side was null (Put the left-hand side into the previous right-hand side)
            }
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            String intLiteral = scanner.getLastLexeme();
            if (!match(MyScanner.TOKEN.INTLITERAL)) {
                System.exit(MATCH_ERROR);
            }
            // Abstract Syntax Tree (Int Literal)
            // Left side is Int Literal so right side is '+'
            AbstractSyntaxTree.NodeIntLiteral nodeIntLiteral = outer.new NodeIntLiteral();
            nodeIntLiteral.intLiteral = Integer.parseInt(intLiteral);
            // Is next token PLUS?
            if (nextToken == MyScanner.TOKEN.PLUS) {
                // Sum
                AbstractSyntaxTree.NodeExpr nodeExpr = outer.new NodeExpr() {
                    @Override
                    public void display() {
                        System.out.println("sum");
                    }
                };
                AbstractSyntaxTree.NodePlus nodePlus = outer.new NodePlus(nodeIntLiteral, nodeExpr);
                return nodePlus;
            } else {
                AbstractSyntaxTree.NodePlus nodePlus = outer.new NodePlus(nodeIntLiteral, null);
                return nodePlus;
                // We can save space on the AST if the sum was adding a value on the left-hand side on the previous expression plus the left-hand side on the left-hand side and the other side was null (Put the left-hand side into the previous right-hand side)
            }
        }
        return null;
    }

    /**
     * <SumEnd>
     */
    private void sumEnd() {
        if (nextToken != MyScanner.TOKEN.SCANEOF) { // empty production <SumEnd>
            if (nextToken == MyScanner.TOKEN.PLUS) {
                if (!match(MyScanner.TOKEN.PLUS)) {
                    System.exit(MATCH_ERROR);
                }
                value();
                sumEnd();
            }
        }

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
     * @return
     */
    private AbstractSyntaxTree.NodeId declareID(String id) {
        if (!symbolTable.containsKey(id)) {
            SymbolTableItem newItem = new SymbolTableItem();
            newItem.name = ""; // empty because we are just declaring it
            newItem.type = TYPE.INTDATATYPE;
            symbolTable.put(id, newItem);
            AbstractSyntaxTree.NodeId nodeId = outer.new NodeId();
            nodeId.variableName = id;
            outer.astRoot.nodeDecls.nodeIdList.add(nodeId);
        } else {
            generateDeclarationErrorMessage(id);
            System.exit(DECLARATION_ERROR);
        }
        return null;
    }

    /**
     * Executes when "print id" is parsed
     * Prints the ID
     * Returns a parsing error if the ID is not inside the symbol table or the ID is undefined
     *
     * @param id
     */
    private AbstractSyntaxTree.NodePrint printStatement(String id) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else {
            SymbolTableItem printID = symbolTable.get(id);
            if (printID.name.isEmpty()) {
                generatePrintWarningMessage(id);
            } else { // Print the ID
                AbstractSyntaxTree.NodeId nodeId = outer.new NodeId();
                nodeId.variableName = id;
                AbstractSyntaxTree.NodePrint nodePrint = outer.new NodePrint(nodeId);
                outer.astRoot.nodeStmts.nodeStmtList.add(nodePrint);
                return nodePrint;
            }
        }
        return null;
    }

    /**
     * Executes when "set id = intliteral" is parsed
     * Defines an ID
     * Returns a parsing error if the ID is not in the symbol table
     *
     * @param id
     * @param intLiteral
     */
    private AbstractSyntaxTree.NodeSet setID(String id, String intLiteral) {
        if (!symbolTable.containsKey(id)) {
            generateUndeclaredErrorMessage(id);
            System.exit(UNDECLARED_ERROR);
        } else { // Set the ID
            SymbolTableItem setID = symbolTable.get(id);
            setID.name = intLiteral;

            AbstractSyntaxTree.NodeId nodeId = outer.new NodeId();
            nodeId.variableName = id;
            AbstractSyntaxTree.NodeIntLiteral nodeIntLiteral = outer.new NodeIntLiteral();
            nodeIntLiteral.intLiteral = Integer.parseInt(intLiteral);
            AbstractSyntaxTree.NodeSet nodeSet = outer.new NodeSet(nodeId, nodeIntLiteral);
            outer.astRoot.nodeStmts.nodeStmtList.add(nodeSet);
            return nodeSet;
        }
        return null;
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
            } else { // If statement
                if (ifID1.name.equals(ifID2.name)) { // If statement was true
                    return true;
                } else { // If statement was false
                    return false;
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
            // Current ID in Calc
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
            } else { // Add value ID
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
     * Undefined ID in Print
     *
     * @param id
     */
    private void generatePrintWarningMessage(String id) {
        System.out.println("\nParse Warning");
        System.out.println("Received: " + MyScanner.TOKEN.ID + "\tBuffer " + id);
        System.out.println("Warning Message: " + id + " being used in print function is undefined!\n");
    }

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
