package niski.csc;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Queue<Parser.TOKEN> program = new LinkedList<>();
        program.add(Parser.TOKEN.ID);
        program.add(Parser.TOKEN.PLUS);
        program.add(Parser.TOKEN.ID);
        program.add(Parser.TOKEN.PLUS);
        program.add(Parser.TOKEN.ID);
        program.add(Parser.TOKEN.PLUS);
        program.add(Parser.TOKEN.ID);

        Parser parser = new Parser();
        parser.parse(program);
    }
}