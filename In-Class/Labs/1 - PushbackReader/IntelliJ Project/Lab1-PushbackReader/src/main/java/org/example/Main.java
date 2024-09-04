package org.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        usePushbackReader();
    }

    private static void usePushbackReader() {
        FileReader fr = null;
        PushbackReader pbr = null;
        try {
            fr = new FileReader("input.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pbr = new PushbackReader(fr);
        int readCharInt = 0;
        while(readCharInt != -1) {
            try {
                readCharInt = pbr.read();
                if(readCharInt != -1) {
                    char readChar = (char) readCharInt;
                    showMessagesForEachCharacterCategory(pbr, readChar);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            pbr.close();
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showMessagesForEachCharacterCategory(PushbackReader pbr, char readChar) {
        if (Character.isLetter(readChar)) {
            StringBuilder buffer = new StringBuilder().append(readChar);
            while (Character.isLetter(readChar)) {
                try {
                    readChar = (char) pbr.read();
                    buffer.append(readChar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(buffer.toString() + " String");
        }
        else if (Character.isDigit(readChar)) {
            StringBuilder buffer = new StringBuilder().append(readChar);
            while (Character.isLetter(readChar)) {
                try {
                    readChar = (char) pbr.read();
                    buffer.append(readChar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(buffer.toString() + " Digit");
        }
        else if (Character.isSpaceChar(readChar)) {
            StringBuilder buffer = new StringBuilder().append(readChar);
            while (Character.isLetter(readChar)) {
                try {
                    readChar = (char) pbr.read();
                    buffer.append(readChar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(buffer.toString() + " Space");
        }
        else if (readChar == ':') {
            StringBuilder buffer = new StringBuilder().append(readChar);
            while (Character.isLetter(readChar)) {
                try {
                    readChar = (char) pbr.read();
                    buffer.append(readChar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(buffer.toString() + " Colon");
        }
        else if (readChar == ',') {
            StringBuilder buffer = new StringBuilder().append(readChar);
            while (Character.isLetter(readChar)) {
                try {
                    readChar = (char) pbr.read();
                    buffer.append(readChar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(buffer.toString() + " Comma");
        }
        else if (readChar == '\"') {
            StringBuilder buffer = new StringBuilder().append(readChar);
            while (Character.isLetter(readChar)) {
                try {
                    readChar = (char) pbr.read();
                    buffer.append(readChar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(buffer.toString() + " Double Quote");
        }
    }

}