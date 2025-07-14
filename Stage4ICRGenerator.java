import java.util.*;

public class Stage4ICRGenerator {
    public List<String> generate(String expr) {
        String clean = expr.endsWith(";") ? expr.substring(0, expr.length() - 1) : expr;
        System.out.println("======STAGE4: INTERMEDIATE CODE REPRESENTATION (ICR)");
        System.out.println("INPUT STRING: " + clean);
        System.out.println("BODMAS\n");

        StringTokenizer st = new StringTokenizer(clean, "+-*/", true);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String t = st.nextToken().trim();
            if (!t.isEmpty()) tokens.add(t);
        }

        List<String> icr = new ArrayList<>();
        int tCount = 1;
        boolean done;
        do {
            done = false;
            for (int i = 0; i < tokens.size(); i++) {
                String op = tokens.get(i);
                if ("*/".contains(op)) {
                    String l = tokens.get(i - 1), r = tokens.get(i + 1);
                    String tmp = "t" + tCount++;
                    icr.add(tmp + " = " + l + " " + op + " " + r);
                    tokens.set(i - 1, tmp);
                    tokens.remove(i + 1);
                    tokens.remove(i);
                    done = true;
                    break;
                }
            }
        } while (done);

        for (int i = 0; i < tokens.size(); i++) {
            String op = tokens.get(i);
            if ("+-".contains(op)) {
                String l = tokens.get(i - 1), r = tokens.get(i + 1);
                String tmp = "t" + tCount++;
                icr.add(tmp + " = " + l + " " + op + " " + r);
                tokens.set(i - 1, tmp);
                tokens.remove(i + 1);
                tokens.remove(i);
                i = -1;
            }
        }

        List<String> temps = new ArrayList<>();
        for (int i = 1; i < tCount; i++) temps.add("t" + i);
        System.out.println("TEMPORARY VARIABLES: WE USE: " +
                String.join(", ", temps) + "\n");

        icr.forEach(System.out::println);
        System.out.println();
        return icr;
    }
}
