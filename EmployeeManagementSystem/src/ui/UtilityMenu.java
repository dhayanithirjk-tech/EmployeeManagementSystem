package ui;

import model.Employee;
import service.EmployeeService;
import util.Constants;
import util.FileManager;
import util.InputReader;

import java.util.ArrayList;
import java.util.List;

public class UtilityMenu {

    private final EmployeeService employeeService;
    private final InputReader input;

    public UtilityMenu(EmployeeService employeeService, InputReader input) {
        this.employeeService = employeeService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("SYSTEM UTILITIES");
            System.out.println(" 1. Backup Data");
            System.out.println(" 2. Restore Data");
            System.out.println(" 3. Export Employee List");
            System.out.println(" 4. Reset System");
            System.out.println(" 5. About");
            System.out.println(" 6. Help");
            System.out.println(" 7. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: backup(); break;
                case 2: restore(); break;
                case 3: exportEmployeeList(); break;
                case 4: reset(); break;
                case 5: about(); break;
                case 6: help(); break;
                case 7: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private void backup() {
        ConsoleUtil.printSubHeader("Backup Data");
        String[] files = { Constants.USERS_FILE, Constants.EMPLOYEES_FILE, Constants.DEPARTMENTS_FILE,
                Constants.ATTENDANCE_FILE, Constants.LEAVE_FILE, Constants.PAYROLL_FILE };
        boolean allOk = true;
        for (String f : files) {
            String fileName = f.substring(f.lastIndexOf('/') + 1);
            boolean ok = FileManager.copyFile(f, Constants.BACKUP_DIR + "/" + fileName);
            allOk &= ok;
        }
        if (allOk) {
            ConsoleUtil.printSuccess("Backup completed successfully to " + Constants.BACKUP_DIR);
        } else {
            ConsoleUtil.printInfo("Backup completed with some files skipped (they may not exist yet).");
        }
        input.pause();
    }

    private void restore() {
        ConsoleUtil.printSubHeader("Restore Data");
        String confirm = input.readLine("This will overwrite current data with the last backup. Continue? (yes/no): ");
        if (!confirm.equalsIgnoreCase("yes")) {
            ConsoleUtil.printInfo("Restore cancelled.");
            input.pause();
            return;
        }
        String[] files = { Constants.USERS_FILE, Constants.EMPLOYEES_FILE, Constants.DEPARTMENTS_FILE,
                Constants.ATTENDANCE_FILE, Constants.LEAVE_FILE, Constants.PAYROLL_FILE };
        boolean any = false;
        for (String f : files) {
            String fileName = f.substring(f.lastIndexOf('/') + 1);
            if (FileManager.copyFile(Constants.BACKUP_DIR + "/" + fileName, f)) {
                any = true;
            }
        }
        if (any) {
            ConsoleUtil.printSuccess("Restore completed. Please restart the application for all changes to fully apply.");
        } else {
            ConsoleUtil.printError("No backup files found in " + Constants.BACKUP_DIR);
        }
        input.pause();
    }

    private void exportEmployeeList() {
        ConsoleUtil.printSubHeader("Export Employee List");
        List<Employee> employees = employeeService.getAllEmployees();
        List<String> lines = new ArrayList<>();
        lines.add("Employee List Export");
        lines.add("=================================================");
        for (Employee e : employees) {
            lines.add(e.getDetails());
        }
        String path = "data/exports/employee_list_export.txt";
        FileManager.writeAllLines(path, lines);
        ConsoleUtil.printSuccess("Employee list exported to " + path);
        input.pause();
    }

    private void reset() {
        ConsoleUtil.printSubHeader("Reset System");
        String confirm = input.readLine("WARNING: This will erase ALL employee/department/attendance/leave/payroll data. Continue? (yes/no): ");
        if (!confirm.equalsIgnoreCase("yes")) {
            ConsoleUtil.printInfo("Reset cancelled.");
            input.pause();
            return;
        }
        FileManager.writeAllLines(Constants.EMPLOYEES_FILE, new ArrayList<>());
        FileManager.writeAllLines(Constants.DEPARTMENTS_FILE, new ArrayList<>());
        FileManager.writeAllLines(Constants.ATTENDANCE_FILE, new ArrayList<>());
        FileManager.writeAllLines(Constants.LEAVE_FILE, new ArrayList<>());
        FileManager.writeAllLines(Constants.PAYROLL_FILE, new ArrayList<>());
        ConsoleUtil.printSuccess("System data reset. Please restart the application.");
        input.pause();
    }

    private void about() {
        ConsoleUtil.printSubHeader("About");
        System.out.println("Employee Management System v1.0");
        System.out.println("Built with Core Java - Collections, OOP, SOLID, Design Patterns.");
        System.out.println("(c) 2026 - Educational / Portfolio Project.");
        input.pause();
    }

    private void help() {
        ConsoleUtil.printSubHeader("Help");
        System.out.println("Navigate using the numbered menu options.");
        System.out.println("Invalid entries are safely rejected and never crash the app.");
        System.out.println("Employee records, attendance, leave and payroll data are saved");
        System.out.println("automatically to the 'data' folder after every change.");
        input.pause();
    }
}
