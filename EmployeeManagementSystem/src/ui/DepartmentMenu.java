package ui;

import exception.DepartmentException;
import exception.InvalidInputException;
import model.Department;
import service.DepartmentService;
import util.InputReader;
import util.ValidationUtil;

import java.util.List;

public class DepartmentMenu {

    private final DepartmentService departmentService;
    private final InputReader input;

    public DepartmentMenu(DepartmentService departmentService, InputReader input) {
        this.departmentService = departmentService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("DEPARTMENT MANAGEMENT");
            System.out.println(" 1. Add Department");
            System.out.println(" 2. Update Department");
            System.out.println(" 3. Delete Department");
            System.out.println(" 4. View Departments");
            System.out.println(" 5. Assign Employee to Department");
            System.out.println(" 6. Department Employee Count");
            System.out.println(" 7. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: addDepartment(); break;
                case 2: updateDepartment(); break;
                case 3: deleteDepartment(); break;
                case 4: viewDepartments(); break;
                case 5: assignEmployee(); break;
                case 6: employeeCount(); break;
                case 7: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private void addDepartment() {
        ConsoleUtil.printSubHeader("Add Department");
        try {
            String code = input.readNonEmpty("Department Code: ", "Department Code");
            String name = input.readNonEmpty("Department Name: ", "Department Name");
            String hod = input.readNonEmpty("Head of Department: ", "Head of Department");
            Department d = departmentService.addDepartment(code, name, hod);
            ConsoleUtil.printSuccess("Department '" + d.getCode() + "' added successfully.");
        } catch (DepartmentException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void updateDepartment() {
        ConsoleUtil.printSubHeader("Update Department");
        try {
            String code = input.readNonEmpty("Department Code: ", "Department Code");
            String name = input.readNonEmpty("New Department Name: ", "Department Name");
            String hod = input.readNonEmpty("New Head of Department: ", "Head of Department");
            departmentService.updateDepartment(code, name, hod);
            ConsoleUtil.printSuccess("Department updated successfully.");
        } catch (DepartmentException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void deleteDepartment() {
        ConsoleUtil.printSubHeader("Delete Department");
        try {
            String code = input.readNonEmpty("Department Code: ", "Department Code");
            departmentService.deleteDepartment(code);
            ConsoleUtil.printSuccess("Department deleted successfully.");
        } catch (DepartmentException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void viewDepartments() {
        ConsoleUtil.printSubHeader("All Departments");
        List<Department> all = departmentService.getAllDepartments();
        if (all.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            for (Department d : all) {
                System.out.println(d);
            }
        }
        input.pause();
    }

    private void assignEmployee() {
        ConsoleUtil.printSubHeader("Assign Employee to Department");
        try {
            String code = input.readNonEmpty("Department Code: ", "Department Code");
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            departmentService.assignEmployee(code, empId);
            ConsoleUtil.printSuccess("Employee " + empId + " assigned to department '" + code + "'.");
        } catch (DepartmentException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void employeeCount() {
        ConsoleUtil.printSubHeader("Department Employee Count");
        try {
            String code = input.readNonEmpty("Department Code: ", "Department Code");
            int count = departmentService.getEmployeeCount(code);
            System.out.println("Department '" + code + "' has " + count + " employee(s) assigned.");
        } catch (DepartmentException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }
}
