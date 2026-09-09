package jamal.task;

/**
 * Base class for every type of task
 *
 * A task knows its description and whether it is done. Its children
 * ({@link Todo}, {@link Deadline}, {@link Event}) add any date/time information
 * and supply their own one-letter type icon.
 */
public abstract class Task {
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

    /**
     * Returns the single letter shown in the first pair of brackets,
     * actual return is overridden by child classes
     *
     * @return Type icon of this task.
     */
    public abstract String getTypeIcon();

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task formatted for display, e.g. {@code [T][X] read book}.
     * Subclasses append their date/time details by overriding this and calling
     * {@code super.toString()} first.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
