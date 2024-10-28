/**
 * Brian Niski
 */

package niski.csc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyParserTest {

    // Declare multiple variables, the first character of the variable name must be a letter; "y1"
    @Test
    void declTestOne() {
        String program = "declare w \n" +
                "declare x \n" +
                "declare y1 \n" +
                "declare z ";
        assertEquals(true, new MyParser().parse(program));
    }

    // Declare multiple variables, the first character of the variable name must be a letter; "2w" will fail
    @Test
    void declTestTwo() {
        String program = "declare 2w \n" +
                "declare x  \n" +
                "declare y1  \n" +
                "declare z ";
        assertEquals(false, new MyParser().parse(program));
    }

    // Stop declarations of two or more variables with the same variable name; we try to declare "x" twice, but it will fail
    @Test
    void declTestThree() {
        String program = "declare x \n" +
                "declare y  \n" +
                "declare y1  \n" +
                "declare x ";
        assertEquals(false, new MyParser().parse(program));
    }

    // Pr test for an undeclared variable
    @Test
    void prTestOne() {
        String program = "declare x \n" +
                "pr y";
        assertEquals(false, new MyParser().parse(program));
    }

    // Pr test for an undefined variable
    @Test
    void prTestTwo() {
        String program = "declare x \n" +
                "pr x";
        assertEquals(false, new MyParser().parse(program));
    }

    // Pr test for a declared and set variable
    @Test
    void prTestThree() {
        String program = "declare x \n" +
                "set x = 10\n" +
                "pr x";
        assertEquals(true, new MyParser().parse(program));
    }

    // Set test for a declared variable
    @Test
    void setTestOne() {
        String program = "declare x \n" +
                "set x = 10";
        assertEquals(true, new MyParser().parse(program));
    }

    // Set test for an undeclared variable
    @Test
    void setTestTwo() {
        String program = "declare x \n" +
                "set y = 10";
        assertEquals(false, new MyParser().parse(program));
    }

    // If test parsing an If statement if when an if statement is either true or false
    @Test
    void ifTestOne() {
        String program = "declare x \n" +
                "declare y \n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "if x = y then\n" +
                " pr x\n" +
                " pr x\n" +
                " pr x\n" +
                " pr x\n" +
                " pr x\n" +
                "set y = 10\n" +
                " endif\n" +
                "set x = 15\n" +
                "if x = y then\n" +
                " pr x\n" +
                " pr y\n" +
                " endif";
        assertEquals(true, new MyParser().parse(program));
    }

    // If test if an If Statement is never closed! (Match error)
    @Test
    void ifTestTwo() {
        String program = "declare x \n" +
                "declare y \n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "if x = y then\n" +
                " pr x\n" +
                " pr y";
        assertEquals(false, new MyParser().parse(program));
    }

    // If test parsing NESTED If statements
    @Test
    void ifTestThree() {
        String program = "declare x \n" +
                "declare y \n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "if x = y then\n" +
                " pr x\n" +
                " pr x\n" +
                " pr x\n" +
                " pr x\n" +
                " pr x\n" +
                "set y = 10\n" +
                "if x = y then\n" +
                " pr y\n" +
                " pr y\n" +
                " pr y\n" +
                " pr y\n" +
                " pr y\n" +
                " calc y = x + y" +
                " pr y\n" +
                " endif\n" +
                " endif\n" + // notice that we put an if statement inside an if statement
                "set x = 20\n" +
                "set y = 20\n" +
                "if x = y then\n" +
                " pr x\n" +
                " pr y\n" +
                " endif";
        assertEquals(true, new MyParser().parse(program));
    }

    // If test multi nested if statement
    @Test
    void ifTestFour() {
        String program = "declare x \n" +
                "declare y \n" +
                "declare z \n" +
                "set x = 1\n" +
                "set y = 1\n" +
                "set z = 1\n" +
                "if x = y then\n" +
                "if y = z then\n" +
                "if z = x then\n" +
                "if y = y then\n" +
                " pr x" +
                " pr y" +
                " pr z" +
                " endif\n" +
                " endif\n" +
                " endif\n" +
                " endif\n";
        assertEquals(true, new MyParser().parse(program));
    }

    // Calc test <Sum> Recursion
    @Test
    void calcTestOne() {
        String program = "declare w \n" +
                "declare x \n" +
                "declare y \n" +
                "declare z \n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "set z = 20\n" +
                "calc w = x + y + z\n" +
                "calc x = x + y\n" +
                "calc z = x + y + w + z + x\n" +
                " pr w\n" +
                " pr x\n" +
                " pr z";
        assertEquals(true, new MyParser().parse(program));
    }

    // Calc test undefined ID variable
    @Test
    void calcTestTwo() {
        String program = "declare w \n" +
                "declare x \n" +
                "declare y \n" +
                "declare z \n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "calc z = x + y + w + x\n" +
                " pr z";
        assertEquals(true, new MyParser().parse(program));
    }

    // Calc test undefined ID variable and this ID is being used in the calculation
    @Test
    void calcTestThree() {
        String program = "declare w \n" +
                "declare x \n" +
                "declare y \n" +
                "declare z \n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "calc z = x + y + w + x + z\n" +
                " pr z";
        assertEquals(false, new MyParser().parse(program));
    }

    @Test
    void testSampleProgramOne() {
        String program = "declare w \n" +
                "declare x \n" +
                "declare y \n" +
                "declare z \n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "set z = 20\n" +
                "calc w = x + y + z\n" +
                "if x = y then\n" +
                " pr w\n" +
                " pr x\n" +
                "endif\n" +
                "pr y\n" +
                "pr z";
        // Should return NO syntax error
        assertEquals(true, new MyParser().parse(program));
    }

    @Test
    void testSampleProgramTwo() {
        String program = "declare w \n" +
                "declare x \n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "if w x then\n" +
                " pr w\n" +
                "endif";
        // Should return A syntax error
        assertEquals(false, new MyParser().parse(program));
    }

    @Test
    void testSampleProgramThree() {
        String program = "set w = 5\n" +
                "set x = 10\n" +
                "calc y = w + x";
        // y is not in the symbol table
        assertEquals(false, new MyParser().parse(program));
    }

    @Test
    void testEmptyProgram() {
        String program = "";
        assertEquals(true, new MyParser().parse(program));
    }

}