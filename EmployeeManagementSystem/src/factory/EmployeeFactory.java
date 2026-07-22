package factory;

import exception.InvalidEmployeeException;
import model.Employee;
import model.EmployeeStatus;

/**
 * FACTORY DESIGN PATTERN.
 * <p>
 * All {@link Employee} objects in the system must be created through
 * this factory rather than via {@code new Employee(...)} scattered
 * across the codebase. This centralises object-creation logic,
 * satisfies the Open/Closed Principle (new employee "kinds" or extra
 * construction rules can be added here without touching callers), and
 * gives one place to enforce invariants at creation time.
 */
public final class EmployeeFactory {

    private EmployeeFactory() { }

    public static Employee createEmployee(int employeeId, String fullName, int age, String gender,
                                            String department, String designation, String email,
                                            String phoneNumber, String dateOfJoining, double basicSalary,
                                            String address, String bloodGroup, String emergencyContact)
            throws InvalidEmployeeException {

        if (employeeId <= 0) {
            throw new InvalidEmployeeException("Employee ID must be a positive number.");
        }
        if (basicSalary < 0) {
            throw new InvalidEmployeeException("Basic salary cannot be negative.");
        }

        return new Employee(employeeId, fullName, age, gender, department, designation, email,
                phoneNumber, dateOfJoining, basicSalary, address, bloodGroup, emergencyContact,
                EmployeeStatus.ACTIVE);
    }

    /** Overload used when rebuilding an Employee from a persisted file line (status included). */
    public static Employee createEmployee(int employeeId, String fullName, int age, String gender,
                                            String department, String designation, String email,
                                            String phoneNumber, String dateOfJoining, double basicSalary,
                                            String address, String bloodGroup, String emergencyContact,
                                            EmployeeStatus status) throws InvalidEmployeeException {
        if (employeeId <= 0) {
            throw new InvalidEmployeeException("Employee ID must be a positive number.");
        }
        if (basicSalary < 0) {
            throw new InvalidEmployeeException("Basic salary cannot be negative.");
        }
        return new Employee(employeeId, fullName, age, gender, department, designation, email,
                phoneNumber, dateOfJoining, basicSalary, address, bloodGroup, emergencyContact, status);
    }
}
