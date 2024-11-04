package org.example;

import program.PseudoAssemblyWithStringProgram;

import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        String code = "";
        code += ".data\n";
        code += "var int x\n";
        code += ".code\n";
        code += "loadintliteral ri1, 88\n";
        code += "storeintvar ri1, x\n";
        code += "printi x\n";
        code += "printi ri1\n";
        int numVirtualRegistersInt = 32;
        int numVirtualRegistersString = 32;
        String outputClassName = "MyLabProgram";
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
        }
    }
}