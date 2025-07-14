import java.util.*;

public class Stage3SemanticAnalyzer {
    public boolean analyze(List<String> tokens) {
        System.out.println("======STAGE3: COMPILER TECHNIQUES--> SEMANTIC ANALYSIS");
        Set<Character> FORBIDDEN = Set.of('%', '$', '&', '<', '>', ';');
        int e = 0;
        for (int i = 0; i < tokens.size(); i++) {
            String t = tokens.get(i);
            if (t.length() == 1 && FORBIDDEN.contains(t.charAt(0))) {
                System.out.printf("#%d SEMANTIC ERROR – symbol '%s' not allowed at position %d%n", ++e, t, i + 1);
            }
        }
        if (e > 0) {
            System.out.println();
            return false;
        }
        String expr = String.join("", tokens);
        System.out.println("CONCLUSION --> This expression: " + expr + " is Syntactically and Semantically correct\n");
        return true;
    }
}
