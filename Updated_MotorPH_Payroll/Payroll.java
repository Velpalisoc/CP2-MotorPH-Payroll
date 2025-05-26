public class Payroll {
    private double id;
    private double totalHoursWorked;
    private double overtimeHours;
    private double bonuses;
    private double grossSalary;
    public double getGrossSalary() {
        return grossSalary;
    }
    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }
    private double netSalary;
    public double getNetSalary() {
        return netSalary;
    }
    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }
    private String taxExemptions;
    public String getTaxExemptions() {
        return taxExemptions;
    }
    public void setTaxExemptions(String taxExemptions) {
        this.taxExemptions = taxExemptions;
    }
    private double loanRepayment;
    private Deductions deductions;

    public double getId() { return id; }
    public void setId(double id) { this.id = id; }

    public double calculateGrossSalary() {
        return (totalHoursWorked * 100) + bonuses + (overtimeHours * 150);
    }

    public double calculateNetSalary() {
        return calculateGrossSalary() - deductions.getTotalDeductions() - loanRepayment;
    }

    public double getBonuses() { return bonuses; }
    public void setBonuses(double bonuses) { this.bonuses = bonuses; }

    public Deductions getDeductions() { return deductions; }
    public void setDeductions(Deductions deductions) { this.deductions = deductions; }

    public double getLoanRepayments() { return loanRepayment; }
    public void setLoanRepayments(double loanRepayment) { this.loanRepayment = loanRepayment; }

    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }
}
