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
        String program = "declare w int\n" +
                "declare x int \n" +
                "declare y1 int \n" +
                "declare z int";
        assertEquals(true, new MyParser().parse(program));
    }

    // Declare multiple variables, the first character of the variable name must be a letter; "2w" will fail
    @Test
    void declTestTwo() {
        String program = "declare 2w int\n" +
                "declare x int \n" +
                "declare y1 int \n" +
                "declare z int";
        assertEquals(false, new MyParser().parse(program));
    }

    // Stop declarations of two or more variables with the same variable name; we try to declare "x" twice, but it will fail
    @Test
    void declTestThree() {
        String program = "declare x int\n" +
                "declare y int \n" +
                "declare y1 int \n" +
                "declare x int";
        assertEquals(false, new MyParser().parse(program));
    }

    // Print test for an undeclared variable
    @Test
    void printTestOne() {
        String program = "declare x int\n" +
                "print y";
        assertEquals(false, new MyParser().parse(program));
    }

    // Print test for an undefined variable
    @Test
    void printTestTwo() {
        String program = "declare x int\n" +
                "print x";
        assertEquals(false, new MyParser().parse(program));
    }

    // Print test for a declared and set variable
    @Test
    void printTestThree() {
        String program = "declare x int\n" +
                "set x = 10\n" +
                "print x";
        assertEquals(true, new MyParser().parse(program));
    }

    // Set test for a declared variable
    @Test
    void setTestOne() {
        String program = "declare x int\n" +
                "set x = 10";
        assertEquals(true, new MyParser().parse(program));
    }

    // Set test for an undeclared variable
    @Test
    void setTestTwo() {
        String program = "declare x int\n" +
                "set y = 10";
        assertEquals(false, new MyParser().parse(program));
    }

    // If test parsing an If statement if when an if statement is either true or false
    @Test
    void ifTestOne() {
        String program = "declare x int\n" +
                "declare y int\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "if x = y then\n" +
                " print x\n" +
                " print x\n" +
                " print x\n" +
                " print x\n" +
                " print x\n" +
                "set y = 10\n" +
                " endif\n" +
                "set x = 15\n" +
                "if x = y then\n" +
                " print x\n" +
                " print y\n" +
                " endif";
        assertEquals(true, new MyParser().parse(program));
    }

    // If test if an If Statement is never closed! (Match error)
    @Test
    void ifTestTwo() {
        String program = "declare x int\n" +
                "declare y int\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "if x = y then\n" +
                " print x\n" +
                " print y";
        assertEquals(false, new MyParser().parse(program));
    }

    // If test parsing NESTED If statements
    @Test
    void ifTestThree() {
        String program = "declare x int\n" +
                "declare y int\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "if x = y then\n" +
                " print x\n" +
                " print x\n" +
                " print x\n" +
                " print x\n" +
                " print x\n" +
                "set y = 10\n" +
                "if x = y then\n" +
                " print y\n" +
                " print y\n" +
                " print y\n" +
                " print y\n" +
                " print y\n" +
                " calc y = x + y" +
                " print y\n" +
                " endif\n" +
                " endif\n" + // notice that we put an if statement inside an if statement
                "set x = 20\n" +
                "set y = 20\n" +
                "if x = y then\n" +
                " print x\n" +
                " print y\n" +
                " endif";
        assertEquals(true, new MyParser().parse(program));
    }

    // If test multi nested if statement
    @Test
    void ifTestFour() {
        String program = "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set x = 1\n" +
                "set y = 1\n" +
                "set z = 1\n" +
                "if x = y then\n" +
                "if y = z then\n" +
                "if z = x then\n" +
                "if y = y then\n" +
                " print x" +
                " print y" +
                " print z" +
                " endif\n" +
                " endif\n" +
                " endif\n" +
                " endif\n";
        assertEquals(true, new MyParser().parse(program));
    }

    // Calc test <Sum> Recursion
    @Test
    void calcTestOne() {
        String program = "declare w int\n" +
                "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "set z = 20\n" +
                "calc w = x + y + z\n" +
                "calc x = x + y\n" +
                "calc z = x + y + w + z + x\n" +
                " print w\n" +
                " print x\n" +
                " print z";
        assertEquals(true, new MyParser().parse(program));
    }

    // Calc test undefined ID variable
    @Test
    void calcTestTwo() {
        String program = "declare w int\n" +
                "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "calc z = x + y + w + x\n" +
                " print z";
        assertEquals(true, new MyParser().parse(program));
    }

    // Calc test undefined ID variable and this ID is being used in the calculation
    @Test
    void calcTestThree() {
        String program = "declare w int\n" +
                "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "calc z = x + y + w + x + z\n" +
                " print z";
        assertEquals(false, new MyParser().parse(program));
    }

    @Test
    void testSampleProgramOne() {
        String program = "declare w int\n" +
                "declare x int\n" +
                "declare y int\n" +
                "declare z int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "set z = 20\n" +
                "calc w = x + y + z\n" +
                "if x = y then\n" +
                " print w\n" +
                " print x\n" +
                "endif\n" +
                "print y\n" +
                "print z";
        // Should return NO syntax error
        assertEquals(true, new MyParser().parse(program));
    }

    @Test
    void testSampleProgramTwo() {
        String program = "declare w int\n" +
                "declare x int\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "if w x then\n" +
                " print w\n" +
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