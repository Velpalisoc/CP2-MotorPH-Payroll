package model;

public class Deductions {
    private double sss;
    private double philHealth;
    private double pagIbig;
    private double lateOrUndertime;

    public Deductions(double sss, double philHealth, double pagIbig, double lateOrUndertime) {
        this.sss = sss;
        this.philHealth = philHealth;
        this.pagIbig = pagIbig;
        this.lateOrUndertime = lateOrUndertime;
    }

    public double getSss() {
        return sss;
    }

    public double getPhilHealth() {
        return philHealth;
    }

    public double getPagIbig() {
        return pagIbig;
    }

    public double getLateOrUndertime() {
        return lateOrUndertime;
    }

    public double getTotalDeductions() {
        return sss + philHealth + pagIbig + lateOrUndertime;
    }
}
