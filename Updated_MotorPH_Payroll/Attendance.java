import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class Attendance {
    private long id;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public double calculateTotalHours() {
        return Duration.between(timeIn, timeOut).toMinutes() / 60.0;
    }

    public LocalTime getTimeIn() { return timeIn; }
    public void setTimeIn(LocalTime timeIn) { this.timeIn = timeIn; }

    public LocalTime getTimeOut() { return timeOut; }
    public void setTimeOut(LocalTime timeOut) { this.timeOut = timeOut; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
