/**
 * Brian Niski
 */

package niski.csc;

import program.PseudoAssemblyWithStringProgram;

import java.io.IOException;
import java.io.PrintStream;
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
        MyParser myParser = new MyParser();
        boolean parse = myParser.parse(program);
        System.out.println("Result: " + parse + "\n");
        if (parse) {
            String code = myParser.abstractSyntaxTree.getCode();
            run(code);
        }
    }

    private static void run(String code) {
        int numVirtualRegistersInt = 32;
        int numVirtualRegistersString = 32;
        String outputClassName = "MyProgram1";
        String outputPackageNameDot = "mypackage";
        String classRootDir = System.getProperty("user.dir") + "/" + "target/classes";
        PseudoAssemblyWithStringProgram pseudoAssemblyWithStringProgram = new
                PseudoAssemblyWithStringProgram(
                code,
                outputClassName,
                outputPackageNameDot,
                classRootDir,
                numVirtualRegistersInt,
                numVirtualRegistersString
        );
        boolean parseSuccessful;
        parseSuccessful = pseudoAssemblyWithStringProgram.parse();
        if (parseSuccessful == true) {
            // Creates a Java bytecode class file
            pseudoAssemblyWithStringProgram.generateBytecode();
            // Run the Java bytecode class file and show output on the console
            PrintStream outstream = new PrintStream(System.out);
            pseudoAssemblyWithStringProgram.run(outstream);
        } else {
            String messages = pseudoAssemblyWithStringProgram.getAllParseMessages();
            System.out.println(messages);
        }
    }

}