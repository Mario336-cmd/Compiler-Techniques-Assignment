import java.util.*;

public class CompilerMain {
    private static final Set<String> KEYWORDS =
            Set.of("BEGIN", "INTEGER", "LET", "INPUT", "WRITE", "END");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Stage1Lexer lexer = new Stage1Lexer();
        Stage2Parser parser = new Stage2Parser();
        Stage3SemanticAnalyzer semantic = new Stage3SemanticAnalyzer();
        Stage4ICRGenerator icrGen = new Stage4ICRGenerator();
        Stage5CodeGenerator codeGen = new Stage5CodeGenerator();
        Stage6Optimizer optimizer = new Stage6Optimizer();
        Stage7TMCGenerator tmcGen = new Stage7TMCGenerator();

        String mode;
        while (true) {
            System.out.println("Select mode: 1) Iterative (line-by-line)   2) Batch (all at once)");
            mode = scanner.nextLine().trim();
            if (mode.equals("1") || mode.equals("2")) break;
        }

        if (mode.equals("1")) {
            int lineNum = 1;
            while (true) {
                System.out.print("Enter Line " + lineNum + " (or 99 to quit): ");
                String input = scanner.nextLine().trim();
                if (input.equals("99")) break;
                System.out.println("Line " + lineNum++ + ": " + input);

                List<String> tokens = lexer.analyze(input);

                if (!tokens.isEmpty()
                        && KEYWORDS.contains(tokens.get(0))
                        && !tokens.contains("=")) {
                    tmcGen.generateRaw(tokens);
                    System.out.println("======END OF COMPILATION======\n");
                    continue;
                }

                int eq = tokens.indexOf("=");
                List<String> exprTokens;
                String expr;
                if (eq != -1) {
                    exprTokens = tokens.subList(eq + 1, tokens.size());
                    expr = input.substring(input.indexOf('=') + 1).trim();
                } else {

                    exprTokens = tokens;
                    expr = input;
                }

                Stage2Parser.DerivationType dt = chooseDerivation(scanner);
                if (!parser.parse(exprTokens, dt)) continue;
                if (!semantic.analyze(exprTokens)) continue;

                List<String> icr = icrGen.generate(expr);
                codeGen.generate(icr);
                List<String> opt = optimizer.optimize(icr);
                tmcGen.generate(opt);

                System.out.println("======END OF COMPILATION======\n");
            }

        } else {
            List<String> inputs = new ArrayList<>();
            int lineNum = 1;
            while (true) {
                System.out.print("Enter Line " + lineNum + " (or 99 to finish): ");
                String input = scanner.nextLine().trim();
                if (input.equals("99")) break;
                inputs.add(input);
                lineNum++;
            }

            lineNum = 1;
            for (String input : inputs) {
                System.out.println("Line " + lineNum++ + ": " + input);

                List<String> tokens = lexer.analyze(input);

                if (!tokens.isEmpty()
                        && KEYWORDS.contains(tokens.get(0))
                        && !tokens.contains("=")) {
                    tmcGen.generateRaw(tokens);
                    System.out.println("======END OF COMPILATION======\n");
                    continue;
                }

                int eq = tokens.indexOf("=");
                List<String> exprTokens;
                String expr;
                if (eq != -1) {
                    exprTokens = tokens.subList(eq + 1, tokens.size());
                    expr = input.substring(input.indexOf('=') + 1).trim();
                } else {
                    exprTokens = tokens;
                    expr = input;
                }

                Stage2Parser.DerivationType dt = chooseDerivation(scanner);
                if (!parser.parse(exprTokens, dt)) continue;
                if (!semantic.analyze(exprTokens)) continue;

                List<String> icr = icrGen.generate(expr);
                codeGen.generate(icr);
                List<String> opt = optimizer.optimize(icr);
                tmcGen.generate(opt);

                System.out.println("======END OF COMPILATION======\n");
            }
        }

        scanner.close();
        System.out.println("Exiting Compiler...");
    }

    private static Stage2Parser.DerivationType chooseDerivation(Scanner scanner) {
        while (true) {
            System.out.println("Choose derivation type: 1) Leftmost (top-down)   2) Rightmost (bottom-up)");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) return Stage2Parser.DerivationType.LEFTMOST;
            if (choice.equals("2")) return Stage2Parser.DerivationType.RIGHTMOST;
        }
    }
}
