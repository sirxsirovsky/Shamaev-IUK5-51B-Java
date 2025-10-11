package ru.bmstu.domain;

import ru.bmstu.exception.InvalidPhoneNumberException;

public class Person {
    private String firstName;
    private String secondName;
    private int age;
    private String phone;

    public Person(String firstName, String secondName, int age) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
    }

    public Person(String firstName, String secondName, int age, String phone) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) throws InvalidPhoneNumberException {
        if (phone == null || !phone.matches("^[0-9()+\\-]{7,15}$")) {
            throw new InvalidPhoneNumberException(
                    "Некорректный формат номера телефона. " +
                            "Допустимы цифры, (), +, -, длина от 7 до 15 символов."
            );
        }
        this.phone = phone;
    }
}
