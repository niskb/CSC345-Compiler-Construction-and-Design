package niski.csc;

public class Id extends Expr {

    private String name;

    public Id(String name) {
        this.name = name;
    }

    @Override
    public void show() {
        System.out.println(this.name);
    }

}
