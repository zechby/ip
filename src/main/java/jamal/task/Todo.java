package jamal.task;

/**
 * tasks without any date/time attached to them, e.g., visit new theme park
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}
