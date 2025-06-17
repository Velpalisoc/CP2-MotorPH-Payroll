package model;

public class Attendance {
    private int daysPresent;
    private int hoursPerDay;
    private boolean isRegularHoliday;
    private boolean isSpecialHoliday;
    private double lateOrUndertimeHours;

    public Attendance(int daysPresent, int hoursPerDay, boolean isRegularHoliday, boolean isSpecialHoliday, double lateOrUndertimeHours) {
        this.daysPresent = daysPresent;
        this.hoursPerDay = hoursPerDay;
        this.isRegularHoliday = isRegularHoliday;
        this.isSpecialHoliday = isSpecialHoliday;
        this.lateOrUndertimeHours = lateOrUndertimeHours;
    }

    public int getDaysPresent() {
        return daysPresent;
    }

    public int getHoursPerDay() {
        return hoursPerDay;
    }

    public boolean isRegularHoliday() {
        return isRegularHoliday;
    }

    public boolean isSpecialHoliday() {
        return isSpecialHoliday;
    }

    public double getLateOrUndertimeHours() {
        return lateOrUndertimeHours;
    }

    public double getTotalHoursWorked() {
        return (daysPresent * hoursPerDay) - lateOrUndertimeHours;
    }
}
