package task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainTask1 {
    public static void main(String[] args) throws InterruptedException {
        // Список файлов для обработки
        List<String> fileNames = List.of("employee1.txt", "employee2.txt", "employee3.txt");

        // Используем ConcurrentHashMap для потокобезопасного хранения результатов
        Map<String, FileStats> results = new ConcurrentHashMap<>();

        List<Thread> threads = new ArrayList<>();

        // Создаем и запускаем потоки для каждого файла
        for (String fileName : fileNames) {
            FileProcessorTask task = new FileProcessorTask(fileName, results);
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        // Ожидаем завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("\n--- Результаты анализа файлов ---");
        results.forEach((fileName, stats) ->
                System.out.println("Файл: " + fileName + ", Результат: " + stats)
        );
    }
}
