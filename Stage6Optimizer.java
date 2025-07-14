import java.util.*;
import java.util.regex.*;

public class Stage6Optimizer {
    public List<String> optimize(List<String> icr) {
        System.out.println("======STAGE6: CODE OPTIMISATION (CO)");
        System.out.println("WE USE: LDA, MUL, ADD, SUB, STR, BODMAS\n");

        List<String> opt = new ArrayList<>();
        Pattern p = Pattern.compile("(t\\d+) = (.+?) ([+\\-*/]) (.+)");
        for (String instr : icr) {
            Matcher m = p.matcher(instr);
            if (!m.matches()) continue;
            String dest = m.group(1);
            String l = m.group(2);
            String op = m.group(3);
            String r = m.group(4);

            String mn = switch (op) {
                case "*" -> "MUL";
                case "/" -> "DIV";
                case "+" -> "ADD";
                default -> "SUB";
            };
            opt.add(String.format("%s %s, %s, %s", mn, dest, r, l));
        }

        opt.forEach(System.out::println);
        System.out.println();
        return opt;
    }
}
