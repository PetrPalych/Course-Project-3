package utils;

import models.implementation.employees.Employee;

public class ConsolePrinter {
    // Выводит информацию о сотруднике
    public static void printEmployee(Employee employee) {
        System.out.printf("""
                Сотрудник: %s
                Логин: %s
                Пароль: %s
                Тип учетной записи: %s%n
                """,
                employee.getDoctorID(), employee.getUsername(), employee.getPassword(), employee.getAccountType());
    }
}