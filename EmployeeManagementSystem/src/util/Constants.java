package util;

/** Application-wide constants (file paths, business-rule figures). */
public final class Constants {
    private Constants() { }

    public static final String DATA_DIR = "data";
    public static final String USERS_FILE = DATA_DIR + "/users.txt";
    public static final String EMPLOYEES_FILE = DATA_DIR + "/employees.txt";
    public static final String DEPARTMENTS_FILE = DATA_DIR + "/departments.txt";
    public static final String ATTENDANCE_FILE = DATA_DIR + "/attendance.txt";
    public static final String LEAVE_FILE = DATA_DIR + "/leave.txt";
    public static final String PAYROLL_FILE = DATA_DIR + "/payroll.txt";
    public static final String LOGIN_HISTORY_FILE = DATA_DIR + "/login_history.txt";
    public static final String BACKUP_DIR = DATA_DIR + "/backup";

    public static final int MAX_LOGIN_ATTEMPTS = 3;

    // Business rules (LOP - Loss of Pay)
    public static final double LOP_PER_LEAVE_DAY = 500.0;
    public static final double LOP_PER_PERMISSION_HOUR = 50.0;

    public static final double DEFAULT_HRA_PERCENT = 0.20;   // 20% of basic
    public static final double DEFAULT_DA_PERCENT = 0.10;    // 10% of basic
    public static final double DEFAULT_PF_PERCENT = 0.12;    // 12% of basic
    public static final double DEFAULT_TAX_PERCENT = 0.05;   // 5% of basic
}
