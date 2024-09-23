package niski.csc;

import org.junit.jupiter.api.Test;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class MyScannerTest {

    @Test
    void scanFirst() {
        // Arrange
        String testString = "declare x int";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // First Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // First Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.DECLARE), String.valueOf(myScanner.getTokenBufferString()));

        // Second Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Second Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.DECLARE + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Third Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Third Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.DECLARE + MyScanner.TOKEN.ID + MyScanner.TOKEN.INTDATATYPE), String.valueOf(myScanner.getTokenBufferString()));
    }

    @Test
    void scanSecond() {
        // Arrange
        String testString = "set x = 5";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // First Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // First Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.SET), String.valueOf(myScanner.getTokenBufferString()));

        // Second Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Second Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.SET + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Third Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Third Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.SET + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS), String.valueOf(myScanner.getTokenBufferString()));

        // Fourth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Fourth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.SET + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.INTLITERAL), String.valueOf(myScanner.getTokenBufferString()));
    }

    @Test
    void scanThird() {
        // Arrange
        String testString = "calc x + y";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // First Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // First Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.CALC), String.valueOf(myScanner.getTokenBufferString()));

        // Second Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Second Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.CALC + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Third Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Third Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.CALC + MyScanner.TOKEN.ID + MyScanner.TOKEN.PLUS), String.valueOf(myScanner.getTokenBufferString()));

        // Fourth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Fourth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.CALC + MyScanner.TOKEN.ID + MyScanner.TOKEN.PLUS + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));
    }

    @Test
    void scanFourth() {
        // Arrange
        String testString = "print x";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // First Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // First Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.PRINT), String.valueOf(myScanner.getTokenBufferString()));

        // Second Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Second Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.PRINT + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));
    }

    @Test
    void scanFifth() {
        // Arrange
        String testString = "if x = y then \n endif";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // First Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // First Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF), String.valueOf(myScanner.getTokenBufferString()));

        // Second Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Second Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Third Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Third Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS), String.valueOf(myScanner.getTokenBufferString()));

        // Fourth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Fourth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Fifth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Fifth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.ID + MyScanner.TOKEN.THEN), String.valueOf(myScanner.getTokenBufferString()));

        // We are in the if statement, so the code "jumps" into another set of curly braces.

        // Sixth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Sixth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.ID + MyScanner.TOKEN.THEN + MyScanner.TOKEN.ENDIF), String.valueOf(myScanner.getTokenBufferString()));
    }

    @Test
    void scanSixth() {
        // Arrange
        String testString = "if x = y then \n print x \n endif";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // First Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // First Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF), String.valueOf(myScanner.getTokenBufferString()));

        // Second Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Second Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Third Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Third Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS), String.valueOf(myScanner.getTokenBufferString()));

        // Fourth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Fourth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.ID), String.valueOf(myScanner.getTokenBufferString()));

        // Fifth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Fifth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.ID + MyScanner.TOKEN.THEN), String.valueOf(myScanner.getTokenBufferString()));

        // We are in the if statement, so the code "jumps" into another set of curly braces.

        // Sixth Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Sixth Assert
        assertEquals(String.valueOf("" + MyScanner.TOKEN.IF + MyScanner.TOKEN.ID + MyScanner.TOKEN.EQUALS + MyScanner.TOKEN.ID + MyScanner.TOKEN.THEN + MyScanner.TOKEN.PRINT + MyScanner.TOKEN.ID + MyScanner.TOKEN.ENDIF), String.valueOf(myScanner.getTokenBufferString()));
    }

    @Test
    void scanSeventh() {
        // Arrange
        String testString = "";
        MyScanner myScanner = new MyScanner(new PushbackReader(new StringReader(testString)));

        // Act
        try {
            myScanner.scan();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertEquals(String.valueOf(MyScanner.TOKEN.SCANEOF), String.valueOf(myScanner.getTokenBufferString()));

    }
}