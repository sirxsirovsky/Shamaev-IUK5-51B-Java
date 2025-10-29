import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lab4Solutions {

    public static void main(String[] args) {

        List<Student> students = Arrays.asList(
                new Student("Анна", 21, Map.of("Математика", 85, "Физика", 78)),
                new Student("Борис", 19, Map.of("История", 90, "Литература", 82)),
                new Student("Виктор", 22, Map.of("Химия", 75, "Биология", 79)),
                new Student("Дарья", 21, Map.of("Информатика", 95, "Математика", 88)),
                new Student("Егор", 23, Map.of("Физика", 81, "Химия", 70))
        );

        List<Employee> employees = Arrays.asList(
                new Employee("Иван", 30, "IT", 60000),
                new Employee("Мария", 25, "HR", 45000),
                new Employee("Петр", 28, "IT", 75000),
                new Employee("Ольга", 22, "IT", 52000),
                new Employee("Сергей", 35, "Finance", 80000),
                new Employee("Елена", 24, "IT", 55000)
        );

        List<Order> orders = Arrays.asList(
                new Order(1, "client1", LocalDate.now().minusWeeks(1), Arrays.asList(
                        new Item("clothing", 50.0), new Item("electronics", 200.0)
                )),
                new Order(2, "client2", LocalDate.now().minusMonths(2), Arrays.asList(
                        new Item("clothing", 120.0)
                )),
                new Order(3, "client1", LocalDate.now().minusDays(10), Arrays.asList(
                        new Item("books", 30.0), new Item("clothing", 70.0)
                )),
                new Order(4, "client3", LocalDate.now().minusWeeks(2), Arrays.asList(
                        new Item("food", 15.0)
                ))
        );

        List<Product> products = Arrays.asList(
                new Product("Ноутбук", 1200.0, 5),
                new Product("Мышь", 45.0, 10),
                new Product("Клавиатура", 70.0, 0),
                new Product("Монитор", 300.0, 3),
                new Product("Телефон", 800.0, 8)
        );

        List<Customer> customers = Arrays.asList(
                new Customer("alice@email.com", Arrays.asList(
                        new Purchase("electronics", 500.0, LocalDate.now().minusDays(5)),
                        new Purchase("books", 80.0, LocalDate.now().minusWeeks(3)),
                        new Purchase("electronics", 600.0, LocalDate.now().minusDays(20))
                )),
                new Customer("bob@email.com", Arrays.asList(
                        new Purchase("clothing", 150.0, LocalDate.now().minusDays(10)),
                        new Purchase("electronics", 100.0, LocalDate.now().minusDays(3))
                )),
                new Customer("charlie@email.com", Arrays.asList(
                        new Purchase("food", 50.0, LocalDate.now().minusMonths(2)),
                        new Purchase("electronics", 900.0, LocalDate.now().minusDays(40))
                ))
        );

        System.out.println("--- Задание 1: Фильтрация студентов ---");
        List<String> studentNames = solveTask1(students);
        System.out.println("Студенты, соответствующие критериям: " + studentNames);
        System.out.println("----------------------------------------\n");

        System.out.println("--- Задание 2: Топ-3 молодых IT-сотрудников ---");
        List<Employee> topEmployees = solveTask2(employees);
        System.out.println("Топ-3 сотрудника:");
        topEmployees.forEach(e -> System.out.println(e.getName() + ", возраст: " + e.getAge()));
        System.out.println("----------------------------------------\n");

        System.out.println("--- Задание 3: Анализ заказов ---");
        solveTask3(orders);
        System.out.println("----------------------------------------\n");

        System.out.println("--- Задание 4: Фильтрация товаров ---");
        List<String> productNames = solveTask4(products);
        System.out.println("Товары в наличии (цена > 50), отсортированные по кол-ву: " + productNames);
        System.out.println("----------------------------------------\n");

        System.out.println("--- Задание 5: Фильтрация клиентов ---");
        List<String> customerEmails = solveTask5(customers);
        System.out.println("Email клиентов (сумма за месяц > 1000), отсорт. по кол-ву покупок:");
        System.out.println(customerEmails);

        System.out.println("\nПроверка изменения цены у клиента Alice (покупка 1):");
        System.out.println("Новая цена: " + customers.get(0).getPurchases().get(0).getAmount());
        System.out.println("----------------------------------------\n");
    }

    public static List<String> solveTask1(List<Student> students) {
        return students.stream()
                .filter(student -> student.getAge() > 20)
                .filter(student -> student.getGrades().values().stream().anyMatch(grade -> grade > 80))
                .sorted(Comparator.comparing(Student::getName))
                .map(Student::getName)
                .collect(Collectors.toList());
    }

    public static List<Employee> solveTask2(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getSalary() > 50000)
                .filter(e -> "IT".equals(e.getDepartment()))
                .sorted(Comparator.comparingInt(Employee::getAge))
                .limit(3)
                .collect(Collectors.toList());
    }

    public static void solveTask3(List<Order> orders) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        List<Order> filteredOrders = orders.stream()
                .filter(o -> o.getOrderDate().isAfter(oneMonthAgo))
                .filter(o -> o.getItems().stream().anyMatch(i -> "clothing".equals(i.getCategory())))
                .collect(Collectors.toList());

        double totalCost = filteredOrders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToDouble(Item::getPrice)
                .sum();

        System.out.println("Общая стоимость отфильтрованных заказов ('clothing', <1 мес): " + totalCost);

        Map<String, List<Order>> groupedByClient = filteredOrders.stream()
                .collect(Collectors.groupingBy(Order::getCustomerId));

        System.out.println("Отфильтрованные заказы, сгруппированные по клиентам:");
        groupedByClient.forEach((client, orderList) -> {
            System.out.println("  Клиент: " + client);
            orderList.forEach(order -> System.out.println("\t" + order));
        });
    }

    public static List<String> solveTask4(List<Product> products) {
        return products.stream()
                .filter(p -> p.getPrice() > 50)
                .filter(p -> p.getQuantity() > 0)
                .sorted(Comparator.comparingInt(Product::getQuantity))
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public static List<String> solveTask5(List<Customer> customers) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);

        return customers.stream()
                .filter(c -> c.getPurchases().stream()
                        .filter(p -> p.getDate().isAfter(oneMonthAgo))
                        .mapToDouble(Purchase::getAmount)
                        .sum() > 1000)
                .peek(c -> c.getPurchases().stream()
                        .forEach(p -> {
                            if ("electronics".equals(p.getCategory()) && p.getDate().isAfter(twoWeeksAgo)) {
                                p.setAmount(p.getAmount() * 1.10);
                            }
                        }))
                .sorted(Comparator.comparingInt((Customer c) -> c.getPurchases().size()).reversed())
                .map(Customer::getEmail)
                .collect(Collectors.toList());
    }
}

