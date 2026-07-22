package ui;

import exception.InvalidSalaryException;
import model.Payroll;
import service.SalaryCalculator;
import util.InputReader;

public class SalaryCalculatorMenu {

    private final SalaryCalculator salaryCalculator;
    private final InputReader input;

    public SalaryCalculatorMenu(SalaryCalculator salaryCalculator, InputReader input) {
        this.salaryCalculator = salaryCalculator;
        this.input = input;
    }

    public void show() {
        ConsoleUtil.printHeader("SALARY CALCULATOR");
        System.out.println("Enter every component; Net Salary is derived automatically.");
        try {
            double basic = input.readValidatedSalary("Basic Salary: ");
            double hra = input.readValidatedSalary("HRA: ");
            double da = input.readValidatedSalary("DA: ");
            double bonus = input.readValidatedSalary("Bonus: ");
            double allowance = input.readValidatedSalary("Allowance: ");
            double tax = input.readValidatedSalary("Tax: ");
            double pf = input.readValidatedSalary("Provident Fund: ");
            int leaveDays = input.readValidatedLeaveDays("Leave Days: ");
            double permissionHours = input.readValidatedPermissionHours("Permission Hours: ");

            Payroll result = salaryCalculator.calculate(0, "N/A", basic, hra, da, bonus, allowance,
                    tax, pf, leaveDays, permissionHours);

            System.out.println();
            System.out.println(ConsoleUtil.SUB_LINE);
            System.out.println("SALARY BREAKDOWN");
            System.out.println(ConsoleUtil.SUB_LINE);
            System.out.printf("Basic Salary        : Rs. %10.2f%n", result.getBasicSalary());
            System.out.printf("HRA                 : Rs. %10.2f%n", result.getHra());
            System.out.printf("DA                  : Rs. %10.2f%n", result.getDa());
            System.out.printf("Bonus               : Rs. %10.2f%n", result.getBonus());
            System.out.printf("Allowance           : Rs. %10.2f%n", result.getAllowance());
            System.out.println(ConsoleUtil.SUB_LINE);
            System.out.printf("Tax                 : Rs. %10.2f%n", result.getTax());
            System.out.printf("Provident Fund      : Rs. %10.2f%n", result.getProvidentFund());
            System.out.printf("Leave Deduction     : Rs. %10.2f  (%d day(s) x Rs.500)%n", result.getLeaveDeduction(), leaveDays);
            System.out.printf("Permission Deduction: Rs. %10.2f  (%.1f hr(s) x Rs.50)%n", result.getPermissionDeduction(), permissionHours);
            System.out.println(ConsoleUtil.SUB_LINE);
            System.out.printf("NET SALARY          : Rs. %10.2f%n", result.getNetSalary());
            System.out.println(ConsoleUtil.SUB_LINE);
        } catch (InvalidSalaryException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }
}
