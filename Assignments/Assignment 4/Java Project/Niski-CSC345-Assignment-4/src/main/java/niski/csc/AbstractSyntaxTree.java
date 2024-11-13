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
        public String variableName;

        @Override
        public void display() {
            System.out.println("AST id " + variableName);
        }
    }

    /**
     * Int Literal Node
     */
    public class NodeIntLiteral extends NodeExpr {
        public int intLiteral;

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
        public NodeExpr leftHandSide;
        public NodeExpr rightHandSide;

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
        public NodeId printId;

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
        public NodeId nodeId;
        public NodeIntLiteral nodeIntLiteral;

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
        public NodeId nodeId;
        public NodeExpr nodeExpr;

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
    public class NodeStmts {
        public List<NodeStmt> nodeStmtList = new ArrayList<>();
    }

    /**
     * If Node
     */
    public class NodeIf extends NodeStmt {
        public NodeId leftHandSide;
        public NodeId rightHandSide;
        public NodeStmts nodeStmts;

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
    public class NodeDecls {
        public List<NodeId> nodeDeclList = new ArrayList<>();
    }

    /**
     * Node that contains Program Declarations and Statements
     */
    public class NodeProgram {
        public NodeDecls nodeDecls;
        public NodeStmts nodeStmts;

        public NodeProgram(NodeDecls nodeDecls, NodeStmts nodeStmts) {
            this.nodeDecls = nodeDecls;
            this.nodeStmts = nodeStmts;
        }

    }

    /**
     * Abstract Syntax Tree Members
     */
    public NodeProgram root = new NodeProgram(new NodeDecls(), new NodeStmts());

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
        // Traverse Declarations
        System.out.println("AST Declarations");
        for (int i = 0; i < this.root.nodeDecls.nodeDeclList.size(); i++) {
            this.root.nodeDecls.nodeDeclList.get(i).display();
        }
        // Traverse Statements
        System.out.println("\nAST Statements");
        for (int i = 0; i < this.root.nodeStmts.nodeStmtList.size(); i++) {
            this.root.nodeStmts.nodeStmtList.get(i).display();
        }
    }

}
