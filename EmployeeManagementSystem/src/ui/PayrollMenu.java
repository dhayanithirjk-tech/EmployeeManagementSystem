package ui;

import exception.EmployeeNotFoundException;
import exception.InvalidInputException;
import exception.InvalidSalaryException;
import model.Employee;
import model.Payroll;
import service.EmployeeService;
import service.PayrollService;
import util.FileManager;
import util.InputReader;
import util.ValidationUtil;

import java.util.List;

public class PayrollMenu {

    private final PayrollService payrollService;
    private final EmployeeService employeeService;
    private final InputReader input;

    public PayrollMenu(PayrollService payrollService, EmployeeService employeeService, InputReader input) {
        this.payrollService = payrollService;
        this.employeeService = employeeService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("PAYROLL MANAGEMENT");
            System.out.println(" 1. Generate Salary");
            System.out.println(" 2. View Salary Slip");
            System.out.println(" 3. Salary History");
            System.out.println(" 4. Monthly Payroll");
            System.out.println(" 5. Yearly Payroll");
            System.out.println(" 6. Export Salary Report");
            System.out.println(" 7. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: generateSalary(); break;
                case 2: viewSalarySlip(); break;
                case 3: salaryHistory(); break;
                case 4: monthlyPayroll(); break;
                case 5: yearlyPayroll(); break;
                case 6: exportReport(); break;
                case 7: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private void generateSalary() {
        ConsoleUtil.printSubHeader("Generate Salary");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            String period = input.readNonEmpty("Pay Period (yyyy-MM): ", "Pay Period");
            double bonus = input.readValidatedSalary("Bonus: ");
            double allowance = input.readValidatedSalary("Allowance: ");

            Payroll payroll = payrollService.generateSalary(empId, period, bonus, allowance);
            Employee e = employeeService.getEmployee(empId);
            System.out.println(payroll.toSlip(e.getFullName()));
        } catch (EmployeeNotFoundException | InvalidSalaryException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void viewSalarySlip() {
        ConsoleUtil.printSubHeader("View Salary Slip");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            String period = input.readNonEmpty("Pay Period (yyyy-MM): ", "Pay Period");
            Payroll payroll = payrollService.getSalarySlip(empId, period);
            if (payroll == null) {
                System.out.println("No salary slip found for this employee/period.");
            } else {
                Employee e = employeeService.getEmployee(empId);
                System.out.println(payroll.toSlip(e.getFullName()));
            }
        } catch (EmployeeNotFoundException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void salaryHistory() {
        ConsoleUtil.printSubHeader("Salary History");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            List<Payroll> history = payrollService.getSalaryHistory(empId);
            if (history.isEmpty()) {
                System.out.println("No salary history found.");
            } else {
                for (Payroll p : history) {
                    System.out.printf("Period: %-8s Net Salary: Rs. %.2f%n", p.getPayPeriod(), p.getNetSalary());
                }
            }
        } catch (InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void monthlyPayroll() {
        ConsoleUtil.printSubHeader("Monthly Payroll");
        String period = input.readNonEmpty("Pay Period (yyyy-MM): ", "Pay Period");
        List<Payroll> payrolls = payrollService.getMonthlyPayroll(period);
        printPayrollList(payrolls);
        input.pause();
    }

    private void yearlyPayroll() {
        ConsoleUtil.printSubHeader("Yearly Payroll");
        String year = input.readNonEmpty("Year (yyyy): ", "Year");
        List<Payroll> payrolls = payrollService.getYearlyPayroll(year);
        printPayrollList(payrolls);
        input.pause();
    }

    private void printPayrollList(List<Payroll> payrolls) {
        if (payrolls.isEmpty()) {
            System.out.println("No payroll records found.");
            return;
        }
        double total = 0;
        for (Payroll p : payrolls) {
            System.out.printf("EmpID: %-6d Period: %-8s Net Salary: Rs. %.2f%n",
                    p.getEmployeeId(), p.getPayPeriod(), p.getNetSalary());
            total += p.getNetSalary();
        }
        System.out.printf("Total Payout: Rs. %.2f%n", total);
    }

    private void exportReport() {
        ConsoleUtil.printSubHeader("Export Salary Report");
        String period = input.readNonEmpty("Pay Period (yyyy-MM): ", "Pay Period");
        List<Payroll> payrolls = payrollService.getMonthlyPayroll(period);
        if (payrolls.isEmpty()) {
            ConsoleUtil.printInfo("No records to export for this period.");
            input.pause();
            return;
        }
        String path = "data/exports/salary_report_" + period + ".txt";
        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add("Salary Report - " + period);
        lines.add("=================================================");
        for (Payroll p : payrolls) {
            lines.add(String.format("EmpID: %-6d Net Salary: Rs. %.2f", p.getEmployeeId(), p.getNetSalary()));
        }
        FileManager.writeAllLines(path, lines);
        ConsoleUtil.printSuccess("Salary report exported to " + path);
        input.pause();
    }
}
