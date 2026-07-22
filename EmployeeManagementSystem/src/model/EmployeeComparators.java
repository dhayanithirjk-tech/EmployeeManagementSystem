package model;

import java.util.Comparator;

/**
 * Centralised {@link Comparator} implementations for Employee, used by
 * "Sort Employees" and by Binary Search (which requires a sorted list).
 * Demonstrates the Comparator side of Comparable/Comparator usage.
 */
public final class EmployeeComparators {

    private EmployeeComparators() { }

    public static final Comparator<Employee> BY_ID =
            Comparator.comparingInt(Employee::getEmployeeId);

    public static final Comparator<Employee> BY_NAME =
            Comparator.comparing(Employee::getFullName, String.CASE_INSENSITIVE_ORDER);

    public static final Comparator<Employee> BY_SALARY =
            Comparator.comparingDouble(Employee::getBasicSalary);

    public static final Comparator<Employee> BY_SALARY_DESC = BY_SALARY.reversed();

    public static final Comparator<Employee> BY_DEPARTMENT =
            Comparator.comparing(Employee::getDepartment, String.CASE_INSENSITIVE_ORDER)
                      .thenComparing(BY_NAME);

    public static final Comparator<Employee> BY_JOINING_DATE =
            Comparator.comparing(Employee::getDateOfJoining);
}
