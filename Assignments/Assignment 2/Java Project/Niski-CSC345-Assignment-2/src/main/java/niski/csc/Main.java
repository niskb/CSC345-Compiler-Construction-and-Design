/**
 * Brian Niski
 */

package niski.csc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        String program = "";
        try {
            program = Files.readString(Path.of("input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean parse = new MyParser().parse(program);
        System.out.println("Result: " + parse);
    }
}