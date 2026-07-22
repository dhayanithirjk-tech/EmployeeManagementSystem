package repository;

import model.Department;
import java.util.List;

/** Contract for persisting/retrieving Department data. */
public interface DepartmentRepository {
    void add(Department department);
    void update(Department department);
    void delete(String code);
    Department findByCode(String code);
    boolean existsByCode(String code);
    List<Department> findAll();
    void saveToFile();
    void loadFromFile();
}
