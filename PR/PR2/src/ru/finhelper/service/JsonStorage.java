package src.ru.finhelper.service;

import org.json.*;
import src.ru.finhelper.model.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage implements DataStorage {
    private final String transactionsFilePath;
    private final String scheduledFilePath;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public JsonStorage(String transactionsFilePath, String scheduledFilePath) {
        this.transactionsFilePath = transactionsFilePath;
        this.scheduledFilePath = scheduledFilePath;
    }

    @Override
    public void save(List<Transaction> transactions) {
        JSONArray jsonArray = new JSONArray();
        for (Transaction t : transactions) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", t.getType());
            jsonObj.put("amount", t.getAmount());
            jsonObj.put("date", t.getDate().format(DATE_FORMATTER));
            jsonObj.put("description", t.getDescription());
            jsonArray.put(jsonObj);
        }
        try (FileWriter writer = new FileWriter(transactionsFilePath)) {
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            System.err.println("Ошибка сохранения транзакций в JSON: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(transactionsFilePath)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String type = obj.getString("type");
                double amount = obj.getDouble("amount");
                LocalDate date = LocalDate.parse(obj.getString("date"), DATE_FORMATTER);
                String description = obj.getString("description");
                if ("INCOME".equals(type)) {
                    transactions.add(new Income(amount, description, date));
                } else if ("EXPENSE".equals(type)) {
                    transactions.add(new Expense(amount, description, date));
                }
            }
        } catch (IOException | JSONException e) { /* Файл может не существовать */ }
        return transactions;
    }

    @Override
    public void saveScheduled(List<ScheduledPayment> payments) {
        JSONArray jsonArray = new JSONArray();
        for (ScheduledPayment p : payments) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("description", p.getDescription());
            jsonObj.put("amount", p.getAmount());
            jsonObj.put("dueDate", p.getDueDate().format(DATE_FORMATTER));
            jsonObj.put("recurrence", p.getRecurrence().name());
            jsonArray.put(jsonObj);
        }
        try (FileWriter writer = new FileWriter(scheduledFilePath)) {
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            System.err.println("Ошибка сохранения плановых платежей в JSON: " + e.getMessage());
        }
    }

    @Override
    public List<ScheduledPayment> loadScheduled() {
        List<ScheduledPayment> payments = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(scheduledFilePath)));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String description = obj.getString("description");
                double amount = obj.getDouble("amount");
                LocalDate dueDate = LocalDate.parse(obj.getString("dueDate"), DATE_FORMATTER);
                Recurrence recurrence = Recurrence.valueOf(obj.getString("recurrence"));
                payments.add(new ScheduledPayment(description, amount, dueDate, recurrence));
            }
        } catch (IOException | JSONException e) { /* Файл может не существовать */ }
        return payments;
    }
}