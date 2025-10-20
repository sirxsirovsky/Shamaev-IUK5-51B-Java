package task2;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class OrderProducer implements Runnable {
    private final Queue<String> orderQueue;
    private final ReentrantLock lock;
    private final int orderCount;

    public OrderProducer(Queue<String> orderQueue, ReentrantLock lock, int orderCount) {
        this.orderQueue = orderQueue;
        this.lock = lock;
        this.orderCount = orderCount;
    }

    @Override
    public void run() {
        for (int i = 1; i <= orderCount; i++) {
            String order = "Заказ #" + i;
            lock.lock();
            try {
                orderQueue.add(order);
                System.out.println("-> Производитель добавил: " + order);
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
