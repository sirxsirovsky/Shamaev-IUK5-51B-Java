package src.ru.finhelper.service;

import src.ru.finhelper.model.ScheduledPayment;
import src.ru.finhelper.model.Transaction;
import java.util.List;

public interface DataStorage {
    void save(List<Transaction> transactions);
    List<Transaction> load();
    void saveScheduled(List<ScheduledPayment> payments);
    List<ScheduledPayment> loadScheduled();
}