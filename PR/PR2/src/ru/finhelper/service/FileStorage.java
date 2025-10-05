package src.ru.finhelper.service;

import src.ru.finhelper.model.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileStorage implements DataStorage {
    private final File storageFile;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private List<Transaction> transactionsToSave;
    private List<ScheduledPayment> paymentsToSave;

    public FileStorage(String filePath) {
        this.storageFile = new File(filePath);
    }

    @Override
    public void save(List<Transaction> transactions) {
        this.transactionsToSave = transactions;
        if (paymentsToSave != null) {
            saveAllData();
        }
    }

    @Override
    public void saveScheduled(List<ScheduledPayment> payments) {
        this.paymentsToSave = payments;
        if (transactionsToSave != null) {
            saveAllData();
        }
    }

    private void saveAllData() {
        try (FileWriter writer = new FileWriter(storageFile)) {
            if (transactionsToSave != null) {
                for (Transaction t : transactionsToSave) {
                    String line = String.join(";", "T", t.getType(), String.valueOf(t.getAmount()),
                            t.getDate().format(DATE_FORMATTER), t.getDescription());
                    writer.write(line + System.lineSeparator());
                }
            }
            if (paymentsToSave != null) {
                for (ScheduledPayment p : paymentsToSave) {
                    String line = String.join(";", "S", String.valueOf(p.getAmount()),
                            p.getDueDate().format(DATE_FORMATTER), p.getRecurrence().name(), p.getDescription());
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении в текстовый файл: " + e.getMessage());
        } finally {
            transactionsToSave = null;
            paymentsToSave = null;
        }
    }

    @Override
    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        if (!storageFile.exists()) return transactions;
        try {
            List<String> lines = Files.readAllLines(storageFile.toPath());
            for (String line : lines) {
                String[] parts = line.split(";", 5);
                if (parts.length < 5 || !parts[0].equals("T")) continue;
                String type = parts[1];
                double amount = Double.parseDouble(parts[2]);
                LocalDate date = LocalDate.parse(parts[3], DATE_FORMATTER);
                String description = parts[4];
                if ("INCOME".equals(type)) {
                    transactions.add(new Income(amount, description, date));
                } else if ("EXPENSE".equals(type)) {
                    transactions.add(new Expense(amount, description, date));
                }
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Ошибка при загрузке транзакций из файла: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public List<ScheduledPayment> loadScheduled() {
        List<ScheduledPayment> payments = new ArrayList<>();
        if (!storageFile.exists()) return payments;
        try {
            List<String> lines = Files.readAllLines(storageFile.toPath());
            for (String line : lines) {
                String[] parts = line.split(";", 5);
                if (parts.length < 5 || !parts[0].equals("S")) continue;
                double amount = Double.parseDouble(parts[1]);
                LocalDate dueDate = LocalDate.parse(parts[2], DATE_FORMATTER);
                Recurrence recurrence = Recurrence.valueOf(parts[3]);
                String description = parts[4];
                payments.add(new ScheduledPayment(description, amount, dueDate, recurrence));
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Ошибка при загрузке плановых платежей из файла: " + e.getMessage());
        }
        return payments;
    }
}