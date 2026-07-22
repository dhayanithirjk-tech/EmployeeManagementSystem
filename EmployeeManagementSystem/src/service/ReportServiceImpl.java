package service;

import model.Employee;
import model.EmployeeComparators;
import model.EmployeeStatus;

import java.time.Year;
import java.util.*;

public class ReportServiceImpl implements ReportService {

    private final EmployeeService employeeService;

    public ReportServiceImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public String allEmployeesReport() {
        List<Employee> employees = employeeService.sortEmployees(EmployeeComparators.BY_ID);
        StringBuilder sb = new StringBuilder("All Employees Report\n");
        sb.append("--------------------------------------------------------------\n");
        if (employees.isEmpty()) {
            sb.append("No employees found.\n");
        }
        for (Employee e : employees) {
            sb.append(e.getDetails()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String departmentWiseReport() {
        Map<String, List<Employee>> byDept = new TreeMap<>();
        for (Employee e : employeeService.getAllEmployees()) {
            byDept.computeIfAbsent(e.getDepartment(), k -> new ArrayList<>()).add(e);
        }
        StringBuilder sb = new StringBuilder("Department-wise Employees Report\n");
        sb.append("--------------------------------------------------------------\n");
        for (Map.Entry<String, List<Employee>> entry : byDept.entrySet()) {
            sb.append("Department: ").append(entry.getKey())
              .append(" (").append(entry.getValue().size()).append(" employee(s))\n");
            for (Employee e : entry.getValue()) {
                sb.append("   ").append(e.getDetails()).append("\n");
            }
        }
        if (byDept.isEmpty()) {
            sb.append("No employees found.\n");
        }
        return sb.toString();
    }

    @Override
    public String highestSalaryReport() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) return "No employees found.\n";
        Employee highest = Collections.max(employees, EmployeeComparators.BY_SALARY);
        return "Highest Paid Employee:\n" + highest.getDetails() + "\n";
    }

    @Override
    public String lowestSalaryReport() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) return "No employees found.\n";
        Employee lowest = Collections.min(employees, EmployeeComparators.BY_SALARY);
        return "Lowest Paid Employee:\n" + lowest.getDetails() + "\n";
    }

    @Override
    public String averageSalaryReport() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) return "No employees found.\n";
        double total = 0;
        for (Employee e : employees) {
            total += e.getBasicSalary();
        }
        double avg = total / employees.size();
        return String.format("Average Basic Salary across %d employee(s): Rs. %.2f%n", employees.size(), avg);
    }

    @Override
    public String employeesJoinedThisYearReport() {
        String currentYear = String.valueOf(Year.now().getValue());
        StringBuilder sb = new StringBuilder("Employees Joined in " + currentYear + "\n");
        sb.append("--------------------------------------------------------------\n");
        boolean any = false;
        for (Employee e : employeeService.getAllEmployees()) {
            if (e.getDateOfJoining() != null && e.getDateOfJoining().startsWith(currentYear)) {
                sb.append(e.getDetails()).append("\n");
                any = true;
            }
        }
        if (!any) sb.append("No employees joined this year.\n");
        return sb.toString();
    }

    @Override
    public String inactiveEmployeesReport() {
        StringBuilder sb = new StringBuilder("Inactive Employees\n");
        sb.append("--------------------------------------------------------------\n");
        boolean any = false;
        for (Employee e : employeeService.getAllEmployees()) {
            if (e.getStatus() == EmployeeStatus.INACTIVE) {
                sb.append(e.getDetails()).append("\n");
                any = true;
            }
        }
        if (!any) sb.append("No inactive employees.\n");
        return sb.toString();
    }

    @Override
    public String salaryStatisticsReport() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) return "No employees found.\n";
        double total = 0, max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (Employee e : employees) {
            double s = e.getBasicSalary();
            total += s;
            max = Math.max(max, s);
            min = Math.min(min, s);
        }
        double avg = total / employees.size();
        StringBuilder sb = new StringBuilder("Salary Statistics\n");
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("Total Employees : %d%n", employees.size()));
        sb.append(String.format("Total Payroll    : Rs. %.2f%n", total));
        sb.append(String.format("Average Salary   : Rs. %.2f%n", avg));
        sb.append(String.format("Highest Salary   : Rs. %.2f%n", max));
        sb.append(String.format("Lowest Salary    : Rs. %.2f%n", min));
        return sb.toString();
    }
}
