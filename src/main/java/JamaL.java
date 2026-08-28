import java.util.Scanner;

/**
 * JamaL is a personal assistant chatbot that manages a task list.
 */
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

    private static Task[] taskList = new Task[MAX_TASKS];
    private static int taskCount = 0;

    /**
     * Runs the chatbot's main loop, reading commands until the user types "bye".
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        greeting();
        while (true) {
            String input = in.nextLine().trim();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                listTasks();
            } else {
                addTask(input);
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
     * Adds a new task with the given description to the task list.
     * Rejects the task if the list is full.
     *
     * @param taskDesc Description of the task to add.
     */
    private static void addTask(String taskDesc) {
        if (taskCount >= MAX_TASKS) {
            System.out.println(DIVIDER);
            System.out.println("No more than " + MAX_TASKS + " tasks.");
            System.out.println(DIVIDER);
            return;
        }
        Task newTask = new Task(taskDesc);
        taskList[taskCount++] = newTask;
        System.out.println(DIVIDER);
        if (taskCount > 1) {
            System.out.println("added task " + taskDesc + ", you got " + taskCount + " tasks now.");
        } else {
            System.out.println("added task " + taskDesc + ", you got " + taskCount + " task now.");
        }
        System.out.println(DIVIDER);
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
            System.out.println((i + 1) + ". " + taskList[i].getDescription());
        }
        System.out.println(DIVIDER);
    }
}