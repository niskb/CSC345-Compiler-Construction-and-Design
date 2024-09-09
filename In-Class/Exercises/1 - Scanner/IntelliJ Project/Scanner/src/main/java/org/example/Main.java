package org.example;

import java.io.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        enum TOKEN {NUM, PLUS, EOF}
        PushbackReader pbr = null;
        TOKEN nextToken = null;
        try {
            pbr = new PushbackReader(new FileReader(new File("input.txt")));
            int c;
            c = pbr.read();

            //Keep reading until end of file
            while (c != -1) {
                // Figure out token type then return the token
                if (Character.isDigit(c)) {
                    nextToken = TOKEN.NUM;
                    // do inner loop and add to buffer
                } else if ((char) c == '+') {
                    nextToken = TOKEN.NUM;
                } else {
                    if (null != nextToken) {
                        // Print the buffer
                        System.out.println(nextToken);
                    }
                }
                c = pbr.read();
            }
            // Print last character token
            System.out.println(nextToken);
            // Set token to end of file
            nextToken = TOKEN.EOF;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isWhiteSpace(int c) {
        return ((c == 32) || (c == 9) || (c == 10) || (c == 13));
    }

}