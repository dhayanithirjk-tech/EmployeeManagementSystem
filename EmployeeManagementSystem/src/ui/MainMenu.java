package ui;

import model.User;
import service.*;
import util.InputReader;

/**
 * Top-level application menu. Wires together every service via
 * constructor-style composition (Dependency Inversion: MainMenu depends
 * on the service interfaces, injected from Main) and dispatches to the
 * relevant submenu.
 */
public class MainMenu {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;
    private final PayrollService payrollService;
    private final SalaryCalculator salaryCalculator;
    private final ReportService reportService;
    private final AuthenticationService authService;
    private final InputReader input;

    private User currentUser;

    public MainMenu(EmployeeService employeeService, DepartmentService departmentService,
                     AttendanceService attendanceService, LeaveService leaveService,
                     PayrollService payrollService, SalaryCalculator salaryCalculator,
                     ReportService reportService, AuthenticationService authService,
                     InputReader input, User currentUser) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.attendanceService = attendanceService;
        this.leaveService = leaveService;
        this.payrollService = payrollService;
        this.salaryCalculator = salaryCalculator;
        this.reportService = reportService;
        this.authService = authService;
        this.input = input;
        this.currentUser = currentUser;
    }

    /** Returns true if the user chose Exit (full application shutdown), false if they logged out. */
    public boolean show() {
        boolean exitApp = false;
        boolean loggedOut = false;

        while (!exitApp && !loggedOut) {
            ConsoleUtil.printHeader("EMPLOYEE MANAGEMENT SYSTEM");
            System.out.println(" 1. Employee Management");
            System.out.println(" 2. Department Management");
            System.out.println(" 3. Attendance Management");
            System.out.println(" 4. Leave Management");
            System.out.println(" 5. Payroll Management");
            System.out.println(" 6. Salary Calculator");
            System.out.println(" 7. Search Employee");
            System.out.println(" 8. Reports");
            System.out.println(" 9. User Account");
            System.out.println("10. System Utilities");
            System.out.println("11. Exit");
            System.out.println(ConsoleUtil.LINE);
            System.out.println("Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: new EmployeeMenu(employeeService, input).show(); break;
                case 2: new DepartmentMenu(departmentService, input).show(); break;
                case 3: new AttendanceMenu(attendanceService, input).show(); break;
                case 4: new LeaveMenu(leaveService, input).show(); break;
                case 5: new PayrollMenu(payrollService, employeeService, input).show(); break;
                case 6: new SalaryCalculatorMenu(salaryCalculator, input).show(); break;
                case 7: new SearchMenu(employeeService, input).show(); break;
                case 8: new ReportMenu(reportService, attendanceService, input).show(); break;
                case 9:
                    boolean stillLoggedIn = new UserAccountMenu(authService, input, currentUser).show();
                    if (!stillLoggedIn) {
                        loggedOut = true;
                    }
                    break;
                case 10: new UtilityMenu(employeeService, input).show(); break;
                case 11:
                    String confirm = input.readLine("Are you sure you want to exit? (yes/no): ");
                    if (confirm.equalsIgnoreCase("yes")) {
                        exitApp = true;
                    }
                    break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
        return exitApp;
    }
}
