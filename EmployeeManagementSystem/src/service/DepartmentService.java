package service;

import exception.DepartmentException;
import exception.InvalidInputException;
import model.Department;
import java.util.List;

public interface DepartmentService {
    Department addDepartment(String code, String name, String hod) throws DepartmentException, InvalidInputException;
    void updateDepartment(String code, String name, String hod) throws DepartmentException;
    void deleteDepartment(String code) throws DepartmentException;
    Department getDepartment(String code) throws DepartmentException;
    List<Department> getAllDepartments();
    void assignEmployee(String departmentCode, int employeeId) throws DepartmentException;
    int getEmployeeCount(String code) throws DepartmentException;
}
