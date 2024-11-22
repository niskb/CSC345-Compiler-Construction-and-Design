/**
 * Brian Niski
 */

package niski.csc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractSyntaxTree {

    /**
     * Abstract Syntax Tree Node Classes
     */
    /**
     * Base Class for all other AST node classes
     */
    public abstract class NodeBase {
        public abstract void display();
        public abstract String generateCode();
    }

    /**
     * Base Class for Expression node classes
     */
    public abstract class NodeExpr extends NodeBase {

    }

    /**
     * Base Class for Statement node classes
     */
    public abstract class NodeStmt extends NodeBase {

    }

    /**
     * Id Node
     */
    public class NodeId extends NodeExpr {
        private String variableName;

        public NodeId(String variableName) {
            this.variableName = variableName;
        }

        @Override
        public void display() {
            System.out.println("AST id " + variableName);
        }

        @Override
        public String generateCode() {
            String reg = "";
            for (int i = 0; i < registerIdMap.size(); i++) {
                if (registerIdMap.containsKey(variableName)) {
                    reg = registerIdMap.get(variableName);
                    break;
                }
            }
            return reg;
        }
    }

    /**
     * Int Literal Node
     */
    public class NodeIntLiteral extends NodeExpr {
        private int intLiteral;

        public NodeIntLiteral(int intLiteral) {
            this.intLiteral = intLiteral;
        }

        @Override
        public void display() {
            System.out.println("AST int literal " + intLiteral);
        }

        @Override
        public String generateCode() {
            String reg = getNextOpenRegister();
            String line = String.format("loadintliteral %s, %d", reg, intLiteral);
            generatedLines.add(line);
            return reg;
        }
    }

    /**
     * Plus Node
     * Left-hand Side is the Expression on the left
     * Right-hand Side is the Expression on the right
     */
    public class NodePlus extends NodeExpr {
        private NodeExpr leftHandSide;
        private NodeExpr rightHandSide;

        public NodePlus(NodeExpr leftHandSide, NodeExpr rightHandSide) {
            this.leftHandSide = leftHandSide;
            this.rightHandSide = rightHandSide;
        }

        @Override
        public void display() {
            if (null != rightHandSide) {
                System.out.println("AST sum");
                System.out.print("LHS: ");
                leftHandSide.display();
                System.out.print("RHS: ");
                rightHandSide.display();
            } else {
                leftHandSide.display();
                System.out.println();
            }
        }

        @Override
        public String generateCode() {
            if (null != rightHandSide) {
                String line = String.format("add %s, %s, %s", leftHandSide.generateCode(), rightHandSide.generateCode(), calcTempReg);
                generatedLines.add(line);
            } else {
                String line = String.format("add %s, %s, %s", leftHandSide.generateCode(), calcTempReg, calcTempReg);
                generatedLines.add(line);
            }
            return calcTempReg;
        }
    }

    /**
     * Print Node
     */
    public class NodePrint extends NodeStmt {
        private NodeId printId;

        public NodePrint(NodeId printId) {
            this.printId = printId;
        }

        @Override
        public void display() {
            System.out.println("AST print");
            printId.display();
            System.out.println();
        }

        @Override
        public String generateCode() {
            String reg = printId.generateCode();
            String line = String.format("printi %s", reg);
            generatedLines.add(line);
            return reg;
        }
    }

    /**
     * Set Node
     */
    public class NodeSet extends NodeStmt {
        private NodeId nodeId;
        private NodeIntLiteral nodeIntLiteral;

        public NodeSet(NodeId nodeId, NodeIntLiteral nodeIntLiteral) {
            this.nodeId = nodeId;
            this.nodeIntLiteral = nodeIntLiteral;
        }

        @Override
        public void display() {
            System.out.println("AST set");
            nodeId.display();
            nodeIntLiteral.display();
            System.out.println();
        }

        @Override
        public String generateCode() {
            String reg = nodeIntLiteral.generateCode();
            String line = String.format("storeintvar %s, %s", reg, nodeId.variableName);
            generatedLines.add(line);
            registerIdMap.put(nodeId.variableName, reg);
            return reg;
        }
    }

    /**
     * Calc Node
     */
    public class NodeCalc extends NodeStmt {
        private NodeId nodeId;
        private NodeExpr nodeExpr;

        public NodeCalc(NodeId nodeId, NodeExpr nodeExpr) {
            this.nodeId = nodeId;
            this.nodeExpr = nodeExpr;
        }

        @Override
        public void display() {
            System.out.println("AST calc");
            nodeId.display();
            nodeExpr.display();
        }

        @Override
        public String generateCode() {
            calcTempReg = getNextOpenRegister();
            String nodeReg = nodeId.generateCode();
            String exprReg = nodeExpr.generateCode();
            String line = String.format("storeintvar %s, %s", exprReg, nodeId.variableName);
            generatedLines.add(line);
            registerIdMap.replace(nodeId.variableName, exprReg);
            return nodeReg;
        }
    }

    /**
     * List of Statement Nodes
     */
    public class NodeStmts extends NodeBase {
        public List<NodeStmt> nodeStmtList = new ArrayList<>();

        @Override
        public void display() {
            for (int i = 0; i < nodeStmtList.size(); i++) {
                nodeStmtList.get(i).display();
            }
        }

        @Override
        public String generateCode() {
            for (int i = 0; i < nodeStmtList.size(); i++) {
                nodeStmtList.get(i).generateCode();
            }
            return "";
        }
    }

    /**
     * If Node
     */
    public class NodeIf extends NodeStmt {
        private NodeId leftHandSide;
        private NodeId rightHandSide;
        private NodeStmts nodeStmts;

        public NodeIf(NodeId leftHandSide, NodeId rightHandSide, NodeStmts nodeStmts) {
            this.leftHandSide = leftHandSide;
            this.rightHandSide = rightHandSide;
            this.nodeStmts = nodeStmts;
        }

        @Override
        public void display() {
            System.out.println("AST if");
            System.out.print("LHS: ");
            leftHandSide.display();
            System.out.print("RHS: ");
            rightHandSide.display();
            System.out.println();
            for (int i = 0; i < nodeStmts.nodeStmtList.size(); i++) {
                nodeStmts.nodeStmtList.get(i).display();
            }
            System.out.println("AST endif\n");
        }

        @Override
        public String generateCode() {
            String reg1 = leftHandSide.generateCode();
            String reg2 = rightHandSide.generateCode();
            String label = "ifStatementLabel" + getNextIfLabelNumber();
            String line1 = String.format("bne %s, %s, %s", reg1, reg2, label);
            generatedLines.add(line1);
            nodeStmts.generateCode();
            String line2 = String.format(":%s", label);
            generatedLines.add(line2);
            return "";
        }
    }

    /**
     * List of Declaration Nodes
     */
    public class NodeDecls extends NodeBase {
        public List<NodeId> nodeDeclList = new ArrayList<>();

        @Override
        public void display() {
            for (int i = 0; i < nodeDeclList.size(); i++) {
                nodeDeclList.get(i).display();
            }
        }

        @Override
        public String generateCode() {
            for (int i = 0; i < nodeDeclList.size(); i++) {
                String var = nodeDeclList.get(i).variableName;
                String line = String.format("var int %s", var);
                generatedLines.add(line);
            }
            return "";
        }
    }

    /**
     * Node that contains Program Declarations and Statements
     */
    public class NodeProgram extends NodeBase {
        public NodeDecls nodeDecls;
        public NodeStmts nodeStmts;

        public NodeProgram(NodeDecls nodeDecls, NodeStmts nodeStmts) {
            this.nodeDecls = nodeDecls;
            this.nodeStmts = nodeStmts;
        }

        @Override
        public void display() {
            System.out.println("AST Declarations");
            nodeDecls.display();
            System.out.println("\nAST Statements");
            nodeStmts.display();
        }

        @Override
        public String generateCode() {
            return "";
        }
    }

    /**
     * Abstract Syntax Tree Members
     */
    private NodeProgram root = new NodeProgram(new NodeDecls(), new NodeStmts());
    private int nextOpenRegister = 1;
    private int nextIfLabel = 1;
    private String calcTempReg = "";

    /**
     * Abstract Syntax Tree Methods
     */
    /**
     * Getter for root
     * Get the program
     * @return
     */
    public NodeProgram getRoot() {
        return root;
    }

    /**
     * Setter for root
     * Set a program
     * @param root
     */
    public void setRoot(NodeProgram root) {
        this.root = root;
    }

    /**
     * Abstract Syntax Tree Display
     * Implemenation of Depth First Traversal of the Program Tree
     */
    public void display() {
        this.root.display();
    }

    /**
     * Code Generation
     */
    /**
     * List to store generated lines of code
     */
    private List<String> generatedLines = new ArrayList<>();
    /**
     * List to store registers with their respected variable name
     * First String is register name, Second String is variable name
     */
    private Map<String, String> registerIdMap = new HashMap<>();
    /**
     * Generate Pseudo Assembler Code
     * Traverse the tree
     * The parent node should call this method on child nodes as necessary
     */
    private void generateCode() {
        generatedLines.add(".data");
        root.nodeDecls.generateCode();
        generatedLines.add(".code");
        root.nodeStmts.generateCode();
    }

    /**
     * Gets code from program
     * This method should return one String for the whole program
     * @return
     */
    public String getCode() {
        generatedLines.clear();
        generateCode();
        StringBuilder code = new StringBuilder();
        for (String generatedLine : generatedLines) {
            code.append(generatedLine).append("\n");
        }
        return code.toString();
    }

    /**
     * Method to get next open register
     * Increment register count by 1
     * This method does not reuse registers
     * @return
     */
    private String getNextOpenRegister() {
        String reg = String.format("ri%d", nextOpenRegister);
        nextOpenRegister++;
        return reg;
    }

    /**
     * Returns the next label number when branching
     * @return
     */
    private int getNextIfLabelNumber() {
        return nextIfLabel++;
    }

}
