public class JamaL {
    /** Name shown to the user in the greeting. */
    private static final String NAME = "JamaL";

    /** Horizontal line used to visually separate the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";

    private static final String BANNER =
              "     ____.                     .____     \n"
            + "    |    |____    _____ _____  |    |    \n"
            + "    |    \\__  \\  /     \\\\__  \\ |    |    \n"
            + "/\\__|    |/ __ \\|  Y Y  \\/ __ \\|    |___ \n"
            + "\\________(____  /__|_|  (____  /_______ \\\n"
            + "              \\/      \\/     \\/        \\/";

    public static void main(String[] args) {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello! I'm " + NAME + ".");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
