package src.ru.finhelper.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Transaction {
    protected double amount;
    protected String description;
    protected LocalDate date;
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Transaction(double amount, String description, LocalDate date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    public abstract String getType();

    @Override
    public String toString() {
        return "Дата: " + date.format(DATE_FORMATTER) + ", Сумма: " + amount + " руб., Описание: " + description;
    }
}