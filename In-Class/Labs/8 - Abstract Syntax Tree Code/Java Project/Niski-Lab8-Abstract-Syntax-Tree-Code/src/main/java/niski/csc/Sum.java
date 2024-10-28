package niski.csc;

public class Sum extends Expr {

    private Expr leftHandSide;
    private Expr rightHandSide;

    public Sum(Expr leftHandSide, Expr rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @Override
    public void show() {
        System.out.println("SUM");
        System.out.println("LEFT-HAND SIDE");
        leftHandSide.show();
        System.out.println("RIGHT-HAND SIDE");
        rightHandSide.show();
    }

}
