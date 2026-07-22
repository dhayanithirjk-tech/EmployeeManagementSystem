package service;

import exception.DepartmentException;
import exception.InvalidInputException;
import model.Department;
import repository.DepartmentRepository;
import util.ValidationUtil;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Department addDepartment(String code, String name, String hod)
            throws DepartmentException, InvalidInputException {
        ValidationUtil.validateNotEmpty(code, "Department code");
        ValidationUtil.validateNotEmpty(name, "Department name");
        if (repository.existsByCode(code)) {
            throw new DepartmentException("Department code '" + code + "' already exists.");
        }
        Department d = new Department(code.toUpperCase(), name, hod);
        repository.add(d);
        return d;
    }

    @Override
    public void updateDepartment(String code, String name, String hod) throws DepartmentException {
        Department d = getDepartment(code);
        d.setName(name);
        d.setHeadOfDepartment(hod);
        repository.update(d);
    }

    @Override
    public void deleteDepartment(String code) throws DepartmentException {
        Department d = getDepartment(code);
        if (!d.getEmployeeIds().isEmpty()) {
            throw new DepartmentException("Cannot delete department '" + code
                    + "' - it still has " + d.getEmployeeIds().size() + " employee(s) assigned.");
        }
        repository.delete(code);
    }

    @Override
    public Department getDepartment(String code) throws DepartmentException {
        Department d = repository.findByCode(code == null ? null : code.toUpperCase());
        if (d == null) {
            throw new DepartmentException("Department '" + code + "' not found.");
        }
        return d;
    }

    @Override
    public List<Department> getAllDepartments() {
        return repository.findAll();
    }

    @Override
    public void assignEmployee(String departmentCode, int employeeId) throws DepartmentException {
        Department d = getDepartment(departmentCode);
        d.addEmployee(employeeId);
        repository.update(d);
    }

    @Override
    public int getEmployeeCount(String code) throws DepartmentException {
        return getDepartment(code).getEmployeeIds().size();
    }
}
