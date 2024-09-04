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
                    showMessagesForEachCharacterCategory(readChar);
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

    private static void showMessagesForEachCharacterCategory(char readChar) {
        if (Character.isLetter(readChar)) {
            System.out.printf("%c %s\n", readChar, "Letter");
        }
        if (Character.isDigit(readChar)) {
            System.out.printf("%c %s\n", readChar, "Digit");
        }
        if (Character.isSpaceChar(readChar)) {
            System.out.printf("%c %s\n", readChar, "Space");
        }
        if (readChar == ':') {
            System.out.printf("%c %s\n", readChar, "Colon");
        }
        if (readChar == ',') {
            System.out.printf("%c %s\n", readChar, "Comma");
        }
        if (readChar == '\"') {
            System.out.printf("%c %s\n", readChar, "Double quote");
        }
    }

}