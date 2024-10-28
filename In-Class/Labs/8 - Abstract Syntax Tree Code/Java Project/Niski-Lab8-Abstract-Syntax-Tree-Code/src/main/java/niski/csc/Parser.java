package niski.csc;

import java.util.Queue;

public class Parser {

    public enum TOKEN {ID, PLUS}

    ;
    private Queue<TOKEN> program;

    public boolean match(TOKEN expectedToken) {
        if (expectedToken == program.peek()) {
            // Consume token
            program.remove();
            return true;
        }
        return false;
    }

    public Expr factor() {
        Id idNode;
        if (program.peek() == TOKEN.ID) {
            match(TOKEN.ID);
            idNode = new Id(TOKEN.ID.toString()); // DO NOT DO THIS ON THE HOMEWORK
            return idNode;
        }
        return null;
    }

    public Expr exprEnd() {
        Expr leftHandSide;
        Expr rightHandSide;
        Expr sumNode;
            if (program.peek() == TOKEN.PLUS) {
                match(TOKEN.PLUS);
                leftHandSide = factor();
                rightHandSide = exprEnd();
                // If right-hand side is null then epsilon production was used
                if (null == rightHandSide) {
                    return leftHandSide;
                }
                // There is a right-hand side
                sumNode = new Sum(leftHandSide, rightHandSide);
                return sumNode;
            }
            // Epsilon production used
            return null;
    }

    public Expr expr() {
        Expr leftHandSide;
        Expr rightHandSide;
        Expr sumNode;
        leftHandSide = factor();
        rightHandSide = exprEnd();
        if (null == rightHandSide) {
           return leftHandSide;
        }
        sumNode = new Sum(leftHandSide, rightHandSide);
        return sumNode;
    }

    public void parse(Queue<TOKEN> program) {
        this.program = program;
        Expr astRoot;
        astRoot = expr();
        astRoot.show();
    }

}
