package service;

import exception.InvalidSalaryException;
import model.Payroll;
import util.Constants;

/**
 * Default {@link SalaryCalculator}.
 * <p>
 * Formula (exactly as specified):
 * Net Salary = Basic + HRA + DA + Bonus + Allowance
 *              - Tax - PF - LeaveDeduction - PermissionDeduction
 * <p>
 * Leave Deduction      = leaveDays * Rs.500
 * Permission Deduction = permissionHours * Rs.50
 * <p>
 * Example: Basic 40000, HRA 5000, DA 3000, Bonus 2000, Allowance 0,
 * Tax 2500, PF 1800, Leave 2 days, Permission 4 hours
 * -&gt; Leave Deduction 1000, Permission Deduction 200, Net Salary 44500.
 */
public class SalaryCalculatorImpl implements SalaryCalculator {

    @Override
    public Payroll calculate(int employeeId, String payPeriod, double basicSalary, double hra, double da,
                              double bonus, double allowance, double tax, double providentFund,
                              int leaveDays, double permissionHours) throws InvalidSalaryException {

        if (basicSalary < 0) throw new InvalidSalaryException("Basic salary cannot be negative.");
        if (hra < 0) throw new InvalidSalaryException("HRA cannot be negative.");
        if (da < 0) throw new InvalidSalaryException("DA cannot be negative.");
        if (bonus < 0) throw new InvalidSalaryException("Bonus cannot be negative.");
        if (allowance < 0) throw new InvalidSalaryException("Allowance cannot be negative.");
        if (tax < 0) throw new InvalidSalaryException("Tax cannot be negative.");
        if (providentFund < 0) throw new InvalidSalaryException("Provident Fund cannot be negative.");
        if (leaveDays < 0) throw new InvalidSalaryException("Leave days cannot be negative.");
        if (permissionHours < 0) throw new InvalidSalaryException("Permission hours cannot be negative.");

        double leaveDeduction = leaveDays * Constants.LOP_PER_LEAVE_DAY;
        double permissionDeduction = permissionHours * Constants.LOP_PER_PERMISSION_HOUR;

        double netSalary = basicSalary + hra + da + bonus + allowance
                - tax - providentFund - leaveDeduction - permissionDeduction;

        return new Payroll(employeeId, payPeriod, basicSalary, hra, da, bonus, allowance,
                tax, providentFund, leaveDeduction, permissionDeduction, netSalary);
    }
}
