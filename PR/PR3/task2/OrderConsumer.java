package task2;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class OrderConsumer implements Callable<String> {
    private final Queue<String> orderQueue;
    private final ReentrantLock lock;
    private final AtomicInteger processedOrdersCount;
    private final String consumerId;

    public OrderConsumer(String id, Queue<String> orderQueue, ReentrantLock lock, AtomicInteger processedOrdersCount) {
        this.consumerId = id;
        this.orderQueue = orderQueue;
        this.lock = lock;
        this.processedOrdersCount = processedOrdersCount;
    }

    @Override
    public String call() throws Exception {
        int ordersProcessedByThisConsumer = 0;
        while (true) {
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
                System.out.println("<- " + consumerId + " обрабатывает: " + order);
                Thread.sleep(500);
                processedOrdersCount.incrementAndGet();
                ordersProcessedByThisConsumer++;
            } else {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
        }
        return consumerId + " обработал " + ordersProcessedByThisConsumer + " заказов.";
    }
}