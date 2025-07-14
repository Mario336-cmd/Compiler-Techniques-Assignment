import java.util.*;

public class Stage2Parser {
    public enum DerivationType {LEFTMOST, RIGHTMOST}

    private static class DerivationStep {
        String line, rule;

        DerivationStep(String l, String r) {
            line = l;
            rule = r;
        }
    }

    public boolean parse(List<String> tokens, DerivationType type) {
        System.out.println("======STAGE2: COMPILER TECHNIQUES--> SYNTAX ANALYSIS-Parser");
        String input = String.join("", tokens);
        System.out.println("GET THE DERIVATION FOR : " + input + "\n");

        List<String> errors = new ArrayList<>();
        int e = 0;

        for (int i = 0; i + 1 < tokens.size(); i++) {
            if ("+-*/".contains(tokens.get(i)) && "+-*/".contains(tokens.get(i + 1))) {
                errors.add("#" + (++e) + " SYNTAX ERROR – combined operators '"
                        + tokens.get(i) + tokens.get(i + 1) + "' not allowed");
            }
        }

        if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).equals(";")) {
            errors.add("#" + (++e) + " SYNTAX ERROR – semicolon ';' at end of line not allowed");
        }

        for (String t : tokens) {
            if (t.matches("\\d+")) {
                errors.add("#" + (++e) + " SYNTAX ERROR – numbers not allowed: '" + t + "'");
            }
        }

        for (String t : tokens) {
            if (!t.matches("[A-Za-z]") && !"+-*/".contains(t)
                    && !"=".equals(t) && !";".equals(t)) {
                errors.add("#" + (++e) + " SYNTAX ERROR – invalid token '" + t + "'");
            }
        }

        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i);
            if (t.length() == 1 && "%$&<>;".contains(t)) {
                errors.add("#" + (++e) + " SEMANTIC ERROR – symbol '" + t + "' not allowed at position " + (i + 1));
            }
        }

        if (!errors.isEmpty()) {
            errors.forEach(System.out::println);
            System.out.println();
            return false;
        }

        List<String> ops = new ArrayList<>(), ids = new ArrayList<>();
        for (String t : tokens) {
            if ("+-*/".contains(t)) ops.add(t);
            else if (t.matches("[A-Za-z]")) ids.add(t);
        }

        List<DerivationStep> steps = new ArrayList<>();
        steps.add(new DerivationStep("E -> E", "Rule1 (R1)"));
        String seq = "E";
        for (String op : ops) {
            seq += op + "E";
            steps.add(new DerivationStep("E -> " + seq, "Rule2 (R2)"));
        }
        for (int i = 0; i < ids.size(); i++) {
            int idx = Character.toUpperCase(ids.get(i).charAt(0)) - 'A' + 1;
            seq = replaceNth(seq, "E", "E" + idx, i);
            steps.add(new DerivationStep("E -> " + seq, "Rule3 (R3)"));
        }
        for (String id : ids) {
            int idx = Character.toUpperCase(id.charAt(0)) - 'A' + 1;
            seq = seq.replaceFirst("E" + idx, id);
            steps.add(new DerivationStep("E -> " + seq, "Rule4 (R4)"));
        }
        if (type == DerivationType.RIGHTMOST) Collections.reverse(steps);
        steps.forEach(s -> System.out.printf("%-50s%s%n", s.line, s.rule));
        System.out.println();
        return true;
    }

    private int nthIndex(String s, String sub, int n) {
        int pos = -1, from = 0;
        for (int i = 0; i <= n; i++) {
            pos = s.indexOf(sub, from);
            if (pos < 0) return -1;
            from = pos + sub.length();
        }
        return pos;
    }

    private String replaceNth(String s, String find, String rep, int n) {
        int p = nthIndex(s, find, n);
        if (p < 0) return s;
        return s.substring(0, p) + rep + s.substring(p + find.length());
    }
}
