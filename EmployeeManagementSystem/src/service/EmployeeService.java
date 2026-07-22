package service;

import exception.*;
import model.Employee;
import java.util.Comparator;
import java.util.List;

/** Business-logic contract for Employee Management (SRP / DIP: UI depends on this, not the impl). */
public interface EmployeeService {
    Employee addEmployee(int id, String name, int age, String gender, String department, String designation,
                          String email, String phone, String doj, double salary, String address,
                          String bloodGroup, String emergencyContact)
            throws InvalidEmployeeException, DuplicateEmployeeException, InvalidInputException;

    void updateEmployee(Employee employee) throws EmployeeNotFoundException;

    void deleteEmployee(int id) throws EmployeeNotFoundException;

    Employee getEmployee(int id) throws EmployeeNotFoundException;

    List<Employee> getAllEmployees();

    List<Employee> sortEmployees(Comparator<Employee> comparator);

    Employee searchByIdBinary(int id) throws EmployeeNotFoundException;

    List<Employee> searchByName(String name);

    List<Employee> searchByDepartment(String department);

    Employee searchByPhone(String phone) throws EmployeeNotFoundException;

    Employee searchByEmail(String email) throws EmployeeNotFoundException;

    List<Employee> searchByDesignation(String designation);
}
