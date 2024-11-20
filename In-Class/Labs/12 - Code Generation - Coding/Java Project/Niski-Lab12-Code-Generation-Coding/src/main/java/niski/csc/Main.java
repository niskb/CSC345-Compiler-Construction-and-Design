package niski.csc;

public class Main {
    public static void main(String[] args) {
        AST ast = new AST();
        String assemblyCode;
        assemblyCode = ast.generateAssemblyCode();
        System.out.println(assemblyCode);
    }
}