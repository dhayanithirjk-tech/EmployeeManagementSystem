package service;

import exception.EmployeeNotFoundException;
import exception.InvalidSalaryException;
import model.Employee;
import model.Payroll;
import util.Constants;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates payroll generation by pulling the employee's basic salary
 * from {@link EmployeeService}, applying the standard HRA/DA/Tax/PF
 * percentage policy (business rule owned by this class), and delegating
 * the final arithmetic to {@link SalaryCalculator} (Single Responsibility
 * Principle: this class decides the policy, the calculator does the sum).
 */
public class PayrollServiceImpl implements PayrollService {

    private final EmployeeService employeeService;
    private final SalaryCalculator salaryCalculator;
    private final List<Payroll> payrollHistory = new ArrayList<>();

    public PayrollServiceImpl(EmployeeService employeeService, SalaryCalculator salaryCalculator) {
        this.employeeService = employeeService;
        this.salaryCalculator = salaryCalculator;
        load();
    }

    @Override
    public Payroll generateSalary(int employeeId, String payPeriod, double bonus, double allowance)
            throws EmployeeNotFoundException, InvalidSalaryException {
        Employee employee = employeeService.getEmployee(employeeId);
        double basicSalary = employee.getBasicSalary();

        // Standard payroll policy: HRA/DA/Tax/PF as a percentage of Basic Salary.
        double hra = basicSalary * Constants.DEFAULT_HRA_PERCENT;
        double da = basicSalary * Constants.DEFAULT_DA_PERCENT;
        double tax = basicSalary * Constants.DEFAULT_TAX_PERCENT;
        double pf = basicSalary * Constants.DEFAULT_PF_PERCENT;

        // Leave/permission figures default to 0 here; the Salary Calculator menu
        // lets the user supply explicit leave/permission figures for ad-hoc calculations.
        Payroll payroll = salaryCalculator.calculate(employeeId, payPeriod, basicSalary, hra, da,
                bonus, allowance, tax, pf, 0, 0);
        payrollHistory.removeIf(p -> p.getEmployeeId() == employeeId && p.getPayPeriod().equals(payPeriod));
        payrollHistory.add(payroll);
        save();
        return payroll;
    }

    @Override
    public Payroll getSalarySlip(int employeeId, String payPeriod) {
        for (Payroll p : payrollHistory) {
            if (p.getEmployeeId() == employeeId && p.getPayPeriod().equals(payPeriod)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Payroll> getSalaryHistory(int employeeId) {
        List<Payroll> result = new ArrayList<>();
        for (Payroll p : payrollHistory) {
            if (p.getEmployeeId() == employeeId) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<Payroll> getMonthlyPayroll(String payPeriod) {
        List<Payroll> result = new ArrayList<>();
        for (Payroll p : payrollHistory) {
            if (p.getPayPeriod().equals(payPeriod)) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<Payroll> getYearlyPayroll(String year) {
        List<Payroll> result = new ArrayList<>();
        for (Payroll p : payrollHistory) {
            if (p.getPayPeriod().startsWith(year)) {
                result.add(p);
            }
        }
        return result;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Payroll p : payrollHistory) {
            lines.add(p.toFileLine());
        }
        FileManager.writeAllLines(Constants.PAYROLL_FILE, lines);
    }

    private void load() {
        for (String line : FileManager.readAllLines(Constants.PAYROLL_FILE)) {
            try {
                String[] p = line.split("\\|", -1);
                Payroll payroll = new Payroll(Integer.parseInt(p[0]), p[1], Double.parseDouble(p[2]),
                        Double.parseDouble(p[3]), Double.parseDouble(p[4]), Double.parseDouble(p[5]),
                        Double.parseDouble(p[6]), Double.parseDouble(p[7]), Double.parseDouble(p[8]),
                        Double.parseDouble(p[9]), Double.parseDouble(p[10]), Double.parseDouble(p[11]));
                payrollHistory.add(payroll);
            } catch (Exception ex) {
                System.out.println("Warning: skipping corrupt payroll record.");
            }
        }
    }
}
