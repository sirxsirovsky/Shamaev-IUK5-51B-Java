package task3;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainTask3 {
    public static void main(String[] args) throws InterruptedException {
        RestaurantService restaurant = new RestaurantService();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Order order1 = new Order("Пицца Пепперони", 2000, 600.0);
        Order order2 = new Order("Салат Цезарь", 500, 350.0);
        Order order3 = new Order("Стейк Рибай", 3000, 1500.0);

        System.out.println("--- Размещение заказов ---");

        CompletableFuture<Void> future1 = CompletableFuture.supplyAsync(() -> restaurant.checkIngredients(order1), executor)
                .thenApplyAsync(restaurant::cook, executor)
                .thenApplyAsync(restaurant::calculateCost, executor)
                .thenAcceptAsync(restaurant::notifyReady, executor)
                .exceptionally(ex -> {
                    System.err.println("❌ Ошибка обработки заказа '" + order1.getDishName() + "': " + ex.getMessage());
                    return null;
                });

        CompletableFuture<Void> future2 = CompletableFuture.supplyAsync(() -> restaurant.checkIngredients(order2), executor)
                .thenApplyAsync(restaurant::cook, executor)
                .thenApplyAsync(restaurant::calculateCost, executor)
                .thenAcceptAsync(restaurant::notifyReady, executor)
                .exceptionally(ex -> {
                    System.err.println("❌ Ошибка обработки заказа '" + order2.getDishName() + "': " + ex.getMessage());
                    return null;
                });

        CompletableFuture<Void> future3 = CompletableFuture.supplyAsync(() -> restaurant.checkIngredients(order3), executor)
                .thenApplyAsync(restaurant::cook, executor)
                .thenApplyAsync(restaurant::calculateCost, executor)
                .thenAcceptAsync(restaurant::notifyReady, executor)
                .exceptionally(ex -> {
                    System.err.println("❌ Ошибка обработки заказа '" + order3.getDishName() + "': " + ex.getMessage());
                    return null;
                });

        System.out.println("--- Заказы отправлены на кухню. Ожидаем завершения... ---");

        CompletableFuture.allOf(future1, future2, future3).join();

        executor.shutdown();
    }
}