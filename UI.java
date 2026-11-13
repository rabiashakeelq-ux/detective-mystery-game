public class UI {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    // Typewriter effect
    public static void type(String text, String color, int delayMs) {
        System.out.print(color);
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try { Thread.sleep(delayMs); } catch (InterruptedException e) { }
        }
        System.out.println(RESET);
    }

    public static void type(String text) { type(text, RESET, 25); }

    public static void line() {
        System.out.println(CYAN + "=".repeat(50) + RESET);
    }

    public static void pause(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }

    public static void banner() {
        System.out.println(RED + BOLD);
        System.out.println("  __  __ __   __ ___ _____ ___ ___ __   __");
        System.out.println(" |  \\/  |\\ \\ / // __|_   _| __| _ \\\\ \\ / /");
        System.out.println(" | |\\/| | \\ V / \\__ \\ | | | _||   / \\ V / ");
        System.out.println(" |_|  |_|  |_|  |___/ |_| |___|_|_\\  |_|  ");
        System.out.println("      T H E   B L A C K W O O D   M U R D E R" + RESET);
    }
}
