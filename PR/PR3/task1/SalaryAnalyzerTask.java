package task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class SalaryAnalyzerTask implements Runnable {
    private final String filePath;
    private final Map<String, FileStats> results;

    public SalaryAnalyzerTask(String filePath, Map<String, FileStats> results) {
        this.filePath = filePath;
        this.results = results;
    }

    @Override
    public void run() {
        long totalSalary = 0;
        int employeeCount = 0;
        System.out.println("Поток '" + Thread.currentThread().getName() + "' начал обработку файла: " + filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        totalSalary += Long.parseLong(parts[2].trim());
                        employeeCount++;
                    } catch (NumberFormatException e) {
                        System.err.println("Неверный формат зарплаты в строке: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла " + filePath + ": " + e.getMessage());
        }

        results.put(filePath, new FileStats(totalSalary, employeeCount));
        System.out.println("Поток '" + Thread.currentThread().getName() + "' закончил обработку файла: " + filePath);
    }
}
