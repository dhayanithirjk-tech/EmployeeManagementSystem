package util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic file-handling utility used by every module that needs
 * persistence (users, employees, attendance, leave, payroll).
 * Creates directories/files automatically if they do not yet exist.
 */
public final class FileManager {

    private FileManager() { }

    /** Ensures the data directory and the given file both exist. */
    public static void ensureFileExists(String path) {
        try {
            Path filePath = Paths.get(path);
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException ex) {
            System.out.println("Warning: could not initialise file " + path + " (" + ex.getMessage() + ")");
        }
    }

    public static List<String> readAllLines(String path) {
        ensureFileExists(path);
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException ex) {
            System.out.println("Warning: could not read file " + path + " (" + ex.getMessage() + ")");
        }
        return lines;
    }

    /** Overwrites the file with the given lines. */
    public static void writeAllLines(String path, List<String> lines) {
        ensureFileExists(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Warning: could not write file " + path + " (" + ex.getMessage() + ")");
        }
    }

    /** Appends a single line to the file. */
    public static void appendLine(String path, String line) {
        ensureFileExists(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException ex) {
            System.out.println("Warning: could not append to file " + path + " (" + ex.getMessage() + ")");
        }
    }

    /** Copies a file (used for backup/restore). Overwrites destination if present. */
    public static boolean copyFile(String sourcePath, String destPath) {
        try {
            Path src = Paths.get(sourcePath);
            Path dst = Paths.get(destPath);
            if (dst.getParent() != null) {
                Files.createDirectories(dst.getParent());
            }
            if (!Files.exists(src)) {
                return false;
            }
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException ex) {
            System.out.println("Warning: copy failed for " + sourcePath + " (" + ex.getMessage() + ")");
            return false;
        }
    }
}
