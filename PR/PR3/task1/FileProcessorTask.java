package task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class FileProcessorTask implements Runnable {
    private final String fileName;
    private final Map<String, FileStats> resultsMap;

    public FileProcessorTask(String fileName, Map<String, FileStats> resultsMap) {
        this.fileName = fileName;
        this.resultsMap = resultsMap;
    }

    @Override
    public void run() {
        long totalSalary = 0;
        int employeeCount = 0;

        System.out.println("Поток " + Thread.currentThread().getName() + " начал обработку файла " + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    try {
                        totalSalary += Long.parseLong(parts[2].trim());
                        employeeCount++;
                    } catch (NumberFormatException e) {
                        System.err.println("Неверный формат зарплаты в файле " + fileName + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сохраняем результат в потокобезопасную Map
        resultsMap.put(fileName, new FileStats(totalSalary, employeeCount));
        System.out.println("Поток " + Thread.currentThread().getName() + " завершил обработку файла " + fileName);
    }
}
