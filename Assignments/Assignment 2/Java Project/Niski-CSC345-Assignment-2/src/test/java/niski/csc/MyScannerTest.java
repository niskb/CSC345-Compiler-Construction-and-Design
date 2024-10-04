package niski.csc;

import org.junit.jupiter.api.Test;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class MyScannerTest {

    @Test
    void testFirst() {
        // Arrange
        String program = "declare x int";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.DECLARE, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.INTDATATYPE, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testSecond() {
        String program = "set x = 5";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.SET, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.EQUALS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.INTLITERAL, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testThird() {
        String program = "calc x + y";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.CALC, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.PLUS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testFourth() {
        String program = "print x";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.PRINT, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testFifth() {
        String program = "if x = y then \n endif";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.IF, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.EQUALS, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ID, myScanner.scan());
            assertEquals(MyScanner.TOKEN.THEN, myScanner.scan());
            assertEquals(MyScanner.TOKEN.ENDIF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testSixth() {
        String program = "if x = y then\n print x \n endif";
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

    @Test
    void testSeventh() {
        String program = "";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(program)));
        try {
            assertEquals(MyScanner.TOKEN.SCANEOF, myScanner.scan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(myScanner.getTokenBufferString());
    }

}