package repository;

import model.Employee;
import util.Constants;
import util.FileManager;

import java.util.*;

/**
 * In-memory {@link EmployeeRepository} implementation.
 * <p>
 * Demonstrates the JAVA COLLECTIONS FRAMEWORK: an {@link ArrayList} holds
 * the canonical ordered collection, while a {@link HashMap} gives O(1)
 * lookup by Employee ID. Data is mirrored to a flat file via
 * {@link FileManager} for persistence between runs.
 */
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final List<Employee> employeeList = new ArrayList<>();
    private final Map<Integer, Employee> employeeMap = new HashMap<>();

    public EmployeeRepositoryImpl() {
        loadFromFile();
    }

    @Override
    public void add(Employee employee) {
        employeeList.add(employee);
        employeeMap.put(employee.getEmployeeId(), employee);
        saveToFile();
    }

    @Override
    public void update(Employee employee) {
        int index = indexOf(employee.getEmployeeId());
        if (index >= 0) {
            employeeList.set(index, employee);
            employeeMap.put(employee.getEmployeeId(), employee);
            saveToFile();
        }
    }

    @Override
    public void delete(int employeeId) {
        employeeList.removeIf(e -> e.getEmployeeId() == employeeId);
        employeeMap.remove(employeeId);
        saveToFile();
    }

    @Override
    public Employee findById(int employeeId) {
        // HashMap lookup - O(1) average case.
        return employeeMap.get(employeeId);
    }

    @Override
    public boolean existsById(int employeeId) {
        return employeeMap.containsKey(employeeId);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employeeList);
    }

    private int indexOf(int employeeId) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getEmployeeId() == employeeId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Employee e : employeeList) {
            lines.add(e.toFileLine());
        }
        FileManager.writeAllLines(Constants.EMPLOYEES_FILE, lines);
    }

    @Override
    public void loadFromFile() {
        employeeList.clear();
        employeeMap.clear();
        List<String> lines = FileManager.readAllLines(Constants.EMPLOYEES_FILE);
        for (String line : lines) {
            try {
                Employee e = Employee.fromFileLine(line);
                employeeList.add(e);
                employeeMap.put(e.getEmployeeId(), e);
            } catch (Exception ex) {
                // Skip malformed lines rather than crashing the application.
                System.out.println("Warning: skipping corrupt employee record.");
            }
        }
    }
}
