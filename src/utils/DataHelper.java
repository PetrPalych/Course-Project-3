package utils;

import models.implementation.employees.Employee;

public class DataHelper {
    // Проверяет логин и пароль на корректность
    public static String[] checkSignInData(String username, String password) {
        for (Employee employee : FileHelper.getFileData(Employee.class, FilePath.authorization)) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return new String[] {employee.getAccountType(), employee.getDoctorID()};
            }
        }

        return new String[]{"Error", null};
    }
}
