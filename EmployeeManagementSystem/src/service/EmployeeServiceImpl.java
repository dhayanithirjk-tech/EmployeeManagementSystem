package service;

import exception.*;
import factory.EmployeeFactory;
import model.Employee;
import repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default {@link EmployeeService} implementation.
 * <p>
 * DEPENDENCY INVERSION PRINCIPLE: depends on the {@link EmployeeRepository}
 * abstraction (injected via the constructor), not a concrete class.<br>
 * Uses {@link EmployeeFactory} (Factory Pattern) as the ONLY way Employee
 * objects get created, and demonstrates SORTING (Collections.sort with
 * Comparator) plus a hand-written BINARY SEARCH implementation.
 */
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee addEmployee(int id, String name, int age, String gender, String department,
                                 String designation, String email, String phone, String doj,
                                 double salary, String address, String bloodGroup, String emergencyContact)
            throws InvalidEmployeeException, DuplicateEmployeeException, InvalidInputException {

        if (repository.existsById(id)) {
            throw new DuplicateEmployeeException("Employee ID " + id + " already exists.");
        }

        // Factory Pattern: object creation is delegated, never done with 'new' here.
        Employee employee = EmployeeFactory.createEmployee(id, name, age, gender, department, designation,
                email, phone, doj, salary, address, bloodGroup, emergencyContact);

        repository.add(employee);
        return employee;
    }

    @Override
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        if (!repository.existsById(employee.getEmployeeId())) {
            throw new EmployeeNotFoundException("Employee ID " + employee.getEmployeeId() + " not found.");
        }
        repository.update(employee);
    }

    @Override
    public void deleteEmployee(int id) throws EmployeeNotFoundException {
        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee ID " + id + " not found.");
        }
        repository.delete(id);
    }

    @Override
    public Employee getEmployee(int id) throws EmployeeNotFoundException {
        Employee e = repository.findById(id);
        if (e == null) {
            throw new EmployeeNotFoundException("Employee ID " + id + " not found.");
        }
        return e;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    /** SORTING: delegates to Collections.sort with a caller-supplied Comparator. */
    @Override
    public List<Employee> sortEmployees(java.util.Comparator<Employee> comparator) {
        List<Employee> list = repository.findAll();
        Collections.sort(list, comparator);
        return list;
    }

    /**
     * BINARY SEARCH: the list is first sorted by ID (Employee implements
     * Comparable<Employee> using ID as the natural key), then searched
     * with a manual binary-search algorithm rather than a HashMap, to
     * explicitly demonstrate the algorithm as required.
     */
    @Override
    public Employee searchByIdBinary(int id) throws EmployeeNotFoundException {
        List<Employee> sorted = repository.findAll();
        Collections.sort(sorted); // uses Employee.compareTo -> sorted by ID

        int low = 0;
        int high = sorted.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int midId = sorted.get(mid).getEmployeeId();
            if (midId == id) {
                return sorted.get(mid);
            } else if (midId < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        throw new EmployeeNotFoundException("Employee ID " + id + " not found (binary search).");
    }

    /** LINEAR SEARCH by (partial, case-insensitive) name. */
    @Override
    public List<Employee> searchByName(String name) {
        List<Employee> results = new ArrayList<>();
        String needle = name.toLowerCase();
        for (Employee e : repository.findAll()) {
            if (e.getFullName().toLowerCase().contains(needle)) {
                results.add(e);
            }
        }
        return results;
    }

    @Override
    public List<Employee> searchByDepartment(String department) {
        List<Employee> results = new ArrayList<>();
        for (Employee e : repository.findAll()) {
            if (e.getDepartment().equalsIgnoreCase(department)) {
                results.add(e);
            }
        }
        return results;
    }

    @Override
    public Employee searchByPhone(String phone) throws EmployeeNotFoundException {
        for (Employee e : repository.findAll()) {
            if (e.getPhoneNumber().equals(phone)) {
                return e;
            }
        }
        throw new EmployeeNotFoundException("No employee found with phone " + phone);
    }

    @Override
    public Employee searchByEmail(String email) throws EmployeeNotFoundException {
        for (Employee e : repository.findAll()) {
            if (e.getEmail().equalsIgnoreCase(email)) {
                return e;
            }
        }
        throw new EmployeeNotFoundException("No employee found with email " + email);
    }

    @Override
    public List<Employee> searchByDesignation(String designation) {
        List<Employee> results = new ArrayList<>();
        for (Employee e : repository.findAll()) {
            if (e.getDesignation().equalsIgnoreCase(designation)) {
                results.add(e);
            }
        }
        return results;
    }
}
