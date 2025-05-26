import java.time.LocalTime;

public class LoginSession {
    private long id;
    private LocalTime loginTime;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalTime loginTime) { this.loginTime = loginTime; }
}
