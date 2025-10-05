package src.ru.finhelper.model;

import java.time.LocalDate;

public class Expense extends Transaction {
    public Expense(double amount, String description, LocalDate date) {
        super(amount, description, date);
    }

    @Override
    public String getType() {
        return "EXPENSE";
    }

    @Override
    public String toString() {
        return "[РАСХОД] " + super.toString();
    }
}