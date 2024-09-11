package org.example;

import org.example.p1.LabScanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PushbackReader;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        LabScanner labScanner = new LabScanner(); // No PushbackReader initialized
        try {
            PushbackReader pbr = new PushbackReader(new FileReader("input.txt"));
            labScanner.setInputPBR(pbr);
            // Now we can operate the labScanner
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        LabScanner.TOKEN token;
        token = labScanner.scan();
        while (token != LabScanner.TOKEN.EOF) {
            System.out.println(token);
            token = labScanner.scan();
        }

    }
}