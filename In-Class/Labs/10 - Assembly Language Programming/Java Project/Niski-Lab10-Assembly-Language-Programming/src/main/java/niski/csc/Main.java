package niski.csc;

import program.PseudoAssemblyWithStringProgram;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String fileName = args[0];
        String code = "";
        try {
            FileReader fr = new FileReader(fileName);
            Scanner infile = new Scanner(fr);
            while (infile.hasNext()) {
                code += infile.nextLine();
                code += "\n";
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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