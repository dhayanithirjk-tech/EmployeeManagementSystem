package ui;

import exception.InvalidInputException;
import exception.LeaveException;
import model.LeaveRecord;
import model.LeaveType;
import service.LeaveService;
import util.Constants;
import util.InputReader;
import util.ValidationUtil;

import java.util.List;

public class LeaveMenu {

    private final LeaveService leaveService;
    private final InputReader input;

    public LeaveMenu(LeaveService leaveService, InputReader input) {
        this.leaveService = leaveService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("LEAVE MANAGEMENT");
            System.out.println(" 1. Apply Leave");
            System.out.println(" 2. Approve Leave");
            System.out.println(" 3. Reject Leave");
            System.out.println(" 4. View Leave History");
            System.out.println(" 5. Leave Balance");
            System.out.println(" 6. Cancel Leave");
            System.out.println(" 7. Calculate LOP (Loss of Pay)");
            System.out.println(" 8. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: applyLeave(); break;
                case 2: approveLeave(); break;
                case 3: rejectLeave(); break;
                case 4: viewHistory(); break;
                case 5: leaveBalance(); break;
                case 6: cancelLeave(); break;
                case 7: calculateLop(); break;
                case 8: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private LeaveType chooseLeaveType() {
        System.out.println("Leave Types: 1. Casual  2. Medical  3. Emergency  4. Loss of Pay");
        int t = input.readMenuChoice("Select leave type: ");
        switch (t) {
            case 1: return LeaveType.CASUAL_LEAVE;
            case 2: return LeaveType.MEDICAL_LEAVE;
            case 3: return LeaveType.EMERGENCY_LEAVE;
            case 4: return LeaveType.LOSS_OF_PAY_LEAVE;
            default: return null;
        }
    }

    private void applyLeave() {
        ConsoleUtil.printSubHeader("Apply Leave");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            LeaveType type = chooseLeaveType();
            if (type == null) {
                ConsoleUtil.printError("Invalid leave type selected.");
                input.pause();
                return;
            }
            int days = input.readValidatedLeaveDays("Number of Days: ");
            String date = input.readValidatedDate("Applied Date (yyyy-MM-dd): ");
            String reason = input.readNonEmpty("Reason: ", "Reason");

            LeaveRecord record = leaveService.applyLeave(empId, type, days, date, reason);
            ConsoleUtil.printSuccess("Leave applied successfully. Leave ID: " + record.getLeaveId());
        } catch (LeaveException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void approveLeave() {
        ConsoleUtil.printSubHeader("Approve Leave");
        try {
            int leaveId = ValidationUtil.parseInt(input.readLine("Leave ID: "), "Leave ID must be numeric.");
            leaveService.approveLeave(leaveId);
            ConsoleUtil.printSuccess("Leave " + leaveId + " approved.");
        } catch (LeaveException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void rejectLeave() {
        ConsoleUtil.printSubHeader("Reject Leave");
        try {
            int leaveId = ValidationUtil.parseInt(input.readLine("Leave ID: "), "Leave ID must be numeric.");
            leaveService.rejectLeave(leaveId);
            ConsoleUtil.printSuccess("Leave " + leaveId + " rejected.");
        } catch (LeaveException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void cancelLeave() {
        ConsoleUtil.printSubHeader("Cancel Leave");
        try {
            int leaveId = ValidationUtil.parseInt(input.readLine("Leave ID: "), "Leave ID must be numeric.");
            leaveService.cancelLeave(leaveId);
            ConsoleUtil.printSuccess("Leave " + leaveId + " cancelled.");
        } catch (LeaveException | InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void viewHistory() {
        ConsoleUtil.printSubHeader("Leave History");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            List<LeaveRecord> history = leaveService.getLeaveHistory(empId);
            if (history.isEmpty()) {
                System.out.println("No leave records found.");
            } else {
                for (LeaveRecord r : history) {
                    System.out.println(r);
                }
            }
        } catch (InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void leaveBalance() {
        ConsoleUtil.printSubHeader("Leave Balance");
        try {
            int empId = ValidationUtil.parseInt(input.readLine("Employee ID: "), "Employee ID must be numeric.");
            for (LeaveType type : LeaveType.values()) {
                if (type == LeaveType.LOSS_OF_PAY_LEAVE) continue;
                System.out.println(type + " balance: " + leaveService.getLeaveBalance(empId, type) + " day(s)");
            }
        } catch (InvalidInputException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    /** Business Rule: LOP = (LeaveDays * Rs.500) + (PermissionHours * Rs.50) */
    private void calculateLop() {
        ConsoleUtil.printSubHeader("Calculate Loss of Pay (LOP)");
        int leaveDays = input.readValidatedLeaveDays("Leave Days: ");
        double permissionHours = input.readValidatedPermissionHours("Permission Hours: ");
        double lop = (leaveDays * Constants.LOP_PER_LEAVE_DAY) + (permissionHours * Constants.LOP_PER_PERMISSION_HOUR);
        System.out.println();
        System.out.println("Leave Days        : " + leaveDays + " x Rs." + Constants.LOP_PER_LEAVE_DAY);
        System.out.println("Permission Hours   : " + permissionHours + " x Rs." + Constants.LOP_PER_PERMISSION_HOUR);
        System.out.printf ("Total LOP          : Rs. %.2f%n", lop);
        input.pause();
    }
}
