package ui;

import exception.EmployeeNotFoundException;
import exception.InvalidInputException;
import model.Employee;
import service.EmployeeService;
import util.InputReader;
import util.ValidationUtil;

import java.util.List;

public class SearchMenu {

    private final EmployeeService employeeService;
    private final InputReader input;

    public SearchMenu(EmployeeService employeeService, InputReader input) {
        this.employeeService = employeeService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("SEARCH EMPLOYEE");
            System.out.println(" 1. Search by Employee ID (Binary Search)");
            System.out.println(" 2. Search by Name (Linear Search)");
            System.out.println(" 3. Search by Department (Linear Search)");
            System.out.println(" 4. Search by Phone (HashMap-backed lookup)");
            System.out.println(" 5. Search by Email");
            System.out.println(" 6. Search by Designation");
            System.out.println(" 7. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            try {
                switch (choice) {
                    case 1: searchById(); break;
                    case 2: searchByName(); break;
                    case 3: searchByDepartment(); break;
                    case 4: searchByPhone(); break;
                    case 5: searchByEmail(); break;
                    case 6: searchByDesignation(); break;
                    case 7: back = true; break;
                    default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
                }
            } catch (EmployeeNotFoundException | InvalidInputException ex) {
                ConsoleUtil.printError(ex.getMessage());
                input.pause();
            }
        }
    }

    private void searchById() throws EmployeeNotFoundException, InvalidInputException {
        int id = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
        Employee e = employeeService.searchByIdBinary(id);
        System.out.println(e.getDetails());
        input.pause();
    }

    private void searchByName() {
        String name = input.readNonEmpty("Name (or part of it): ", "Name");
        List<Employee> results = employeeService.searchByName(name);
        printResults(results);
        input.pause();
    }

    private void searchByDepartment() {
        String dept = input.readNonEmpty("Department: ", "Department");
        List<Employee> results = employeeService.searchByDepartment(dept);
        printResults(results);
        input.pause();
    }

    private void searchByPhone() throws EmployeeNotFoundException {
        String phone = input.readLine("Phone Number: ");
        Employee e = employeeService.searchByPhone(phone);
        System.out.println(e.getDetails());
        input.pause();
    }

    private void searchByEmail() throws EmployeeNotFoundException {
        String email = input.readLine("Email: ");
        Employee e = employeeService.searchByEmail(email);
        System.out.println(e.getDetails());
        input.pause();
    }

    private void searchByDesignation() {
        String designation = input.readNonEmpty("Designation: ", "Designation");
        List<Employee> results = employeeService.searchByDesignation(designation);
        printResults(results);
        input.pause();
    }

    private void printResults(List<Employee> results) {
        if (results.isEmpty()) {
            System.out.println("No matching employees found.");
        } else {
            for (Employee e : results) {
                System.out.println(e.getDetails());
            }
        }
    }
}
