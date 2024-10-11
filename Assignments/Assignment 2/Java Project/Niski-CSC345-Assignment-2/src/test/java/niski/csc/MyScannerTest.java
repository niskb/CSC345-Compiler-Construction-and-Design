package niski.csc;

import org.junit.jupiter.api.Test;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class MyScannerTest {

    @Test
    void testFirst() {
        System.out.println("Test One:");
        String program = "declare x int";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.DECLARE, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.INTDATATYPE, myScanner.scan());
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testSecond() {
        System.out.println("Test Two:");
        String program = "set x = 5";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.SET, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.EQUALS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.INTLITERAL, myScanner.scan());
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testThird() {
        System.out.println("Test Three:");
        String program = "calc x + y";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.CALC, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.PLUS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testFourth() {
        System.out.println("Test Four:");
        String program = "print x";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.PRINT, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testFifth() {
        System.out.println("Test Five:");
        String program = "if x = y then \n endif";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.IF, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.EQUALS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.THEN, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ENDIF, myScanner.scan());
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testSixth() {
        System.out.println("Test Six:");
        String program = "if x = y then\n print x \n endif";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.IF, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.EQUALS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.THEN, myScanner.scan());
            assertEquals(MyScanner.TOKEN.PRINT, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ENDIF, myScanner.scan());
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testSeventh() {
        System.out.println("Test Seven:");
        String program = "";
        System.out.println(program);
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

}