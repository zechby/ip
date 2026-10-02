package jamal;

import jamal.command.Command;

public class JamaL {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public JamaL() {
        ui = new Ui();
        storage = new Storage();
        tasks = new TaskList();
    }

    /**
     * Fills the task list from the save file. Problems are reported to the
     * user but never stop the chatbot from starting; it just starts with
     * fewer (or no) tasks.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
            if (storage.getCorruptLineCount() > 0) {
                ui.showError("skipped " + storage.getCorruptLineCount()
                        + " broken line(s) in the save file.");
            }
        } catch (JamaLException e) {
            ui.showError(e.getMessage());
        }
    }

    public void run() {
        ui.showWelcome();
        loadTasks();
        boolean isExit = false;
        while (!isExit) {
            // Every user-facing error is thrown as a JamaLException and caught
            // here, so all error messages are printed from one place.
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (JamaLException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new JamaL().run();
    }
}
