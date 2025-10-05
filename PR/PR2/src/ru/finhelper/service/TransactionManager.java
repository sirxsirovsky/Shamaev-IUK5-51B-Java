package src.ru.finhelper.service;

import src.ru.finhelper.model.Expense;
import src.ru.finhelper.model.ScheduledPayment;
import src.ru.finhelper.model.Transaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {
    private List<Transaction> transactions;
    private List<ScheduledPayment> scheduledPayments;
    private final DataStorage storage;

    public TransactionManager(DataStorage storage) {
        this.storage = storage;
        this.transactions = storage.load();
        this.scheduledPayments = storage.loadScheduled();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        System.out.println("✅ Запись успешно добавлена!");
    }

    public void viewAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("📭 Список транзакций пока пуст.");
            return;
        }
        transactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .forEach(System.out::println);
        //.forEach(element -> System.out.println(element));
    }

    public void showBalance() {
        double totalIncome = transactions.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(Transaction::getAmount).sum();
        double totalExpense = transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(Transaction::getAmount).sum();

        System.out.println("\n--- 📊 Текущий баланс ---");
        System.out.printf("⬆️ Всего доходов: %.2f руб.\n", totalIncome);
        System.out.printf("⬇️ Всего расходов: %.2f руб.\n", totalExpense);
        System.out.printf("💰 Итоговый баланс: %.2f руб.\n", (totalIncome - totalExpense));
        System.out.println("------------------------");
    }

    public void addScheduledPayment(ScheduledPayment payment) {
        scheduledPayments.add(payment);
        System.out.println("✅ Запланированный платеж добавлен!");
    }

    public void viewUpcomingPayments() {
        System.out.println("\n--- 🗓️ Календарь платежей (на 30 дней) ---");
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        List<ScheduledPayment> upcoming = scheduledPayments.stream()
                .filter(p -> !p.getDueDate().isAfter(thirtyDaysFromNow))
                .sorted((p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate()))
                .collect(Collectors.toList());

        if (upcoming.isEmpty()) {
            System.out.println("📭 Нет запланированных платежей на ближайший месяц.");
        } else {
            upcoming.forEach(System.out::println);
        }
        System.out.println("------------------------------------------");
    }

    public void executeDuePayments() {
        List<ScheduledPayment> paymentsToExecute = scheduledPayments.stream()
                .filter(p -> !p.getDueDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        if (paymentsToExecute.isEmpty()) {
            return;
        }
        System.out.println("\n❗️ Обнаружены плановые платежи. Выполняется " + paymentsToExecute.size() + "...");

        List<ScheduledPayment> toRemove = new ArrayList<>();
        for (ScheduledPayment payment : paymentsToExecute) {
            transactions.add(new Expense(payment.getAmount(), payment.getDescription() + " (плановый)", LocalDate.now()));
            System.out.println("  -> Исполнен платеж: " + payment.getDescription());
            if (payment.isOneTime()) {
                toRemove.add(payment);
            } else {
                payment.executePayment();
            }
        }
        scheduledPayments.removeAll(toRemove);
        save();
    }

    public void save() {
        storage.save(transactions);
        storage.saveScheduled(scheduledPayments);
        System.out.println("💾 Данные сохранены.");
    }
}