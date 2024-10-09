package niski.csc;

import java.io.*;

public class LabParser {

    private LabScanner scanner;
    private LabScanner.TOKEN nextToken;

    public void parse(String filename) {
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            PushbackReader pbr = new PushbackReader(fr);
            scanner = new LabScanner(pbr);
            getNextToken();
            expr();
            if (match(LabScanner.TOKEN.EOF)) {
                System.out.println("Parsing successful");
            } else {
                System.out.println("Parsing failed");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void getNextToken() {
        nextToken = scanner.scan();
    }

    private boolean match(LabScanner.TOKEN expectedToken) {
        if(nextToken == expectedToken) {
            getNextToken();
            return true;
        }
        System.out.println("TOKEN: " + nextToken + "\nEXPECTED: " + expectedToken);
        return false;
    }

    private void expr() {
        fact();
        exprEnd();
    }

    private void exprEnd() {
        if(nextToken == LabScanner.TOKEN.PLUS) {
            if(!match(LabScanner.TOKEN.PLUS)) {
                System.out.println("Parse error:");
                System.exit(0);
            }
        } else if(nextToken == LabScanner.TOKEN.MINUS) {
            if(!match(LabScanner.TOKEN.MINUS)) {
                System.out.println("Parse error:");
                System.exit(0);
            }
        }
        if(!match(LabScanner.TOKEN.ID)) {
            System.out.println("Parse error:");
            System.exit(0);
        }
    }

    private void fact() {
        if(!match(LabScanner.TOKEN.ID)) {
            System.out.println("Parse error:");
            System.exit(0);
        }
    }

}
