/**
 * Brian Niski
 */

package niski.csc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    /**
     * Main method to run your own program
     * You may load your own program by entering it in the input.txt file!
     * My MyScanner Class has been rewritten for it to work with this and future homework assignments
     * @param args
     */
    public static void main(String[] args) {
        String program = "";
        try {
            program = Files.readString(Path.of("input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean parse = new MyParser().parse(program);
        System.out.println("Result: " + parse + "\n");
    }

}