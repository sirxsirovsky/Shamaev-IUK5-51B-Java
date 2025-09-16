import java.util.ArrayList;

public class LR1 {

    public static void main(String[] args) {
        System.out.println("--- Лабораторная работа №1 ---");

        System.out.println("\n--- 1.1. Операторы if/else if/else ---");
        int a = 10;
        int b = 20;
        int c = 30;
        int d = 40;

        if (a < b && b < c) {
            System.out.println("Условие 1 (&&): ИСТИНА");
        } else {
            System.out.println("Условие 1 (&&): ЛОЖЬ");
        }

        if (a > b || b > c || c < d) {
            System.out.println("Условие 2 (||): ИСТИНА");
        } else {
            System.out.println("Условие 2 (||): ЛОЖЬ");
        }

        if ((a < b && b < c) && (c < d || a > d)) {
            System.out.println("Условие 3 (смешанное): ИСТИНА");
        } else if (a == 10) {
            System.out.println("Условие 3 (смешанное): попали в else if");
        } else {
            System.out.println("Условие 3 (смешанное): ЛОЖЬ");
        }

        System.out.println("\n--- 1.2. Конструкция switch/case ---");
        int dayOfWeek = 3;
        String dayName;

        switch (dayOfWeek) {
            case 1:
                dayName = "Понедельник";
                break;
            case 2:
                dayName = "Вторник";
                break;
            case 3:
                dayName = "Среда";
                break;
            case 4:
                dayName = "Четверг";
                break;
            case 5:
                dayName = "Пятница";
                break;
            default:
                dayName = "Выходной";
                break;
        }
        System.out.println("Сегодня: " + dayName);

        System.out.println("\n--- 1.3. Тернарный оператор ---");
        int x = 10;
        int y = 20;
        int max = (x > y) ? x : y;
        System.out.println("Максимальное значение: " + max);

        System.out.println("\n--- 2.1. Циклы for, while, do...while ---");
        System.out.print("Цикл for: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.print("Цикл while: ");
        int j = 0;
        while (j < 5) {
            System.out.print(j + " ");
            j++;
        }
        System.out.println();

        System.out.print("Цикл do...while: ");
        int k = 0;
        do {
            System.out.print(k + " ");
            k++;
        } while (k < 5);
        System.out.println();

        System.out.println("\n--- 2.2. Операторы continue и break ---");
        System.out.print("Цикл с continue и break: ");
        for (int i = 0; i < 10; i++) {
            if (i % 2 != 0) {
                continue;
            }
            if (i > 6) {
                break;
            }
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("\n--- 2.3. Многомерный массив ---");
        int[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };

        System.out.println("Перебор элементов многомерного массива:");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col] + "\t");
            }
            System.out.println();
        }

        System.out.println("\n--- 3. Boxing (Упаковка/Распаковка) ---");

        ArrayList<Integer> numberList = new ArrayList<>();
        int primitiveI = 100;
        numberList.add(primitiveI); // Autoboxing: int 100 -> Integer 100
        numberList.add(200); // Тоже Autoboxing

        System.out.println("Список объектов Integer: " + numberList);


        int primitiveInt = 100;
        Integer wrapperInteger = primitiveInt;

        boolean primitiveBool = true;
        Boolean wrapperBoolean = primitiveBool;

        long primitiveLong = 1000L;
        Long wrapperLong = primitiveLong;

        System.out.println("Автоупаковка: " + primitiveInt + " -> " + wrapperInteger);

        Integer anotherWrapper = Integer.valueOf(200);
        int anotherPrimitive = anotherWrapper;
        System.out.println("Распаковка: " + anotherWrapper + " -> " + anotherPrimitive);

        System.out.println("\nСимуляция NullPointerException:");
        Integer nullInteger = null;
        try {
            int errorInt = nullInteger;
        } catch (NullPointerException e) {
            System.out.println("Успешно поймали ошибку: " + e);
        }

        System.out.println("\n--- 4.1. Операции со строками ---");
        String originalString = "  Java - это весело!  ";

        String replacedString = originalString.replace("весело", "мощно");
        System.out.println("Замена: '" + replacedString + "'");

        String trimmedString = originalString.trim();
        System.out.println("Обрезка: '" + trimmedString + "'");

        String[] words = trimmedString.split(" ");
        System.out.print("Разбиение: ");
        for (String word : words) {
            System.out.print("'" + word + "' ");
        }
        System.out.println();

        System.out.println("\n--- 4.2. Сравнение строк ---");
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        String s4 = "java";

        System.out.println("s1 == s2: " + (s1 == s2));
        System.out.println("s1 == s3: " + (s1 == s3));

        System.out.println("s1.equals(s3): " + s1.equals(s3));
        System.out.println("s1.equals(s4): " + s1.equals(s4));

        System.out.println("s1.equalsIgnoreCase(s4): " + s1.equalsIgnoreCase(s4));

        System.out.println("\n--- 4.3. StringBuilder ---");
        StringBuilder sb = new StringBuilder("Привет");

        sb.append(", ");
        sb.append("Мир!");
        System.out.println("Конкатенация: " + sb.toString());

        sb.delete(6, 12);
        System.out.println("Обрезка: " + sb.toString());
    }
}