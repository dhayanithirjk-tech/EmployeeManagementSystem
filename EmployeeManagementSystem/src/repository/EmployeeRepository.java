package repository;

import model.Employee;
import java.util.List;

/**
 * INTERFACE SEGREGATION + DEPENDENCY INVERSION: services depend on this
 * abstraction, not on a concrete storage implementation. This is the
 * contract for persisting/retrieving Employee data, whatever the
 * underlying storage mechanism (in-memory collections, files, a DB...).
 */
public interface EmployeeRepository {
    void add(Employee employee);
    void update(Employee employee);
    void delete(int employeeId);
    Employee findById(int employeeId);
    boolean existsById(int employeeId);
    List<Employee> findAll();
    void saveToFile();
    void loadFromFile();
}
