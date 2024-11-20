package niski.csc;

import java.util.ArrayList;
import java.util.List;

public class AST {

    public List<String> assemblyCodeList = new ArrayList<>();
    public int nextOpenRegister = 1;
    public ASTExprBase root;

    private String getNextOpenRegister() {
        String reg = String.format("r%d", nextOpenRegister);
        nextOpenRegister++;
        return reg;
    }

    public abstract class ASTExprBase {
        abstract String code();
    }

    public class ASTId extends ASTExprBase {
        public String name;

        public ASTId(String idName) {
            this.name = idName;
        }

        @Override
        String code() {
            String reg;
            String line;
            reg = getNextOpenRegister();
            line = String.format("load %s, %s", reg, name);
            assemblyCodeList.add(line);
            return reg;
        }
    }

    public class ASTAdd extends ASTExprBase {
        public ASTExprBase lhs;
        public ASTExprBase rhs;

        public ASTAdd(ASTExprBase lhs, ASTExprBase rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        String code() {
            String regResult;
            String regLhs;
            String regRhs;
            regLhs = lhs.code();
            regRhs = rhs.code();
            regResult = getNextOpenRegister();
            String line;
            line = String.format("add %s, %s, %s", regLhs, regRhs, regResult);
            assemblyCodeList.add(line);
            return regResult;
        }
    }

    public String generateAssemblyCode() {
        StringBuilder fullCodeString = new StringBuilder();
        assemblyCodeList.clear();
        root.code();
        for (String line : assemblyCodeList) {
            fullCodeString.append(line);
            fullCodeString.append("\n");
        }
        return fullCodeString.toString();
    }

    /* Do not do this on the homework */
    public AST() {
        ASTId idA = new ASTId("a");
        ASTId idB = new ASTId("b");
        ASTId idC = new ASTId("c");
        ASTAdd addBC = new ASTAdd(idB, idC);
        ASTAdd addABC = new ASTAdd(idA, addBC);
        root = addABC;
    }
}
