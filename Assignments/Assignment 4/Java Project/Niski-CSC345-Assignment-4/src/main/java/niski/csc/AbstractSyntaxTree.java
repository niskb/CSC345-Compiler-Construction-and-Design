/**
 * Brian Niski
 */

package niski.csc;

import java.util.ArrayList;
import java.util.List;

public class AbstractSyntaxTree {

    /**
     * Abstract Syntax Tree Node Classes
     */
    /**
     * Base Class for all other AST node classes
     */
    public abstract class NodeBase {
        public abstract void display();
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
            } else { // There is no right-hand side because there is no PLUS
                leftHandSide.display();
                System.out.println();
            }
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
            // Traverse Declarations
            System.out.println("AST Declarations");
            nodeDecls.display();
            // Traverse Statements
            System.out.println("\nAST Statements");
            nodeStmts.display();
        }
    }

    /**
     * Abstract Syntax Tree Members
     */
    private NodeProgram root = new NodeProgram(new NodeDecls(), new NodeStmts());

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

}
