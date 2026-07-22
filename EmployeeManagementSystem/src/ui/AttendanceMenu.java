package ui;

import exception.InvalidInputException;
import model.Attendance;
import service.AttendanceService;
import util.InputReader;
import util.ValidationUtil;

import java.util.List;

public class AttendanceMenu {

    private final AttendanceService attendanceService;
    private final InputReader input;

    public AttendanceMenu(AttendanceService attendanceService, InputReader input) {
        this.attendanceService = attendanceService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("ATTENDANCE MANAGEMENT");
            System.out.println(" 1. Record / Update Attendance");
            System.out.println(" 2. View Attendance History (Employee)");
            System.out.println(" 3. View Monthly Attendance (Employee)");
            System.out.println(" 4. Generate Monthly Attendance Report");
            System.out.println(" 5. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: recordAttendance(); break;
                case 2: viewHistory(); break;
                case 3: viewMonthly(); break;
                case 4: monthlyReport(); break;
                case 5: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private void recordAttendance() {
        ConsoleUtil.printSubHeader("Record / Update Attendance");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            String month = input.readNonEmpty("Month (yyyy-MM): ", "Month");
            int workingDays = ValidationUtil.parseInt(input.readLine("Working Days: "), "Working Days must be numeric.");
            int presentDays = ValidationUtil.parseInt(input.readLine("Present Days: "), "Present Days must be numeric.");
            int absentDays = ValidationUtil.parseInt(input.readLine("Absent Days: "), "Absent Days must be numeric.");
            int leaveDays = input.readValidatedLeaveDays("Leave Days: ");
            double permissionHours = input.readValidatedPermissionHours("Permission Hours: ");
            int lateEntries = ValidationUtil.parseInt(input.readLine("Late Entry Count: "), "Late Entry Count must be numeric.");

            attendanceService.recordAttendance(empId, month, workingDays, presentDays, absentDays,
                    leaveDays, permissionHours, lateEntries);
            ConsoleUtil.printSuccess("Attendance recorded for employee " + empId + " (" + month + ").");
        } catch (InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void viewHistory() {
        ConsoleUtil.printSubHeader("Attendance History");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            List<Attendance> records = attendanceService.getAttendanceForEmployee(empId);
            if (records.isEmpty()) {
                System.out.println("No attendance records found.");
            } else {
                for (Attendance a : records) {
                    System.out.println(a);
                }
            }
        } catch (InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void viewMonthly() {
        ConsoleUtil.printSubHeader("Monthly Attendance");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            String month = input.readNonEmpty("Month (yyyy-MM): ", "Month");
            Attendance a = attendanceService.getMonthlyAttendance(empId, month);
            if (a == null) {
                System.out.println("No attendance record found for this employee/month.");
            } else {
                System.out.println(a);
            }
        } catch (InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void monthlyReport() {
        ConsoleUtil.printSubHeader("Generate Monthly Attendance Report");
        String month = input.readNonEmpty("Month (yyyy-MM): ", "Month");
        System.out.println(attendanceService.generateMonthlyReport(month));
        input.pause();
    }
}
