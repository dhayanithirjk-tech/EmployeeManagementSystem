package service;

import exception.InvalidSalaryException;
import model.Payroll;

/**
 * Dedicated Salary Calculator module (module 6 of the spec).
 * <p>
 * SINGLE RESPONSIBILITY PRINCIPLE: this class does ONE thing -
 * turn a set of salary inputs into a {@link Payroll} breakdown.
 * PayrollService reuses it so the formula is defined in exactly one place.
 */
public interface SalaryCalculator {
    Payroll calculate(int employeeId, String payPeriod, double basicSalary, double hra, double da,
                       double bonus, double allowance, double tax, double providentFund,
                       int leaveDays, double permissionHours)
            throws InvalidSalaryException;
}
