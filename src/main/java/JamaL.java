import java.util.Scanner;

public class JamaL {
    /** Name shown to the user in the greeting. */
    private static final String NAME = "JamaL";

    /** Horizontal line used to visually separate the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";

    /** ASCII art banner displayed on startup. */
    private static final String BANNER = "     ____.                     .____     \n"
            + "    |    |____    _____ _____  |    |    \n"
            + "    |    \\__  \\  /     \\\\__  \\ |    |    \n"
            + "/\\__|    |/ __ \\|  Y Y  \\/ __ \\|    |___ \n"
            + "\\________(____  /__|_|  (____  /_______ \\\n"
            + "              \\/      \\/     \\/        \\/";

    /** Maximum number of tasks the list can hold. */
    private static final int MAX_TASKS = 100;

    /* Task array that stores every task*/
    private static Task[] taskList = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        greeting();
        while (true) {
            // Split into the command word and everything after it
            String[] input = in.nextLine().trim().split(" ", 2);
            String command = input[0];
            String arguments = input.length > 1 ? input[1].trim() : "";

            if (command.equals("bye")) {
                break;
            }

            // Every user-facing error is thrown as a JamaLException and caught
            // here, so all error messages are printed in the same format from
            // one place instead of each command printing its own.
            try {
                if (command.equals("list")) {
                    listTasks();
                } else if (command.equals("mark")) {
                    int index = parseTaskIndex(arguments, "mark");
                    taskList[index].setDone(true);
                    printBlock("task " + arguments + " is marked", "  " + taskList[index]);
                } else if (command.equals("unmark")) {
                    int index = parseTaskIndex(arguments, "unmark");
                    taskList[index].setDone(false);
                    printBlock("task " + arguments + " is unmarked", "  " + taskList[index]);
                } else if (command.equals("todo")) {
                    addTodo(arguments);
                } else if (command.equals("deadline")) {
                    addDeadline(arguments);
                } else if (command.equals("event")) {
                    addEvent(arguments);
                } else {
                    // Anything that is not a known command word is rejected
                    throw new JamaLException("I'm not doing whatever " + command + " is.");
                }
            } catch (JamaLException e) {
                printBlock(e.getMessage());
            }
        }
        goodbye();
    }

    /** Prints the greeting banner and prompt. */
    private static void greeting() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("It's " + NAME + ".");
        System.out.println("What do you want.");
        System.out.println(DIVIDER);
    }

    /** Prints the exit message. */
    private static void goodbye() {
        System.out.println(DIVIDER);
        System.out.println("alright.");
        System.out.println(DIVIDER);
    }

    /**
     * Prints the given lines wrapped between two dividers, so every reply
     * to the user looks the same.
     *
     * @param lines Lines to print inside the block.
     */
    private static void printBlock(String... lines) {
        System.out.println(DIVIDER);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(DIVIDER);
    }

    /**
     * Turns the argument of a mark/unmark command into an index into taskList.
     *
     * @param arguments Text typed after the command word, e.g. "2".
     * @param command Command word, used in the error message.
     * @return Zero-based index of the task the user meant.
     * @throws JamaLException If the argument is missing, is not a number, or
     *                        does not point at a task that exists.
     */
    private static int parseTaskIndex(String arguments, String command) throws JamaLException {
        if (arguments.isEmpty()) {
            throw new JamaLException(command + " what.");
        }
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            // parseInt throws on anything that is not a plain number, e.g. "abc".
            throw new JamaLException("that's not a number.");
        }
        // Slots from taskCount up to MAX_TASKS are still null, so checking
        // against taskCount (not MAX_TASKS) is what stops a NullPointerException
        // when the caller does taskList[index].setDone(...).
        if (index < 0 || index >= taskCount) {
            throw new JamaLException("no task " + arguments + ".");
        }
        return index;
    }

    /**
     * Creates a todo from the given description.
     *
     * @param description Text of the todo, e.g. "borrow book".
     * @throws JamaLException If the description is empty, or the list is full.
     */
    private static void addTodo(String description) throws JamaLException {
        if (description.isEmpty()) {
            throw new JamaLException("todo what.");
        }
        addTask(new Todo(description));
    }

    /**
     * Creates a deadline from arguments of the form
     * {@code <description> /by <when>}.
     *
     * @param arguments Text typed after the "deadline" command word.
     * @throws JamaLException If the description or the "/by" part is missing
     *                        or empty, or the list is full.
     */
    private static void addDeadline(String arguments) throws JamaLException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new JamaLException("deadline what /by when.");
        }
        addTask(new Deadline(parts[0].trim(), parts[1].trim()));
    }

    /**
     * Creates an event from arguments of the form
     * {@code <description> /from <start> /to <end>}.
     *
     * @param arguments Text typed after the "event" command word.
     * @throws JamaLException If the description, the "/from" part or the "/to"
     *                        part is missing or empty, or the list is full.
     */
    private static void addEvent(String arguments) throws JamaLException {
        String[] descAndRest = arguments.split(" /from ", 2);
        if (descAndRest.length < 2 || descAndRest[0].trim().isEmpty()) {
            throw new JamaLException("event what /from when /to when.");
        }
        String[] fromAndTo = descAndRest[1].split(" /to ", 2);
        if (fromAndTo.length < 2 || fromAndTo[0].trim().isEmpty() || fromAndTo[1].trim().isEmpty()) {
            throw new JamaLException("event what /from when /to when.");
        }
        addTask(new Event(descAndRest[0].trim(), fromAndTo[0].trim(), fromAndTo[1].trim()));
    }

    /**
     * Stores an already-built task and confirms it to the user.
     * Takes a {@code Task}, so it works for every task type.
     *
     * @param newTask Task to add to the list.
     * @throws JamaLException If the list is already holding MAX_TASKS tasks.
     */
    private static void addTask(Task newTask) throws JamaLException {
        if (taskCount >= MAX_TASKS) {
            throw new JamaLException("list's full.");
        }
        taskList[taskCount++] = newTask;
        String noun = taskCount > 1 ? " tasks now." : " task now.";
        printBlock("added:", "  " + newTask, "you got " + taskCount + noun);
    }

    /** Prints all tasks in the list, numbered starting from 1. */
    private static void listTasks() {
        System.out.println(DIVIDER);
        if (taskCount == 0) {
            System.out.println("You got no tasks.");
            System.out.println(DIVIDER);
            return;
        }
        for (int i = 0; i < taskCount; i++) {
            // toString() picks the tasktype, so each line shows the
            // right type icon and date/time details.
            System.out.println((i + 1) + "." + taskList[i]);
        }
        System.out.println(DIVIDER);
    }
}
