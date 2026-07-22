package ui;

import service.AttendanceService;
import service.ReportService;
import util.InputReader;

public class ReportMenu {

    private final ReportService reportService;
    private final AttendanceService attendanceService;
    private final InputReader input;

    public ReportMenu(ReportService reportService, AttendanceService attendanceService, InputReader input) {
        this.reportService = reportService;
        this.attendanceService = attendanceService;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("REPORTS");
            System.out.println(" 1. All Employees");
            System.out.println(" 2. Department-wise Employees");
            System.out.println(" 3. Highest Salary");
            System.out.println(" 4. Lowest Salary");
            System.out.println(" 5. Average Salary");
            System.out.println(" 6. Attendance Report");
            System.out.println(" 7. Employees Joined This Year");
            System.out.println(" 8. Inactive Employees");
            System.out.println(" 9. Salary Statistics");
            System.out.println("10. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: print(reportService.allEmployeesReport()); break;
                case 2: print(reportService.departmentWiseReport()); break;
                case 3: print(reportService.highestSalaryReport()); break;
                case 4: print(reportService.lowestSalaryReport()); break;
                case 5: print(reportService.averageSalaryReport()); break;
                case 6: attendanceReport(); break;
                case 7: print(reportService.employeesJoinedThisYearReport()); break;
                case 8: print(reportService.inactiveEmployeesReport()); break;
                case 9: print(reportService.salaryStatisticsReport()); break;
                case 10: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
    }

    private void attendanceReport() {
        String month = input.readNonEmpty("Month (yyyy-MM): ", "Month");
        print(attendanceService.generateMonthlyReport(month));
    }

    private void print(String content) {
        System.out.println();
        System.out.println(content);
        input.pause();
    }
}
