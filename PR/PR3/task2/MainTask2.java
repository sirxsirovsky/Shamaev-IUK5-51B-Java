package task2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MainTask2 {
    public static void main(String[] args) {
        final int NUM_CONSUMERS = 3;
        final int TOTAL_ORDERS = 10;

        Queue<String> orderQueue = new LinkedList<>();
        ReentrantLock lock = new ReentrantLock();
        AtomicInteger processedOrdersCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_CONSUMERS + 1);

        executor.submit(new OrderProducer(orderQueue, lock, TOTAL_ORDERS));

        List<Future<String>> consumerFutures = new ArrayList<>();
        for (int i = 1; i <= NUM_CONSUMERS; i++) {
            Callable<String> consumer = new OrderConsumer("Потребитель #" + i, orderQueue, lock, processedOrdersCount);
            consumerFutures.add(executor.submit(consumer));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("завершение.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("\n--- Результаты работы потребителей ---");
        for (Future<String> future : consumerFutures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Ошибка при получении результата от потребителя: " + e.getMessage());
            }
        }

        System.out.println("\nОбщее количество обработанных заказов: " + processedOrdersCount.get());
    }
}
