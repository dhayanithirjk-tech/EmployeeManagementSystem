package model;

/** A generated salary slip for one employee for one pay period. */
public class Payroll {

    private final int employeeId;
    private final String payPeriod; // e.g. "2026-07"
    private final double basicSalary;
    private final double hra;
    private final double da;
    private final double bonus;
    private final double allowance;
    private final double tax;
    private final double providentFund;
    private final double leaveDeduction;
    private final double permissionDeduction;
    private final double netSalary;

    public Payroll(int employeeId, String payPeriod, double basicSalary, double hra, double da,
                    double bonus, double allowance, double tax, double providentFund,
                    double leaveDeduction, double permissionDeduction, double netSalary) {
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.da = da;
        this.bonus = bonus;
        this.allowance = allowance;
        this.tax = tax;
        this.providentFund = providentFund;
        this.leaveDeduction = leaveDeduction;
        this.permissionDeduction = permissionDeduction;
        this.netSalary = netSalary;
    }

    public int getEmployeeId() { return employeeId; }
    public String getPayPeriod() { return payPeriod; }
    public double getBasicSalary() { return basicSalary; }
    public double getHra() { return hra; }
    public double getDa() { return da; }
    public double getBonus() { return bonus; }
    public double getAllowance() { return allowance; }
    public double getTax() { return tax; }
    public double getProvidentFund() { return providentFund; }
    public double getLeaveDeduction() { return leaveDeduction; }
    public double getPermissionDeduction() { return permissionDeduction; }
    public double getNetSalary() { return netSalary; }

    /** Formatted, multi-line salary slip suitable for console/file output. */
    public String toSlip(String employeeName) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("               SALARY SLIP               \n");
        sb.append("=========================================\n");
        sb.append(String.format("Employee ID   : %d%n", employeeId));
        sb.append(String.format("Employee Name : %s%n", employeeName));
        sb.append(String.format("Pay Period    : %s%n", payPeriod));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Basic Salary       : Rs. %10.2f%n", basicSalary));
        sb.append(String.format("HRA                : Rs. %10.2f%n", hra));
        sb.append(String.format("DA                 : Rs. %10.2f%n", da));
        sb.append(String.format("Bonus              : Rs. %10.2f%n", bonus));
        sb.append(String.format("Allowance          : Rs. %10.2f%n", allowance));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("Tax                : Rs. %10.2f%n", tax));
        sb.append(String.format("Provident Fund     : Rs. %10.2f%n", providentFund));
        sb.append(String.format("Leave Deduction    : Rs. %10.2f%n", leaveDeduction));
        sb.append(String.format("Permission Deduct. : Rs. %10.2f%n", permissionDeduction));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("NET SALARY         : Rs. %10.2f%n", netSalary));
        sb.append("=========================================\n");
        return sb.toString();
    }

    public String toFileLine() {
        return employeeId + "|" + payPeriod + "|" + basicSalary + "|" + hra + "|" + da + "|"
                + bonus + "|" + allowance + "|" + tax + "|" + providentFund + "|"
                + leaveDeduction + "|" + permissionDeduction + "|" + netSalary;
    }
}
