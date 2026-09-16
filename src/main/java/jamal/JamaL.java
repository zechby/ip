package jamal;

import java.util.ArrayList;
import java.util.Scanner;

import jamal.task.Deadline;
import jamal.task.Event;
import jamal.task.Task;
import jamal.task.Todo;

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

    /**
     * Stores every task in the order it was added.
     * An ArrayList grows automatically and can remove items from the middle,
     * so there is no fixed size limit and no separate counter to keep in sync.
     */
    private static final ArrayList<Task> taskList = new ArrayList<>();

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
                    taskList.get(index).setDone(true);
                    printBlock("task " + arguments + " is marked", "  " + taskList.get(index));
                } else if (command.equals("unmark")) {
                    int index = parseTaskIndex(arguments, "unmark");
                    taskList.get(index).setDone(false);
                    printBlock("task " + arguments + " is unmarked", "  " + taskList.get(index));
                } else if (command.equals("delete")) {
                    deleteTask(parseTaskIndex(arguments, "delete"));
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
     * Turns the argument of a mark/unmark/delete command into an index into taskList.
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
        // Checking the bounds here gives a friendly message instead of letting
        // taskList.get(index) throw an IndexOutOfBoundsException.
        if (index < 0 || index >= taskList.size()) {
            throw new JamaLException("no task " + arguments + ".");
        }
        return index;
    }

    /**
     * Creates a todo from the given description.
     *
     * @param description Text of the todo, e.g. "borrow book".
     * @throws JamaLException If the description is empty.
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
     *                        or empty.
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
     *                        part is missing or empty.
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
     */
    private static void addTask(Task newTask) {
        taskList.add(newTask);
        printBlock("added:", "  " + newTask, "you got " + taskCountText() + " now.");
    }

    /**
     * Removes the task at the given index and confirms it to the user.
     * ArrayList.remove shifts the later tasks down, so the numbering in
     * the next "list" stays continuous.
     *
     * @param index Zero-based index, already checked by parseTaskIndex.
     */
    private static void deleteTask(int index) {
        Task removed = taskList.remove(index);
        printBlock("gone:", "  " + removed, "you got " + taskCountText() + " left.");
    }

    /**
     * Returns the number of tasks with the right singular/plural noun,
     * e.g. "1 task" or "3 tasks".
     */
    private static String taskCountText() {
        int count = taskList.size();
        return count + (count == 1 ? " task" : " tasks");
    }

    /** Prints all tasks in the list, numbered starting from 1. */
    private static void listTasks() {
        System.out.println(DIVIDER);
        if (taskList.isEmpty()) {
            System.out.println("You got no tasks.");
            System.out.println(DIVIDER);
            return;
        }
        for (int i = 0; i < taskList.size(); i++) {
            // toString() picks the tasktype, so each line shows the
            // right type icon and date/time details.
            System.out.println((i + 1) + "." + taskList.get(i));
        }
        System.out.println(DIVIDER);
    }
}
