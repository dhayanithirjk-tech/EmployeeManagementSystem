package service;

import exception.InvalidInputException;
import model.Attendance;
import java.util.List;

public interface AttendanceService {
    Attendance recordAttendance(int employeeId, String month, int workingDays, int presentDays,
                                 int absentDays, int leaveDays, double permissionHours, int lateEntryCount)
            throws InvalidInputException;
    List<Attendance> getAttendanceForEmployee(int employeeId);
    Attendance getMonthlyAttendance(int employeeId, String month);
    String generateMonthlyReport(String month);
}
