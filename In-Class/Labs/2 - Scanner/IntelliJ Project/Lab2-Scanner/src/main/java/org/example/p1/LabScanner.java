package org.example.p1;

import java.io.IOException;
import java.io.PushbackReader;

public class LabScanner {

    /**
     * TOKENS
     */
    public enum TOKEN {PLUS, MINUS, INTLITERAL, EOF}

    /**
     * Input Stream
     */
    private PushbackReader inputPBR = null;

    /**
     * Initialize the PushbackReader
     */
    public void setInputPBR(PushbackReader inputPBR) {
        this.inputPBR = inputPBR;
    }

    /**
     * Read next character in stream, skips white spaces
     *
     * @return
     */
    private int readNextChar() {
        try {
            return inputPBR.read();
        } catch (IOException e) {
            return -1;
        }
    }

    private void unread(int c) {
        try {
            inputPBR.unread(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isWhiteSpace(int c) {
        return (c == 32) || (c == 9) || (c == 10) || (c == 13);
    }

    public TOKEN scan() {
        int c;
        c = readNextChar();
        while (c != -1) {
            if (isWhiteSpace(c)) {
                c = readNextChar();
                continue;
            } else if (Character.isDigit(c)) {
                c = readNextChar();
                while (Character.isDigit(c)) {
                    c = readNextChar();
                }
                // Put the char back in the input stream
                unread(c);
                return TOKEN.INTLITERAL;
            }
        }
        return TOKEN.EOF;
    }
}
