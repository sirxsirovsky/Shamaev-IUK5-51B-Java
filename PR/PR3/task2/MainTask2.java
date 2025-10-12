package task2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MainTask2 {

    private static final int NUM_CONSUMERS = 3;
    private static final int TOTAL_ORDERS = 10;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Queue<String> orderQueue = new LinkedList<>();
        ReentrantLock lock = new ReentrantLock();
        AtomicInteger processedOrdersCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_CONSUMERS + 1); // +1 для поставщика

        Runnable producerTask = () -> {
            for (int i = 1; i <= TOTAL_ORDERS; i++) {
                lock.lock();
                try {
                    String order = "Заказ #" + i;
                    orderQueue.add(order);
                    System.out.println("Поставщик добавил в очередь: " + order);
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        executor.submit(producerTask);

        List<Future<String>> consumerFutures = new ArrayList<>();

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            final int consumerId = i + 1;
            Callable<String> consumerTask = () -> {
                while (processedOrdersCount.get() < TOTAL_ORDERS) {
                    String order = null;
                    lock.lock();
                    try {
                        if (!orderQueue.isEmpty()) {
                            order = orderQueue.poll();
                        }
                    } finally {
                        lock.unlock();
                    }

                    if (order != null) {
                        System.out.println("Обработчик " + consumerId + " начал обрабатывать: " + order);
                        Thread.sleep(500 + (long)(Math.random() * 500));
                        processedOrdersCount.incrementAndGet();
                        System.out.println("Обработчик " + consumerId + " завершил обработку: " + order);
                    } else {
                        Thread.sleep(100);
                    }
                }
                return "Обработчик " + consumerId + " завершил свою работу.";
            };
            consumerFutures.add(executor.submit(consumerTask));
        }

        for (Future<String> future : consumerFutures) {
            System.out.println(future.get());
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("\nВсего обработано заказов: " + processedOrdersCount.get());
    }
}