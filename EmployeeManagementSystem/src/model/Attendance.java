package model;

/** Monthly attendance record for a single employee. */
public class Attendance {

    private final int employeeId;
    private final String month; // e.g. "2026-07"
    private int workingDays;
    private int presentDays;
    private int absentDays;
    private int leaveDays;
    private double permissionHours;
    private int lateEntryCount;

    public Attendance(int employeeId, String month, int workingDays) {
        this.employeeId = employeeId;
        this.month = month;
        this.workingDays = workingDays;
    }

    public int getEmployeeId() { return employeeId; }
    public String getMonth() { return month; }

    public int getWorkingDays() { return workingDays; }
    public void setWorkingDays(int workingDays) { this.workingDays = workingDays; }

    public int getPresentDays() { return presentDays; }
    public void setPresentDays(int presentDays) { this.presentDays = presentDays; }

    public int getAbsentDays() { return absentDays; }
    public void setAbsentDays(int absentDays) { this.absentDays = absentDays; }

    public int getLeaveDays() { return leaveDays; }
    public void setLeaveDays(int leaveDays) { this.leaveDays = leaveDays; }

    public double getPermissionHours() { return permissionHours; }
    public void setPermissionHours(double permissionHours) { this.permissionHours = permissionHours; }

    public int getLateEntryCount() { return lateEntryCount; }
    public void setLateEntryCount(int lateEntryCount) { this.lateEntryCount = lateEntryCount; }

    @Override
    public String toString() {
        return String.format(
            "Month: %s | Working: %d Present: %d Absent: %d Leave: %d Permission(hrs): %.1f LateEntries: %d",
            month, workingDays, presentDays, absentDays, leaveDays, permissionHours, lateEntryCount);
    }
}
