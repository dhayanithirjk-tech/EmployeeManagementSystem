package repository;

import model.Department;
import util.Constants;
import util.FileManager;

import java.util.*;

/** In-memory {@link DepartmentRepository} implementation backed by a HashMap. */
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final Map<String, Department> departmentMap = new LinkedHashMap<>();

    public DepartmentRepositoryImpl() {
        loadFromFile();
    }

    @Override
    public void add(Department department) {
        departmentMap.put(department.getCode(), department);
        saveToFile();
    }

    @Override
    public void update(Department department) {
        departmentMap.put(department.getCode(), department);
        saveToFile();
    }

    @Override
    public void delete(String code) {
        departmentMap.remove(code);
        saveToFile();
    }

    @Override
    public Department findByCode(String code) {
        return departmentMap.get(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return departmentMap.containsKey(code);
    }

    @Override
    public List<Department> findAll() {
        return new ArrayList<>(departmentMap.values());
    }

    @Override
    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Department d : departmentMap.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(d.getCode()).append("|").append(d.getName()).append("|").append(d.getHeadOfDepartment());
            sb.append("|");
            List<Integer> ids = d.getEmployeeIds();
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(ids.get(i));
            }
            lines.add(sb.toString());
        }
        FileManager.writeAllLines(Constants.DEPARTMENTS_FILE, lines);
    }

    @Override
    public void loadFromFile() {
        departmentMap.clear();
        List<String> lines = FileManager.readAllLines(Constants.DEPARTMENTS_FILE);
        for (String line : lines) {
            try {
                String[] parts = line.split("\\|", -1);
                Department d = new Department(parts[0], parts[1], parts[2]);
                if (parts.length > 3 && !parts[3].isEmpty()) {
                    for (String idStr : parts[3].split(",")) {
                        if (!idStr.isEmpty()) {
                            d.addEmployee(Integer.parseInt(idStr));
                        }
                    }
                }
                departmentMap.put(d.getCode(), d);
            } catch (Exception ex) {
                System.out.println("Warning: skipping corrupt department record.");
            }
        }
    }
}
