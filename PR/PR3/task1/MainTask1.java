package task1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainTask1 {

    public static void main(String[] args) {
        createTestFiles();

        List<String> filePaths = List.of("employees1.txt", "employees2.txt", "employees3.txt");

        Map<String, FileStats> results = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(filePaths.size());

        System.out.println("Запускаем анализ файлов...");
        for (String path : filePaths) {
            executor.submit(new SalaryAnalyzerTask(path, results));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("Задачи не были выполнены за отведенное время.");
            }
        } catch (InterruptedException e) {
            System.err.println("Ожидание было прервано.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- Итоговые результаты анализа ---");
        results.forEach((filePath, stats) ->
                System.out.println("Файл '" + filePath + "': " + stats)
        );
    }

    private static void createTestFiles() {
        try (FileWriter writer1 = new FileWriter("employees1.txt")) {
            writer1.write("Иван Иванов,Инженер,70000\n");
            writer1.write("Петр Петров,Тестировщик,65000\n");
        } catch (IOException e) { e.printStackTrace(); }

        try (FileWriter writer2 = new FileWriter("employees2.txt")) {
            writer2.write("Анна Сидорова,Менеджер,85000\n");
            writer2.write("Мария Кузнецова,Аналитик,90000\n");
            writer2.write("Ольга Васильева,Дизайнер,75000\n");
        } catch (IOException e) { e.printStackTrace(); }

        try (FileWriter writer3 = new FileWriter("employees3.txt")) {
            writer3.write("Сергей Смирнов,Разработчик,120000\n");
        } catch (IOException e) { e.printStackTrace(); }
    }
}
