package src.ru.finhelper.ui;

import src.ru.finhelper.model.*;
import src.ru.finhelper.service.TransactionManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleMenu {
    private final TransactionManager manager;
    private final Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ConsoleMenu(TransactionManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        manager.executeDuePayments();
        while (true) {
            printMenu();
            System.out.print("➡️ Ваш выбор: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": addIncome(); break;
                case "2": addExpense(); break;
                case "3": manager.viewAllTransactions(); break;
                case "4": manager.showBalance(); break;
                case "5": addScheduledPayment(); break;
                case "6": manager.viewUpcomingPayments(); break;
                case "7": manager.save(); break;
                case "0":
                    manager.save();
                    System.out.println("До встречи!");
                    return;
                default:
                    System.out.println("❗️ Неверный ввод, попробуйте снова.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n======= 🏦 Финансовый Помощник =======");
        System.out.println("--- Основные операции ---");
        System.out.println("1. Добавить доход");
        System.out.println("2. Добавить расход");
        System.out.println("3. Показать все транзакции");
        System.out.println("4. Показать баланс");
        System.out.println("--- Календарь платежей ---");
        System.out.println("5. Добавить плановый платеж");
        System.out.println("6. Показать ближайшие платежи");
        System.out.println("--- Управление ---");
        System.out.println("7. Сохранить в файл");
        System.out.println("0. Выход");
        System.out.println("======================================");
    }

    private void addIncome() {
        System.out.println("\n--- Добавление дохода ---");
        try {
            System.out.print("Введите сумму: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Введите описание (например, 'Зарплата'): ");
            String description = scanner.nextLine();
            LocalDate date = askDate("Введите дату (дд.мм.гггг) или оставьте пустым для сегодня:");
            manager.addTransaction(new Income(amount, description, date));
        } catch (NumberFormatException e) {
            System.out.println("❗️ Ошибка: сумма должна быть числом.");
        }
    }

    private void addExpense() {
        System.out.println("\n--- Добавление расхода ---");
        try {
            System.out.print("Введите сумму: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Введите описание (например, 'Продукты'): ");
            String description = scanner.nextLine();
            LocalDate date = askDate("Введите дату (дд.мм.гггг) или оставьте пустым для сегодня:");
            manager.addTransaction(new Expense(amount, description, date));
        } catch (NumberFormatException e) {
            System.out.println("❗️ Ошибка: сумма должна быть числом.");
        }
    }

    private void addScheduledPayment() {
        System.out.println("\n--- Добавление планового платежа ---");
        try {
            System.out.print("Введите описание (например, 'Аренда'): ");
            String description = scanner.nextLine();
            System.out.print("Введите сумму: ");
            double amount = Double.parseDouble(scanner.nextLine());
            LocalDate dueDate = askDate("Введите дату следующего платежа (дд.мм.гггг):");
            Recurrence recurrence = askRecurrence();
            manager.addScheduledPayment(new ScheduledPayment(description, amount, dueDate, recurrence));
        } catch (NumberFormatException e) {
            System.out.println("❗️ Ошибка: сумма должна быть числом.");
        }
    }

    private Recurrence askRecurrence() {
        System.out.println("Выберите регулярность:\n1. Одноразовый\n2. Ежемесячный\n3. Ежегодный");
        System.out.print("➡️ Ваш выбор: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "2": return Recurrence.MONTHLY;
            case "3": return Recurrence.YEARLY;
            default: return Recurrence.NONE;
        }
    }

    private LocalDate askDate(String prompt) {
        System.out.print(prompt + " ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty() && prompt.contains("сегодня")) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(input, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("❗️ Неверный формат даты. Попробуйте снова.");
            return askDate(prompt);
        }
    }
}