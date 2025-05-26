import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        // Create an employee
        Employee emp = new Employee();
        emp.setId(1);
        emp.setEmployeeNumber("EMP001");
        emp.setName("Juan Dela Cruz");
        emp.setBirthday(LocalDate.of(1995, 5, 15));
        emp.setContactInfo("09123456789");
        emp.setPosition("Developer");
        emp.setDepartment("IT");

        // Create attendance
        Attendance att = new Attendance();
        att.setId(1);
        att.setDate(LocalDate.now());
        att.setTimeIn(LocalTime.of(9, 0));
        att.setTimeOut(LocalTime.of(18, 0));

        // Calculate total hours
        double hoursWorked = att.calculateTotalHours();
        System.out.println("Total hours worked: " + hoursWorked);

        // Create deductions
        Deductions ded = new Deductions();
        ded.setId(1);
        ded.setSss(200);
        ded.setPhilHealth(150);
        ded.setPagIbig(100);
        ded.setWithholdingTax(300);
        ded.setOtherDeductions(50);

        // Create payroll
        Payroll payroll = new Payroll();
        payroll.setId(1);
        payroll.setBonuses(500);
        payroll.setOvertimeHours(2);
        payroll.setDeductions(ded);
        payroll.setLoanRepayments(300);
        payroll.setOvertimeHours(2);

        payroll.calculateGrossSalary();
        payroll.calculateNetSalary();

        System.out.println("Gross Salary: " + payroll.calculateGrossSalary());
        System.out.println("Net Salary: " + payroll.calculateNetSalary());

        // Login session
        LoginSession session = new LoginSession();
        session.setId(1);
        session.setLoginTime(LocalTime.of(8, 30));
        System.out.println("Login Time: " + session.getLoginTime());
    }
}
