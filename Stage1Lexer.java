import java.util.*;

public class Stage1Lexer {
    public List<String> analyze(String input) {
        System.out.println("======STAGE1: COMPILER TECHNIQUES--> LEXICAL ANALYSIS-Scanner");
        System.out.println("SYMBOL TABLE COMPRISING ATTRIBUTES AND TOKENS:\n");
        Set<String> KEYWORDS = Set.of("BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END");
        Set<String> OPERATORS = Set.of("+", "-", "*", "/");
        StringTokenizer st = new StringTokenizer(input, "+-*/=;, ", true);
        List<String> tokens = new ArrayList<>();
        String prev = null;
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (t.trim().isEmpty() || t.equals(",")) continue;
            if (prev != null && OPERATORS.contains(prev) && OPERATORS.contains(t)) {
                System.out.printf("SYNTAX ERROR – combined operators '%s%s' not allowed%n", prev, t);
            }
            tokens.add(t);
            prev = t;
        }
        if (input.trim().endsWith(";")) {
            System.out.println("SYNTAX ERROR – semicolon ';' at end of line not allowed");
        }
        for (int i = 0; i < tokens.size(); i++) {
            String tok = tokens.get(i);
            String type;
            if (tok.matches("\\d")) {
                System.out.printf("SYNTAX ERROR – numbers not allowed: '%s'%n", tok);
                type = "Invalid";
            } else if (OPERATORS.contains(tok)) {
                type = "Operator";
            } else if ("=".equals(tok) || ";".equals(tok)) {
                type = "Symbol";
            } else if (tok.matches("[A-Z]{2,}")) {
                if (KEYWORDS.contains(tok)) type = "Keyword";
                else {
                    System.out.printf("LEXICAL ERROR – keyword '%s' not recognized%n", tok);
                    type = "Invalid";
                }
            } else if (tok.matches("[A-Za-z]") || tok.matches("[a-z]+")) {
                type = "Identifier";
            } else {
                System.out.printf("SYNTAX ERROR – invalid character '%s'%n", tok);
                type = "Invalid";
            }
            System.out.printf("TOKEN#%d  %-7s %s%n", i + 1, tok, type);
        }
        System.out.println();
        return tokens;
    }
}
