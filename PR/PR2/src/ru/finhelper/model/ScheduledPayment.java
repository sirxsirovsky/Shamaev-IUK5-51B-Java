package src.ru.finhelper.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduledPayment {
    private String description;
    private double amount;
    private LocalDate dueDate;
    private Recurrence recurrence;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public ScheduledPayment(String description, double amount, LocalDate dueDate, Recurrence recurrence) {
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.recurrence = recurrence;
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
    public Recurrence getRecurrence() { return recurrence; }

    public void executePayment() {
        if (this.recurrence == Recurrence.MONTHLY) {
            this.dueDate = this.dueDate.plusMonths(1);
        } else if (this.recurrence == Recurrence.YEARLY) {
            this.dueDate = this.dueDate.plusYears(1);
        }
    }

    public boolean isOneTime() {
        return this.recurrence == Recurrence.NONE;
    }

    @Override
    public String toString() {
        return String.format(
                "[ПЛАТЕЖ] До %s | Сумма: %.2f руб. | %s (%s)",
                dueDate.format(DATE_FORMATTER),
                amount,
                description,
                recurrence.getDisplayName()
        );
    }
}