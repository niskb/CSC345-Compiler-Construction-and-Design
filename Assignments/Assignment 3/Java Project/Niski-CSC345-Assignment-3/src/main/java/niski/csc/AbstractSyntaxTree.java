package niski.csc;

import java.util.ArrayList;
import java.util.List;

public class AbstractSyntaxTree {

    /**
     * Abstract Syntax Tree Node Classes
     */
    public abstract class NodeBase {
        public abstract void display();
    }

    public abstract class NodeExpr extends NodeBase {
    }

    public abstract class NodeStmt extends NodeBase {
    }

    public class NodeId extends NodeExpr {
        public String variableName;

        @Override
        public void display() {
            try {
                System.out.println("AST id " + variableName);
            } catch (Exception e) {

            }
        }
    }

    public class NodeIntLiteral extends NodeExpr {
        public int intLiteral;

        @Override
        public void display() {
            try {
                System.out.println("AST int literal " + intLiteral);
            } catch (Exception e) {

            }
        }
    }

    public class NodePlus extends NodeExpr {
        public NodeExpr leftHandSide;
        public NodeExpr rightHandSide;

        public NodePlus(NodeExpr leftHandSide, NodeExpr rightHandSide) {
            this.leftHandSide = leftHandSide;
            this.rightHandSide = rightHandSide;
        }

        @Override
        public void display() {
            try {
                System.out.print("LHS: ");
                leftHandSide.display();
                System.out.print("RHS: " + rightHandSide);
                rightHandSide.display();
            } catch (Exception e) {

            }
        }
    }

    public class NodePrint extends NodeStmt {
        public NodeId variableName;

        public NodePrint(NodeId variableName) {
            this.variableName = variableName;
        }

        @Override
        public void display() {
            try {
                System.out.println("AST print");
                variableName.display();
                System.out.println();
            } catch (Exception e) {

            }
        }
    }

    public class NodeSet extends NodeStmt {
        public NodeId variableName;
        public NodeIntLiteral intLiteral;

        public NodeSet(NodeId variableName, NodeIntLiteral intLiteral) {
            this.variableName = variableName;
            this.intLiteral = intLiteral;
        }

        @Override
        public void display() {
            try {
                System.out.println("AST set");
                variableName.display();
                intLiteral.display();
                System.out.println();
            } catch (Exception e) {

            }
        }
    }

    public class NodeCalc extends NodeStmt {
        public NodeId variableName;
        public NodeExpr nodeExpr;

        public NodeCalc(NodeId variableName, NodeExpr nodeExpr) {
            this.variableName = variableName;
            this.nodeExpr = nodeExpr;
        }

        @Override
        public void display() {
            try {
                System.out.println("AST calc");
                variableName.display();
                nodeExpr.display();
                System.out.println();
            } catch (Exception e) {

            }
        }
    }

    public class NodeStmts {
        List<NodeStmt> nodeStmtList = new ArrayList<>();
    }

    public class NodeIf extends NodeStmt {
        public NodeId leftHandSide;
        public NodeId rightHandSide;
        public NodeStmts nodeStmts;

        public NodeIf(NodeId leftHandSide, NodeStmts nodeStmts, NodeId rightHandSide) {
            this.leftHandSide = leftHandSide;
            this.nodeStmts = nodeStmts;
            this.rightHandSide = rightHandSide;
        }

        @Override
        public void display() {
            try {
                System.out.println("AST if");
                System.out.print("LHS: ");
                leftHandSide.display();
                System.out.print("RHS: ");
                rightHandSide.display();
                for (int i = 0; i < nodeStmts.nodeStmtList.size(); i++) {
                    nodeStmts.nodeStmtList.get(i).display();
                }
                System.out.print("\nAST endif\n\n");
            } catch (Exception e) {

            }
        }
    }

    public class NodeDecls {
        List<NodeId> nodeIdList = new ArrayList<>();
    }

    public class NodeProgram {
        public NodeDecls nodeDecls;
        public NodeStmts nodeStmts;

        public NodeProgram(NodeDecls nodeDecls, NodeStmts nodeStmts) {
            this.nodeDecls = nodeDecls;
            this.nodeStmts = nodeStmts;
        }

        public NodeDecls getNodeDecls() {
            return nodeDecls;
        }

        public NodeStmts getNodeStmts() {
            return nodeStmts;
        }
    }

    /**
     * Abstract Syntax Tree Members
     */
    public NodeProgram astRoot = new NodeProgram(new NodeDecls(), new NodeStmts());

    /**
     * Abstract Syntax Tree Methods
     */
    public NodeProgram getAstRoot() {
        return astRoot;
    }

    public void setAstRoot(NodeProgram astRoot) {
        this.astRoot = astRoot;
    }

    public void display() {
        try {
            System.out.println("AST Declarations");
            for (int i = 0; i < astRoot.getNodeDecls().nodeIdList.size(); i++) {
                System.out.println("AST id " + astRoot.getNodeDecls().nodeIdList.get(i).variableName);
            }
            System.out.println("\nAST Statements");
            for (int i = 0; i < astRoot.getNodeStmts().nodeStmtList.size(); i++) {
                astRoot.getNodeStmts().nodeStmtList.get(i).display();
            }
        } catch (Exception e) {

        }
    }
}
