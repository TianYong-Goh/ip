import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDate at;

    public Event(String description, LocalDate at) {
        super(description);
        this.at = at;
    }

    private Event(String description, LocalDate at, boolean isDone) {
        super(description, isDone);
        this.at = at;
    }

    public Event markAsDone() {
        return new Event(this.description, this.at, true);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: "
                + at.format(DateTimeFormatter.ofPattern("MMM d yyyy"))
                + "[" + at.getDayOfWeek() + "])";
    }
}

