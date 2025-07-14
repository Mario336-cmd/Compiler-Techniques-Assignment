import java.util.*;
import java.util.regex.*;

public class Stage5CodeGenerator {
    public List<String> generate(List<String> icr) {
        System.out.println("======STAGE5: CODE GENERATION (CG)");
        System.out.println("WE USE: LDA, MUL, ADD, SUB, STR\n");

        List<String> code = new ArrayList<>();
        Pattern p = Pattern.compile("(.+?) = (.+?) ([+\\-*/]) (.+)");
        for (String instr : icr) {
            Matcher m = p.matcher(instr);
            if (!m.matches()) continue;
            String dest = m.group(1).trim();
            String l = m.group(2).trim();
            String op = m.group(3);
            String r = m.group(4).trim();

            code.add("LDA " + l);
            switch (op) {
                case "*":
                    code.add("MUL " + r);
                    break;
                case "/":
                    code.add("DIV " + r);
                    break;
                case "+":
                    code.add("ADD " + r);
                    break;
                default:
                    code.add("SUB " + r);
            }
            code.add("STR " + dest);
        }

        code.forEach(System.out::println);
        System.out.println();
        return code;
    }
}
