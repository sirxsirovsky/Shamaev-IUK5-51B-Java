import java.util.Random;

/**
 * Лабораторная работа №5 - Многопоточность
 * Запрещено использование java.util.concurrent
 */
public class Lab5Solutions {

    public static void main(String[] args) {
        System.out.println("=== Лабораторная работа №5: Многопоточность ===\n");

        System.out.println("--- Задание 1: Поиск максимума в массиве ---");
        solveTask1();
        System.out.println("----------------------------------------\n");

        System.out.println("--- Задание 2: Банковские переводы ---");
        solveTask2();
        System.out.println("----------------------------------------\n");

        System.out.println("--- Задание 3: Симуляция ресторана ---");
        solveTask3();
        System.out.println("----------------------------------------\n");
    }


    public static void solveTask1() {
        int[] array = new int[1_000_000];
        Random random = new Random(42);
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10_000_000);
        }

        int numThreads = 4;
        MaxFinderThread[] threads = new MaxFinderThread[numThreads];
        int chunkSize = array.length / numThreads;

        // Создаем и запускаем потоки
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize;
            threads[i] = new MaxFinderThread(array, start, end, i + 1);
            threads[i].start();
        }

        // Ожидаем завершения всех потоков
        int globalMax = Integer.MIN_VALUE;
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
                System.out.println("Поток " + (i + 1) + " завершен. Локальный максимум: " + threads[i].getLocalMax());
                if (threads[i].getLocalMax() > globalMax) {
                    globalMax = threads[i].getLocalMax();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nГлобальный максимум массива: " + globalMax);

        // Проверка однопоточным методом
        int verifyMax = Integer.MIN_VALUE;
        for (int val : array) {
            if (val > verifyMax) verifyMax = val;
        }
        System.out.println("Проверка (однопоточно): " + verifyMax);
    }


    public static void solveTask2() {
        int numAccounts = 5;
        int numClients = 3;
        int maxTransactionsPerClient = 10;
        long transactionTimeWindowMs = 2000;

        BankAccount[] accounts = new BankAccount[numAccounts];
        for (int i = 0; i < numAccounts; i++) {
            accounts[i] = new BankAccount(i + 1, 1000); // Начальный баланс 1000
        }

        System.out.println("Начальное состояние счетов:");
        for (BankAccount acc : accounts) {
            System.out.println("  " + acc);
        }
        System.out.println();

        BankClient[] clients = new BankClient[numClients];
        for (int i = 0; i < numClients; i++) {
            clients[i] = new BankClient("Клиент-" + (i + 1), accounts, maxTransactionsPerClient, transactionTimeWindowMs);
            clients[i].start();
        }

        // Ожидаем завершения всех клиентов
        for (BankClient client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nКонечное состояние счетов:");
        int totalBalance = 0;
        for (BankAccount acc : accounts) {
            System.out.println("  " + acc);
            totalBalance += acc.getBalance();
        }
        System.out.println("Общий баланс: " + totalBalance + " (должен быть " + (numAccounts * 1000) + ")");
    }


    public static void solveTask3() {
        int maxQueueSize = 5;
        int numWaiters = 2;
        int numChefs = 3;
        int ordersPerWaiter = 4;
        int maxDishesPerChef = 2;

        OrderQueue orderQueue = new OrderQueue(maxQueueSize);
        ReadyOrderQueue readyQueue = new ReadyOrderQueue();

        // Создаем и запускаем поваров
        Chef[] chefs = new Chef[numChefs];
        for (int i = 0; i < numChefs; i++) {
            chefs[i] = new Chef("Повар-" + (i + 1), orderQueue, readyQueue, maxDishesPerChef);
            chefs[i].start();
        }

        // Создаем и запускаем официантов
        Waiter[] waiters = new Waiter[numWaiters];
        for (int i = 0; i < numWaiters; i++) {
            waiters[i] = new Waiter("Официант-" + (i + 1), orderQueue, readyQueue, ordersPerWaiter);
            waiters[i].start();
        }

        // Ожидаем завершения официантов
        for (Waiter waiter : waiters) {
            try {
                waiter.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Сигнализируем поварам о завершении работы
        orderQueue.shutdown();

        // Ожидаем завершения поваров
        for (Chef chef : chefs) {
            try {
                chef.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\nРесторан закрыт. Все заказы обработаны.");
    }
}


class MaxFinderThread extends Thread {
    private final int[] array;
    private final int start;
    private final int end;
    private final int threadId;
    private int localMax;

    public MaxFinderThread(int[] array, int start, int end, int threadId) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.threadId = threadId;
        this.localMax = Integer.MIN_VALUE;
    }

    @Override
    public void run() {
        System.out.println("Поток " + threadId + " обрабатывает индексы [" + start + ", " + end + ")");
        for (int i = start; i < end; i++) {
            if (array[i] > localMax) {
                localMax = array[i];
            }
        }
    }

    public int getLocalMax() {
        return localMax;
    }
}


class BankAccount {
    private final int id;
    private int balance;

    public BankAccount(int id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public synchronized boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    @Override
    public String toString() {
        return "Счет #" + id + ": баланс = " + balance;
    }
}

class BankClient extends Thread {
    private final String clientName;
    private final BankAccount[] accounts;
    private final int maxTransactions;
    private final long timeWindowMs;
    private final Random random;

    public BankClient(String clientName, BankAccount[] accounts, int maxTransactions, long timeWindowMs) {
        this.clientName = clientName;
        this.accounts = accounts;
        this.maxTransactions = maxTransactions;
        this.timeWindowMs = timeWindowMs;
        this.random = new Random();
    }

    @Override
    public void run() {
        int transactionCount = 0;
        long startTime = System.currentTimeMillis();

        while (!Thread.currentThread().isInterrupted()) {
            // Проверяем лимит транзакций за временной промежуток
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < timeWindowMs && transactionCount >= maxTransactions) {
                System.out.println(clientName + " ПРЕРВАН: превышен лимит (" + maxTransactions + " транзакций за " + elapsed + " мс)");
                Thread.currentThread().interrupt();
            }

            if (elapsed >= timeWindowMs) {
                startTime = System.currentTimeMillis();
                transactionCount = 0;
            }

            int fromIndex = random.nextInt(accounts.length);
            int toIndex;
            do {
                toIndex = random.nextInt(accounts.length);
            } while (toIndex == fromIndex);

            // Случайная сумма перевода
            int amount = random.nextInt(100) + 1;

            // Выполняем перевод с синхронизацией
            boolean success = transfer(accounts[fromIndex], accounts[toIndex], amount);

            if (success) {
                System.out.println(clientName + ": перевел " + amount + " со счета #" + 
                    accounts[fromIndex].getId() + " на счет #" + accounts[toIndex].getId());
            }

            transactionCount++;

            try {
                Thread.sleep(random.nextInt(100) + 50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private boolean transfer(BankAccount from, BankAccount to, int amount) {
        // Упорядочиваем блокировки по ID для избежания deadlock
        BankAccount first = from.getId() < to.getId() ? from : to;
        BankAccount second = from.getId() < to.getId() ? to : from;

        synchronized (first) {
            synchronized (second) {
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                    return true;
                }
                return false;
            }
        }
    }
}


class Dish {
    private final String name;
    private final int cookingTimeMs;

    public Dish(String name, int cookingTimeMs) {
        this.name = name;
        this.cookingTimeMs = cookingTimeMs;
    }

    public String getName() {
        return name;
    }

    public int getCookingTimeMs() {
        return cookingTimeMs;
    }

    @Override
    public String toString() {
        return name + " (" + cookingTimeMs + " мс)";
    }
}

class Order {
    private static int counter = 0;
    private final int orderId;
    private final Dish[] dishes;
    private final String waiterName;

    public Order(Dish[] dishes, String waiterName) {
        synchronized (Order.class) {
            this.orderId = ++counter;
        }
        this.dishes = dishes;
        this.waiterName = waiterName;
    }

    public int getOrderId() {
        return orderId;
    }

    public Dish[] getDishes() {
        return dishes;
    }

    public String getWaiterName() {
        return waiterName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Заказ #" + orderId + " [");
        for (int i = 0; i < dishes.length; i++) {
            sb.append(dishes[i].getName());
            if (i < dishes.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

class OrderQueue {
    private final Order[] queue;
    private int head = 0;
    private int tail = 0;
    private int count = 0;
    private boolean shutdown = false;

    public OrderQueue(int capacity) {
        this.queue = new Order[capacity];
    }

    public synchronized void addOrder(Order order) throws InterruptedException {
        while (count == queue.length && !shutdown) {
            System.out.println("  [Очередь] Полная очередь, ожидание...");
            wait();
        }
        if (shutdown) return;

        queue[tail] = order;
        tail = (tail + 1) % queue.length;
        count++;
        System.out.println("  [Очередь] Добавлен " + order + " (размер: " + count + ")");
        notifyAll();
    }

    public synchronized Order takeOrder() throws InterruptedException {
        while (count == 0 && !shutdown) {
            wait();
        }
        if (count == 0 && shutdown) {
            return null;
        }

        Order order = queue[head];
        queue[head] = null;
        head = (head + 1) % queue.length;
        count--;
        System.out.println("  [Очередь] Взят " + order + " (размер: " + count + ")");
        notifyAll();
        return order;
    }

    public synchronized void shutdown() {
        shutdown = true;
        notifyAll();
    }

    public synchronized boolean isShutdown() {
        return shutdown && count == 0;
    }
}

class ReadyOrderQueue {
    private static class Node {
        Order order;
        Node next;
        Node(Order order) { this.order = order; }
    }

    private Node head = null;
    private Node tail = null;

    public synchronized void addReady(Order order) {
        Node node = new Node(order);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        notifyAll();
    }

    public synchronized Order takeReadyFor(String waiterName) throws InterruptedException {
        while (true) {
            Node prev = null;
            Node current = head;
            while (current != null) {
                if (current.order.getWaiterName().equals(waiterName)) {
                    if (prev == null) {
                        head = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    if (current == tail) {
                        tail = prev;
                    }
                    return current.order;
                }
                prev = current;
                current = current.next;
            }
            wait(100);
            return null;
        }
    }
}

class Waiter extends Thread {
    private final String waiterName;
    private final OrderQueue orderQueue;
    private final ReadyOrderQueue readyQueue;
    private final int ordersToTake;
    private final Random random;
    private int ordersTaken = 0;
    private int ordersDelivered = 0;

    private static final String[] DISH_NAMES = {"Борщ", "Пельмени", "Салат Цезарь", "Стейк", "Паста", "Суши", "Пицца"};

    public Waiter(String waiterName, OrderQueue orderQueue, ReadyOrderQueue readyQueue, int ordersToTake) {
        this.waiterName = waiterName;
        this.orderQueue = orderQueue;
        this.readyQueue = readyQueue;
        this.ordersToTake = ordersToTake;
        this.random = new Random();
    }

    @Override
    public void run() {
        System.out.println(waiterName + " начал работу");

        while (ordersTaken < ordersToTake || ordersDelivered < ordersTaken) {
            // Принимаем новый заказ
            if (ordersTaken < ordersToTake) {
                try {
                    Thread.sleep(random.nextInt(200) + 100); // Время на прием заказа
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                int numDishes = random.nextInt(3) + 1;
                Dish[] dishes = new Dish[numDishes];
                for (int i = 0; i < numDishes; i++) {
                    String dishName = DISH_NAMES[random.nextInt(DISH_NAMES.length)];
                    int cookingTime = random.nextInt(500) + 200;
                    dishes[i] = new Dish(dishName, cookingTime);
                }

                Order order = new Order(dishes, waiterName);
                System.out.println(waiterName + " принял " + order);

                try {
                    orderQueue.addOrder(order);
                    ordersTaken++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Проверяем готовые заказы
            try {
                Order ready = readyQueue.takeReadyFor(waiterName);
                if (ready != null) {
                    System.out.println(waiterName + " ДОСТАВИЛ клиенту: " + ready);
                    ordersDelivered++;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(waiterName + " завершил работу (принято: " + ordersTaken + ", доставлено: " + ordersDelivered + ")");
    }
}

class Chef extends Thread {
    private final String chefName;
    private final OrderQueue orderQueue;
    private final ReadyOrderQueue readyQueue;
    private final int maxSimultaneousDishes;
    private int currentDishes = 0;
    private final Object dishLock = new Object();

    public Chef(String chefName, OrderQueue orderQueue, ReadyOrderQueue readyQueue, int maxSimultaneousDishes) {
        this.chefName = chefName;
        this.orderQueue = orderQueue;
        this.readyQueue = readyQueue;
        this.maxSimultaneousDishes = maxSimultaneousDishes;
    }

    @Override
    public void run() {
        System.out.println(chefName + " начал работу (макс. блюд одновременно: " + maxSimultaneousDishes + ")");

        while (true) {
            try {
                Order order = orderQueue.takeOrder();
                if (order == null) {
                    break; // Очередь закрыта
                }

                System.out.println(chefName + " взял в работу: " + order);

                // Готовим каждое блюдо в заказе
                for (Dish dish : order.getDishes()) {
                    // Ожидаем, пока не освободится слот
                    synchronized (dishLock) {
                        while (currentDishes >= maxSimultaneousDishes) {
                            dishLock.wait();
                        }
                        currentDishes++;
                    }

                    System.out.println(chefName + " готовит: " + dish);
                    Thread.sleep(dish.getCookingTimeMs());

                    synchronized (dishLock) {
                        currentDishes--;
                        dishLock.notifyAll();
                    }
                }

                System.out.println(chefName + " ПРИГОТОВИЛ: " + order);
                readyQueue.addReady(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println(chefName + " завершил работу");
    }
}
