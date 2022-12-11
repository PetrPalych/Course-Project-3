package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import models.implementation.employees.Employee;
import models.implementation.patients.Patient;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;

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



    // Проверяет уникальность переданного логина в файле с учетными записями
    public static boolean usernameIsUnique(String username) {
        return !FileHelper.getFileData(Employee.class, FilePath.authorization).stream().map(
                employee -> employee.getUsername().equals(username)).toList().contains(true);
    }



    // Проверяет время приема (24:60) на корректность
    public static boolean timeIsValid(String time) {
        DateTimeFormatter strictTimeFormatter =
                DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalTime.parse(time, strictTimeFormatter);
        }

        catch (PatternSyntaxException | DateTimeParseException | NullPointerException e) {
            return false;
        }

        return true;
    }

    // Проверяет на корректность рабочий режим врача (24:60-24:60)
    public static boolean workingRegimeIsValid(String workingRegime) {
        try {
            String[] workingHours = workingRegime.split("-");
            return !Arrays.stream(workingHours).map(DataHelper::timeIsValid).toList().contains(false);
        }

        catch (PatternSyntaxException e) {
            return false;
        }
    }

    // Форматирует строку в рабочий режим (24:60-24:60)
    public static String formatWorkingRegime(String workingRegime) {
        return "%02d:%02d-%02d:%02d".formatted(
                Integer.parseInt(workingRegime.trim().split("-")[0].split(":")[0]), // hh:60-24:60
                Integer.parseInt(workingRegime.trim().split("-")[0].split(":")[1]), // 24:mm-24:60
                Integer.parseInt(workingRegime.trim().split("-")[1].split(":")[0]), // 24:60-hh:60
                Integer.parseInt(workingRegime.trim().split("-")[1].split(":")[1])); // 24:60-24:mm
    }



    // Для успешной генерации поле "ID" должно быть первой колонкой в файле
    public static int generateId(FilePath filePath) {
        List<Integer> idList = new ArrayList<>();

        try {
            CSVReader csvReader = new CSVReader(new FileReader(filePath.getFilePath()));
            csvReader.skip(1); // Пропускаем хэдер
            csvReader.readAll().forEach(strings -> idList.add(Integer.valueOf(strings[0])));
        }

        catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(idList);
        return idList.get(idList.size() - 1) + 1;
    }



    // Проверяет дату (dd.MM.yyyy) на корректность
    public static boolean dateIsValid(String date) {
        return date.matches("^[0-3][0-9].[0-3][0-9].(?:[0-9][0-9])?[0-9][0-9]$");
    }

    // Форматирует строку в дату (dd.MM.yyyy)
    public static String formatDate(String date) {
        return "%02d.%02d.%d".formatted(
                Integer.parseInt(date.trim().split("\\.")[0]), // День
                Integer.parseInt(date.trim().split("\\.")[1]), // Месяц
                Integer.parseInt(date.trim().split("\\.")[2])); // Год
    }



    // Проверяет телефонный номер (+x xxx xxx xxx) на корректность
    public static boolean phoneNumberIsValid(String phoneNumber) {
        return phoneNumber.matches("^(\\+(\\d{1,10})|(\\d{1,10})) ?\\d{3} ?\\d{3} ?\\d{3}$");
    }



    // Проверяет уникальность переданного персонального номера в файле с пациентами
    public static boolean personalNumberIsUnique(String personalNumber) {
        return !FileHelper.getFileData(Patient.class, FilePath.patients).stream().map(
                patient -> patient.getPersonalNumber().equals(personalNumber)).toList().contains(true);
    }
}
