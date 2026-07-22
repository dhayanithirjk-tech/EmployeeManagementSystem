package service;

import exception.LeaveException;
import model.LeaveRecord;
import model.LeaveType;
import java.util.List;

public interface LeaveService {
    LeaveRecord applyLeave(int employeeId, LeaveType type, int days, String appliedDate, String reason)
            throws LeaveException;
    void approveLeave(int leaveId) throws LeaveException;
    void rejectLeave(int leaveId) throws LeaveException;
    void cancelLeave(int leaveId) throws LeaveException;
    List<LeaveRecord> getLeaveHistory(int employeeId);
    int getLeaveBalance(int employeeId, LeaveType type);
}
