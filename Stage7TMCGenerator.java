import java.util.*;

public class Stage7TMCGenerator {
    public void generate(List<String> opt) {
        System.out.println("======STAGE7: TARGET MACHINE CODES (TMC)");
        for (String instr : opt) {
            String[] tok = instr.split("[ ,]+");
            List<String> binaries = new ArrayList<>();
            for (String t : tok) {
                char c = Character.toUpperCase(t.charAt(0));
                binaries.add(to8(c));
            }
            System.out.println(String.join(" ", binaries));
        }
        System.out.println();
    }

    public void generateRaw(List<String> tokens) {
        System.out.println("======STAGE7: TARGET MACHINE CODES (TMC)");
        List<String> binaries = new ArrayList<>();
        for (String t : tokens) {
            char c = Character.toUpperCase(t.charAt(0));
            binaries.add(to8(c));
        }
        System.out.println(String.join(" ", binaries));
        System.out.println();
    }

    private String to8(char c) {
        return String.format("%8s", Integer.toBinaryString(c & 0xFF))
                .replace(' ', '0');
    }
}
