package model;

public class Employee {
    private String employeeId;
    private String name;
    private String position;
    private double ratePerHour;

    public Employee(String employeeId, String name, String position, double ratePerHour) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.ratePerHour = ratePerHour;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    @Override
    public String toString() {
        return employeeId + "," + name + "," + position + "," + ratePerHour;
    }
}
