package model;

import java.io.Serializable;

/**
 * Concrete Employee entity.
 * <p>
 * INHERITANCE: extends {@link Person}.<br>
 * POLYMORPHISM: overrides {@link #getDetails()} and {@link #toString()}.<br>
 * Implements {@link Comparable} so employees have a natural ordering
 * (by Employee ID) which is required before Binary Search can be used,
 * and can additionally be sorted via {@link java.util.Comparator}
 * instances supplied by callers (by name, salary, department, etc.).
 */
public class Employee extends Person implements Comparable<Employee>, Serializable {

    private static final long serialVersionUID = 1L;

    private final int employeeId;
    private String department;
    private String designation;
    private String dateOfJoining;   // stored as yyyy-MM-dd
    private double basicSalary;
    private String bloodGroup;
    private String emergencyContact;
    private EmployeeStatus status;

    public Employee(int employeeId, String fullName, int age, String gender,
                     String department, String designation, String email,
                     String phoneNumber, String dateOfJoining, double basicSalary,
                     String address, String bloodGroup, String emergencyContact,
                     EmployeeStatus status) {
        super(fullName, age, gender, email, phoneNumber, address);
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
        this.dateOfJoining = dateOfJoining;
        this.basicSalary = basicSalary;
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.status = status;
    }

    public int getEmployeeId() { return employeeId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(String dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }

    /** POLYMORPHISM: Employee-specific implementation of the abstract Person method. */
    @Override
    public String getDetails() {
        return String.format(
            "ID: %-6d Name: %-20s Dept: %-12s Designation: %-15s Salary: Rs.%-10.2f Status: %s",
            employeeId, getFullName(), department, designation, basicSalary, status);
    }

    /** Natural ordering by Employee ID - required for Binary Search / TreeSet use. */
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.employeeId, other.employeeId);
    }

    /** Serialised as a pipe-delimited line for simple flat-file persistence. */
    public String toFileLine() {
        return employeeId + "|" + getFullName() + "|" + getAge() + "|" + getGender() + "|"
                + department + "|" + designation + "|" + getEmail() + "|" + getPhoneNumber() + "|"
                + dateOfJoining + "|" + basicSalary + "|" + getAddress() + "|" + bloodGroup + "|"
                + emergencyContact + "|" + status;
    }

    /**
     * Reconstructs an Employee from a previously-saved file line.
     * <p>
     * NOTE: this deserialiser deliberately calls the constructor directly
     * rather than {@link factory.EmployeeFactory}. The Factory Pattern
     * requirement governs how NEW employees are created by the application
     * (see EmployeeService.addEmployee, which is the only path used by the
     * Add Employee menu); reloading already-validated, previously-persisted
     * records on startup is a plain deserialisation concern, not employee
     * "creation".
     */
    public static Employee fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        Employee e = new Employee(
                Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3],
                p[4], p[5], p[6], p[7], p[8], Double.parseDouble(p[9]),
                p[10], p[11], p[12], EmployeeStatus.valueOf(p[13]));
        return e;
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
