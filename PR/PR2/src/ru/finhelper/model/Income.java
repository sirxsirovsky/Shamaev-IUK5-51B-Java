package src.ru.finhelper.model;

import java.time.LocalDate;

public class Income extends Transaction {
    public Income(double amount, String description, LocalDate date) {
        super(amount, description, date);
    }

    @Override
    public String getType() {
        return "INCOME";
    }

    @Override
    public String toString() {
        return "[ДОХОД] " + super.toString();
    }
}