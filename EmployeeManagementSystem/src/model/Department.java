package model;

import java.util.ArrayList;
import java.util.List;

/** Represents a Department that groups employees together. */
public class Department {

    private final String code;
    private String name;
    private String headOfDepartment;
    private final List<Integer> employeeIds = new ArrayList<>();

    public Department(String code, String name, String headOfDepartment) {
        this.code = code;
        this.name = name;
        this.headOfDepartment = headOfDepartment;
    }

    public String getCode() { return code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHeadOfDepartment() { return headOfDepartment; }
    public void setHeadOfDepartment(String headOfDepartment) { this.headOfDepartment = headOfDepartment; }

    public List<Integer> getEmployeeIds() { return employeeIds; }

    public void addEmployee(int employeeId) {
        if (!employeeIds.contains(employeeId)) {
            employeeIds.add(employeeId);
        }
    }

    public void removeEmployee(int employeeId) {
        employeeIds.remove(Integer.valueOf(employeeId));
    }

    @Override
    public String toString() {
        return String.format("Code: %-8s Name: %-20s HOD: %-15s Employees: %d",
                code, name, headOfDepartment, employeeIds.size());
    }
}
