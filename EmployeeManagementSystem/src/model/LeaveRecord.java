package model;

/** A single leave application raised by an employee. */
public class LeaveRecord {

    private static int sequence = 1000;

    private final int leaveId;
    private final int employeeId;
    private final LeaveType leaveType;
    private final int numberOfDays;
    private final String appliedDate;
    private String reason;
    private LeaveStatus status;

    public LeaveRecord(int employeeId, LeaveType leaveType, int numberOfDays,
                        String appliedDate, String reason) {
        this.leaveId = ++sequence;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.numberOfDays = numberOfDays;
        this.appliedDate = appliedDate;
        this.reason = reason;
        this.status = LeaveStatus.PENDING;
    }

    /**
     * Reconstructs a LeaveRecord with a KNOWN, already-assigned ID - used only
     * when reloading persisted leave records from file, so IDs remain stable
     * across application restarts. Also advances the ID sequence past this
     * value so newly-applied leaves never collide with a reloaded ID.
     */
    public LeaveRecord(int leaveId, int employeeId, LeaveType leaveType, int numberOfDays,
                        String appliedDate, String reason, LeaveStatus status) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.numberOfDays = numberOfDays;
        this.appliedDate = appliedDate;
        this.reason = reason;
        this.status = status;
        if (leaveId > sequence) {
            sequence = leaveId;
        }
    }

    public int getLeaveId() { return leaveId; }
    public int getEmployeeId() { return employeeId; }
    public LeaveType getLeaveType() { return leaveType; }
    public int getNumberOfDays() { return numberOfDays; }
    public String getAppliedDate() { return appliedDate; }
    public String getReason() { return reason; }

    public LeaveStatus getStatus() { return status; }
    public void setStatus(LeaveStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("LeaveID: %d | EmpID: %d | Type: %s | Days: %d | Applied: %s | Status: %s | Reason: %s",
                leaveId, employeeId, leaveType, numberOfDays, appliedDate, status, reason);
    }
}
