package service;

import exception.InvalidInputException;
import model.Attendance;
import util.Constants;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class AttendanceServiceImpl implements AttendanceService {

    private final List<Attendance> records = new ArrayList<>();

    public AttendanceServiceImpl() {
        load();
    }

    @Override
    public Attendance recordAttendance(int employeeId, String month, int workingDays, int presentDays,
                                        int absentDays, int leaveDays, double permissionHours,
                                        int lateEntryCount) throws InvalidInputException {
        if (workingDays < 0 || presentDays < 0 || absentDays < 0 || leaveDays < 0
                || permissionHours < 0 || lateEntryCount < 0) {
            throw new InvalidInputException("Attendance figures cannot be negative.");
        }
        if (presentDays + absentDays + leaveDays > workingDays) {
            throw new InvalidInputException("Present + Absent + Leave days cannot exceed Working days.");
        }

        Attendance existing = getMonthlyAttendance(employeeId, month);
        if (existing != null) {
            records.remove(existing);
        }

        Attendance attendance = new Attendance(employeeId, month, workingDays);
        attendance.setPresentDays(presentDays);
        attendance.setAbsentDays(absentDays);
        attendance.setLeaveDays(leaveDays);
        attendance.setPermissionHours(permissionHours);
        attendance.setLateEntryCount(lateEntryCount);
        records.add(attendance);
        save();
        return attendance;
    }

    @Override
    public List<Attendance> getAttendanceForEmployee(int employeeId) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance a : records) {
            if (a.getEmployeeId() == employeeId) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public Attendance getMonthlyAttendance(int employeeId, String month) {
        for (Attendance a : records) {
            if (a.getEmployeeId() == employeeId && a.getMonth().equals(month)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public String generateMonthlyReport(String month) {
        StringBuilder sb = new StringBuilder();
        sb.append("Monthly Attendance Report - ").append(month).append("\n");
        sb.append("--------------------------------------------------------\n");
        boolean any = false;
        for (Attendance a : records) {
            if (a.getMonth().equals(month)) {
                sb.append("EmpID ").append(a.getEmployeeId()).append(" -> ").append(a).append("\n");
                any = true;
            }
        }
        if (!any) {
            sb.append("No attendance records found for this month.\n");
        }
        return sb.toString();
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Attendance a : records) {
            lines.add(a.getEmployeeId() + "|" + a.getMonth() + "|" + a.getWorkingDays() + "|"
                    + a.getPresentDays() + "|" + a.getAbsentDays() + "|" + a.getLeaveDays() + "|"
                    + a.getPermissionHours() + "|" + a.getLateEntryCount());
        }
        FileManager.writeAllLines(Constants.ATTENDANCE_FILE, lines);
    }

    private void load() {
        for (String line : FileManager.readAllLines(Constants.ATTENDANCE_FILE)) {
            try {
                String[] p = line.split("\\|", -1);
                Attendance a = new Attendance(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]));
                a.setPresentDays(Integer.parseInt(p[3]));
                a.setAbsentDays(Integer.parseInt(p[4]));
                a.setLeaveDays(Integer.parseInt(p[5]));
                a.setPermissionHours(Double.parseDouble(p[6]));
                a.setLateEntryCount(Integer.parseInt(p[7]));
                records.add(a);
            } catch (Exception ex) {
                System.out.println("Warning: skipping corrupt attendance record.");
            }
        }
    }
}
