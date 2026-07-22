import model.User;
import repository.DepartmentRepository;
import repository.DepartmentRepositoryImpl;
import repository.EmployeeRepository;
import repository.EmployeeRepositoryImpl;
import service.*;
import ui.LoginUI;
import ui.MainMenu;
import util.InputReader;

/**
 * Application entry point.
 * <p>
 * Responsible ONLY for composing the object graph (repositories -&gt;
 * services -&gt; UI) and running the top-level login/menu loop. All
 * actual behaviour lives in the service and ui packages - this class
 * demonstrates MODULAR PROGRAMMING and DEPENDENCY INVERSION by wiring
 * concrete implementations to abstractions in a single place.
 */
public class Main {

    public static void main(String[] args) {
        // ---- Repository layer (data access) ----
        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
        DepartmentRepository departmentRepository = new DepartmentRepositoryImpl();

        // ---- Service layer (business logic) ----
        EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
        DepartmentService departmentService = new DepartmentServiceImpl(departmentRepository);
        AttendanceService attendanceService = new AttendanceServiceImpl();
        LeaveService leaveService = new LeaveServiceImpl();
        SalaryCalculator salaryCalculator = new SalaryCalculatorImpl();
        PayrollService payrollService = new PayrollServiceImpl(employeeService, salaryCalculator);
        ReportService reportService = new ReportServiceImpl(employeeService);
        AuthenticationService authService = new AuthenticationServiceImpl();

        InputReader input = new InputReader();

        System.out.println();
        System.out.println("Starting Employee Management System...");

        // ---- Authentication loop: allow re-login after a logout, exit after Exit or lockout ----
        boolean keepRunning = true;
        while (keepRunning) {
            LoginUI loginUI = new LoginUI(authService, input);
            User currentUser = loginUI.login();

            if (currentUser == null) {
                // Max login attempts exceeded.
                keepRunning = false;
                break;
            }

            MainMenu mainMenu = new MainMenu(employeeService, departmentService, attendanceService,
                    leaveService, payrollService, salaryCalculator, reportService, authService,
                    input, currentUser);

            boolean exitRequested = mainMenu.show();
            if (exitRequested) {
                keepRunning = false;
            }
            // otherwise the user logged out -> loop back to the login screen
        }

        System.out.println();
        System.out.println("==============================================");
        System.out.println("   Thank you for using Employee Management");
        System.out.println("   System. Goodbye!");
        System.out.println("==============================================");
    }
}
