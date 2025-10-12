package task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class FileStats {
    private final long totalSalary;
    private final int employeeCount;

    public FileStats(long totalSalary, int employeeCount) {
        this.totalSalary = totalSalary;
        this.employeeCount = employeeCount;
    }

    @Override
    public String toString() {
        return "FileStats{" +
                "totalSalary=" + totalSalary +
                ", employeeCount=" + employeeCount +
                '}';
    }
}
