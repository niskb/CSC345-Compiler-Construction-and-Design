package niski.csc;

import java.io.IOException;
import java.io.PushbackReader;

public class LabScanner {

    enum TOKEN {PLUS, MINUS, ID, EOF}

    ;

    private PushbackReader pushbackReader;

    public LabScanner(PushbackReader pushbackReader) {
        this.pushbackReader = pushbackReader;
    }

    private int readNextChar() {
        try {
            return pushbackReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isWhiteSpace(int c) {
        if ((c == 32) || (c == 9) || (c == 10) || (c == 13)) {
            return true;
        }
        return false;
    }

    public TOKEN scan() {
        int c = readNextChar();
        while (c != -1) {
            if (isWhiteSpace(c)) {
                c = readNextChar();
                continue;
            }
            if (c == '+') {
                return TOKEN.PLUS;
            }
            if (c == '-') {
                return TOKEN.MINUS;
            }
            if(Character.isLetter(c)) {
                c = readNextChar();
                while (Character.isLetter(c) || Character.isDigit(c)) {
                    c = readNextChar();
                }
                try {
                    pushbackReader.unread(c);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return TOKEN.ID;
            }
        } // end of while
        return TOKEN.EOF;
    }

}