class Student {
    String name;
    int age;
    Map<String, Integer> grades;

    public Student(String name, int age, Map<String, Integer> grades) {
        this.name = name;
        this.age = age;
        this.grades = grades;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<String, Integer> getGrades() { return grades; }

    @Override
    public String toString() {
        return "Student{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
}

class Employee {
    String name;
    int age;
    String department;
    double salary;

    public Employee(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return "Employee{" + "name='" + name + '\'' + ", age=" + age + ", dep='" + department + '\'' + '}';
    }
}

class Order {
    int orderId;
    String customerId;
    List<Item> items;
    LocalDate orderDate;

    public Order(int orderId, String customerId, LocalDate orderDate, List<Item> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
    }
    public int getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<Item> getItems() { return items; }
    public LocalDate getOrderDate() { return orderDate; }

    @Override
    public String toString() {
        return "Order{" + "id=" + orderId + ", items=" + items.size() + ", date=" + orderDate + '}';
    }
}

class Item {
    String category;
    double price;

    public Item(String category, double price) {
        this.category = category;
        this.price = price;
    }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
}

class Product {
    String name;
    double price;
    int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}

class Customer {
    String email;
    List<Purchase> purchases;

    public Customer(String email, List<Purchase> purchases) {
        this.email = email;
        this.purchases = purchases;
    }
    public String getEmail() { return email; }
    public List<Purchase> getPurchases() { return purchases; }
}

class Purchase {
    String category;
    double amount;
    LocalDate date;

    public Purchase(String category, double amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
}