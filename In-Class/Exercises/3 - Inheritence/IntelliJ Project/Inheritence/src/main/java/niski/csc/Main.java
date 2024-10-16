package niski.csc;

abstract class Shape {

    protected double area;

    public abstract void calcArea();

}

class Rectangle extends Shape {

    int length;
    int width;

    public Rectangle(int length, int width) {
        this.length = length;
        this.width = width;
    }

    @Override
    public void calcArea() {
        area = length * width;
    }

}

class Circle extends Shape {

    int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    @Override
    public void calcArea() {
        area = Math.PI * (Math.pow(radius, 2));
    }

}

public class Main {

    public static void processShapes(Shape[] sa) {

        for (int i = 0; i < sa.length; i++) {
            try {
                sa[i].calcArea();
                System.out.println(sa[i].area);
            } catch (Exception e) {
                continue;
            }
        }

    }

    public static void main(String[] args) {
        Shape[] sa = new Shape[2];

        sa[0] = new Rectangle(2, 4);
//        sa[0].calcArea();
//        System.out.println(sa[0].area);

        sa[1] = new Circle(3);
//        sa[1].calcArea();
//        System.out.println(sa[1].area);

        processShapes(sa);

    }

}