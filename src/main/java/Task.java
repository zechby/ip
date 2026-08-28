/**
 * Represents a task with a text description.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     * Task is initially not done.
     *
     * @param description Text of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}