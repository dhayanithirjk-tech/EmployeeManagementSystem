package ui;

import exception.*;
import model.Employee;
import model.EmployeeComparators;
import model.EmployeeStatus;
import service.EmployeeService;
import util.InputReader;

import java.util.Comparator;
import java.util.List;

public class EmployeeMenu {

    private final EmployeeService employeeService;
    private final InputReader input;

    public EmployeeMenu(EmployeeService employeeService, InputReader input) {
        this.employeeService = employeeService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("EMPLOYEE MANAGEMENT");
            System.out.println(" 1. Add Employee");
            System.out.println(" 2. View Employee");
            System.out.println(" 3. View All Employees");
            System.out.println(" 4. Update Employee");
            System.out.println(" 5. Delete Employee");
            System.out.println(" 6. Search Employee");
            System.out.println(" 7. Sort Employees");
            System.out.println(" 8. Employee Statistics");
            System.out.println(" 9. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: addEmployee(); break;
                case 2: viewEmployee(); break;
                case 3: viewAllEmployees(); break;
                case 4: updateEmployee(); break;
                case 5: deleteEmployee(); break;
                case 6: searchEmployee(); break;
                case 7: sortEmployees(); break;
                case 8: employeeStatistics(); break;
                case 9: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private void addEmployee() {
        ConsoleUtil.printSubHeader("Add Employee");
        try {
            int id = readNewEmployeeId();
            String name = input.readValidatedName("Full Name: ");
            int age = input.readValidatedAge("Age: ");
            String gender = input.readNonEmpty("Gender: ", "Gender");
            String department = input.readNonEmpty("Department: ", "Department");
            String designation = input.readNonEmpty("Designation: ", "Designation");
            String email = input.readValidatedEmail("Email: ");
            String phone = input.readValidatedPhone("Phone Number (10 digits): ");
            String doj = input.readValidatedDate("Date of Joining (yyyy-MM-dd): ");
            double salary = input.readValidatedSalary("Basic Salary: ");
            String address = input.readNonEmpty("Address: ", "Address");
            String bloodGroup = input.readNonEmpty("Blood Group: ", "Blood Group");
            String emergencyContact = input.readValidatedPhone("Emergency Contact (10 digits): ");

            Employee employee = employeeService.addEmployee(id, name, age, gender, department, designation,
                    email, phone, doj, salary, address, bloodGroup, emergencyContact);
            ConsoleUtil.printSuccess("Employee added successfully with ID " + employee.getEmployeeId());
        } catch (DuplicateEmployeeException | InvalidEmployeeException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    /** Reads a new Employee ID: must be numeric and positive (uniqueness is checked by the service layer). */
    private int readNewEmployeeId() {
        while (true) {
            try {
                String raw = input.readLine("Employee ID: ");
                int id = util.ValidationUtil.parseInt(raw, "Employee ID must be numeric.");
                if (id <= 0) throw new InvalidInputException("Employee ID must be a positive number.");
                return id;
            } catch (InvalidInputException ex) {
                input.printError(ex.getMessage());
            }
        }
    }

    private void viewEmployee() {
        ConsoleUtil.printSubHeader("View Employee");
        try {
            int id = readId();
            Employee e = employeeService.getEmployee(id);
            printEmployeeCard(e);
        } catch (EmployeeNotFoundException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private int readId() {
        while (true) {
            try {
                return util.ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            } catch (InvalidInputException ex) {
                input.printError(ex.getMessage());
            }
        }
    }

    private void viewAllEmployees() {
        ConsoleUtil.printSubHeader("All Employees");
        List<Employee> all = employeeService.getAllEmployees();
        if (all.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee e : all) {
                System.out.println(e.getDetails());
            }
        }
        input.pause();
    }

    private void updateEmployee() {
        ConsoleUtil.printSubHeader("Update Employee");
        try {
            int id = readId();
            Employee e = employeeService.getEmployee(id);
            System.out.println("Leave a field blank to keep its current value.");
            printEmployeeCard(e);

            String name = input.readLine("New Full Name [" + e.getFullName() + "]: ");
            if (!name.isEmpty()) { util.ValidationUtil.validateName(name); e.setFullName(name); }

            String deptStr = input.readLine("New Department [" + e.getDepartment() + "]: ");
            if (!deptStr.isEmpty()) e.setDepartment(deptStr);

            String designationStr = input.readLine("New Designation [" + e.getDesignation() + "]: ");
            if (!designationStr.isEmpty()) e.setDesignation(designationStr);

            String salaryStr = input.readLine("New Basic Salary [" + e.getBasicSalary() + "]: ");
            if (!salaryStr.isEmpty()) e.setBasicSalary(util.ValidationUtil.validateSalary(salaryStr));

            String phoneStr = input.readLine("New Phone [" + e.getPhoneNumber() + "]: ");
            if (!phoneStr.isEmpty()) { util.ValidationUtil.validatePhone(phoneStr); e.setPhoneNumber(phoneStr); }

            String emailStr = input.readLine("New Email [" + e.getEmail() + "]: ");
            if (!emailStr.isEmpty()) { util.ValidationUtil.validateEmail(emailStr); e.setEmail(emailStr); }

            String statusStr = input.readLine("New Status (ACTIVE/INACTIVE) [" + e.getStatus() + "]: ");
            if (!statusStr.isEmpty()) e.setStatus(EmployeeStatus.valueOf(statusStr.toUpperCase()));

            employeeService.updateEmployee(e);
            ConsoleUtil.printSuccess("Employee updated successfully.");
        } catch (EmployeeNotFoundException | InvalidInputException | IllegalArgumentException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void deleteEmployee() {
        ConsoleUtil.printSubHeader("Delete Employee");
        try {
            int id = readId();
            String confirm = input.readLine("Are you sure you want to delete employee " + id + "? (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                employeeService.deleteEmployee(id);
                ConsoleUtil.printSuccess("Employee deleted successfully.");
            } else {
                ConsoleUtil.printInfo("Deletion cancelled.");
            }
        } catch (EmployeeNotFoundException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void searchEmployee() {
        ConsoleUtil.printSubHeader("Search Employee (by ID - Binary Search)");
        try {
            int id = readId();
            Employee e = employeeService.searchByIdBinary(id);
            printEmployeeCard(e);
        } catch (EmployeeNotFoundException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void sortEmployees() {
        ConsoleUtil.printSubHeader("Sort Employees");
        System.out.println("1. By ID");
        System.out.println("2. By Name");
        System.out.println("3. By Salary (Low to High)");
        System.out.println("4. By Salary (High to Low)");
        System.out.println("5. By Department");
        System.out.println("6. By Date of Joining");
        int choice = input.readMenuChoice("Choose sort field: ");

        Comparator<Employee> comparator;
        switch (choice) {
            case 1: comparator = EmployeeComparators.BY_ID; break;
            case 2: comparator = EmployeeComparators.BY_NAME; break;
            case 3: comparator = EmployeeComparators.BY_SALARY; break;
            case 4: comparator = EmployeeComparators.BY_SALARY_DESC; break;
            case 5: comparator = EmployeeComparators.BY_DEPARTMENT; break;
            case 6: comparator = EmployeeComparators.BY_JOINING_DATE; break;
            default:
                ConsoleUtil.printError("Invalid choice.");
                input.pause();
                return;
        }

        List<Employee> sorted = employeeService.sortEmployees(comparator);
        if (sorted.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee e : sorted) {
                System.out.println(e.getDetails());
            }
        }
        input.pause();
    }

    private void employeeStatistics() {
        ConsoleUtil.printSubHeader("Employee Statistics");
        List<Employee> all = employeeService.getAllEmployees();
        long active = all.stream().filter(e -> e.getStatus() == EmployeeStatus.ACTIVE).count();
        long inactive = all.size() - active;
        double totalSalary = all.stream().mapToDouble(Employee::getBasicSalary).sum();
        double avgSalary = all.isEmpty() ? 0 : totalSalary / all.size();

        System.out.println("Total Employees   : " + all.size());
        System.out.println("Active Employees   : " + active);
        System.out.println("Inactive Employees : " + inactive);
        System.out.printf("Total Basic Salary : Rs. %.2f%n", totalSalary);
        System.out.printf("Average Basic Salary: Rs. %.2f%n", avgSalary);
        input.pause();
    }

    private void printEmployeeCard(Employee e) {
        System.out.println(ConsoleUtil.SUB_LINE);
        System.out.println("Employee ID       : " + e.getEmployeeId());
        System.out.println("Full Name         : " + e.getFullName());
        System.out.println("Age               : " + e.getAge());
        System.out.println("Gender            : " + e.getGender());
        System.out.println("Department        : " + e.getDepartment());
        System.out.println("Designation       : " + e.getDesignation());
        System.out.println("Email             : " + e.getEmail());
        System.out.println("Phone             : " + e.getPhoneNumber());
        System.out.println("Date of Joining   : " + e.getDateOfJoining());
        System.out.printf ("Basic Salary      : Rs. %.2f%n", e.getBasicSalary());
        System.out.println("Address           : " + e.getAddress());
        System.out.println("Blood Group       : " + e.getBloodGroup());
        System.out.println("Emergency Contact : " + e.getEmergencyContact());
        System.out.println("Status            : " + e.getStatus());
        System.out.println(ConsoleUtil.SUB_LINE);
    }
}
