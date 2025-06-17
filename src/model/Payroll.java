package model;

public class Payroll {
    private Employee employee;
    private int workedDays;
    private int hoursPerDay;
    private boolean isRegularHoliday;
    private boolean isSpecialHoliday;

    public Payroll(Employee employee, int workedDays, int hoursPerDay, boolean isRegularHoliday, boolean isSpecialHoliday) {
        this.employee = employee;
        this.workedDays = workedDays;
        this.hoursPerDay = hoursPerDay;
        this.isRegularHoliday = isRegularHoliday;
        this.isSpecialHoliday = isSpecialHoliday;
    }

    public double computeGrossPay() {
        double basePay = workedDays * hoursPerDay * employee.getRatePerHour();
        if (isRegularHoliday) {
            basePay *= 2;
        } else if (isSpecialHoliday) {
            basePay *= 1.3;
        }
        return basePay;
    }

    public double computeNetPay(double totalDeductions) {
        return computeGrossPay() - totalDeductions;
    }

    public Employee getEmployee() {
        return employee;
    }

    public int getWorkedDays() {
        return workedDays;
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
}
