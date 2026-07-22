package service;

public interface ReportService {
    String allEmployeesReport();
    String departmentWiseReport();
    String highestSalaryReport();
    String lowestSalaryReport();
    String averageSalaryReport();
    String employeesJoinedThisYearReport();
    String inactiveEmployeesReport();
    String salaryStatisticsReport();
}
