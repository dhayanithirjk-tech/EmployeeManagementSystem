package service;

import exception.LeaveException;
import model.LeaveRecord;
import model.LeaveStatus;
import model.LeaveType;
import util.Constants;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class LeaveServiceImpl implements LeaveService {

    /** Annual entitlement per leave type, used to compute remaining balance. */
    private static final int ANNUAL_ENTITLEMENT = 12;

    private final List<LeaveRecord> leaveRecords = new ArrayList<>();

    public LeaveServiceImpl() {
        load();
    }

    @Override
    public LeaveRecord applyLeave(int employeeId, LeaveType type, int days, String appliedDate, String reason)
            throws LeaveException {
        if (days <= 0) {
            throw new LeaveException("Leave days must be greater than zero.");
        }
        if (type != LeaveType.LOSS_OF_PAY_LEAVE && getLeaveBalance(employeeId, type) < days) {
            throw new LeaveException("Insufficient leave balance for " + type
                    + ". Available: " + getLeaveBalance(employeeId, type) + " day(s).");
        }
        LeaveRecord record = new LeaveRecord(employeeId, type, days, appliedDate, reason);
        leaveRecords.add(record);
        save();
        return record;
    }

    @Override
    public void approveLeave(int leaveId) throws LeaveException {
        LeaveRecord record = findById(leaveId);
        record.setStatus(LeaveStatus.APPROVED);
        save();
    }

    @Override
    public void rejectLeave(int leaveId) throws LeaveException {
        LeaveRecord record = findById(leaveId);
        record.setStatus(LeaveStatus.REJECTED);
        save();
    }

    @Override
    public void cancelLeave(int leaveId) throws LeaveException {
        LeaveRecord record = findById(leaveId);
        record.setStatus(LeaveStatus.CANCELLED);
        save();
    }

    @Override
    public List<LeaveRecord> getLeaveHistory(int employeeId) {
        List<LeaveRecord> result = new ArrayList<>();
        for (LeaveRecord r : leaveRecords) {
            if (r.getEmployeeId() == employeeId) {
                result.add(r);
            }
        }
        return result;
    }

    @Override
    public int getLeaveBalance(int employeeId, LeaveType type) {
        int used = 0;
        for (LeaveRecord r : leaveRecords) {
            if (r.getEmployeeId() == employeeId && r.getLeaveType() == type
                    && r.getStatus() == LeaveStatus.APPROVED) {
                used += r.getNumberOfDays();
            }
        }
        return Math.max(0, ANNUAL_ENTITLEMENT - used);
    }

    private LeaveRecord findById(int leaveId) throws LeaveException {
        for (LeaveRecord r : leaveRecords) {
            if (r.getLeaveId() == leaveId) {
                return r;
            }
        }
        throw new LeaveException("Leave ID " + leaveId + " not found.");
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (LeaveRecord r : leaveRecords) {
            lines.add(r.getLeaveId() + "|" + r.getEmployeeId() + "|" + r.getLeaveType() + "|"
                    + r.getNumberOfDays() + "|" + r.getAppliedDate() + "|" + r.getReason() + "|" + r.getStatus());
        }
        FileManager.writeAllLines(Constants.LEAVE_FILE, lines);
    }

    private void load() {
        for (String line : FileManager.readAllLines(Constants.LEAVE_FILE)) {
            try {
                String[] p = line.split("\\|", -1);
                LeaveRecord r = new LeaveRecord(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                        LeaveType.valueOf(p[2]), Integer.parseInt(p[3]), p[4], p[5],
                        LeaveStatus.valueOf(p[6]));
                leaveRecords.add(r);
            } catch (Exception ex) {
                System.out.println("Warning: skipping corrupt leave record.");
            }
        }
    }
}
