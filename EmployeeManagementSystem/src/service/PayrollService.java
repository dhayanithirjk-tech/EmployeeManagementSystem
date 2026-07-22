package service;

import exception.EmployeeNotFoundException;
import exception.InvalidSalaryException;
import model.Payroll;
import java.util.List;

public interface PayrollService {
    Payroll generateSalary(int employeeId, String payPeriod, double bonus, double allowance)
            throws EmployeeNotFoundException, InvalidSalaryException;
    Payroll getSalarySlip(int employeeId, String payPeriod);
    List<Payroll> getSalaryHistory(int employeeId);
    List<Payroll> getMonthlyPayroll(String payPeriod);
    List<Payroll> getYearlyPayroll(String year);
}
