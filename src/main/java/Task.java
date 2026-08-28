/**
 * Represents a task with a text description.
 */
public class Task {
    private String description;

    /**
     * Creates a new task with the given description.
     *
     * @param description Text of the task.
     */
    public Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}