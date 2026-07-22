package ui;

/** Shared console formatting helpers so every screen looks consistent. */
final class ConsoleUtil {

    private ConsoleUtil() { }

    static final String LINE = "==============================================";
    static final String SUB_LINE = "----------------------------------------------";

    static void printHeader(String title) {
        System.out.println();
        System.out.println(LINE);
        System.out.println(center(title));
        System.out.println(LINE);
    }

    static void printSubHeader(String title) {
        System.out.println();
        System.out.println(SUB_LINE);
        System.out.println(title);
        System.out.println(SUB_LINE);
    }

    static void printSuccess(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }

    static void printError(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    static void printInfo(String message) {
        System.out.println("\n[INFO] " + message);
    }

    private static String center(String text) {
        int width = LINE.length();
        int padding = Math.max(0, (width - text.length()) / 2);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) sb.append(' ');
        sb.append(text);
        return sb.toString();
    }
}
