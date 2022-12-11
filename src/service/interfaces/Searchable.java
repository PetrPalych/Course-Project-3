package service.interfaces;

import models.implementation.doctors.Doctor;
import models.interfaces.Convertible;
import service.implementation.accounts.Account;
import utils.FileHelper;
import utils.FilePath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface Searchable {
    default void findDoctor(Account account) {
        boolean isFound = false;
        boolean incorrectInput = false;

        System.out.println("""
                Выберите по какому критерию искать врача:
                1) ID
                2) ФИО
                3) Специализация
                4) Рабочие дни""");

        List<? extends Convertible> relevantDoctors = null;
        switch (account.getScanner().nextLine()) {
            case "1" -> {
                System.out.println("Введите ID врача: ");
                String id = account.getScanner().nextLine();
                relevantDoctors = account.findInList(
                        FileHelper.getFileData(Doctor.class, FilePath.doctors), id, "getId");
            }

            case "2" -> {
                System.out.println("Введите ФИО врача: ");
                String fullName = account.getScanner().nextLine();
                relevantDoctors = FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                        doctor -> doctor.getFullName().contains(fullName)).toList();
            }

            case "3" -> {
                System.out.println("Введите специализацию врача: ");
                String specialization = account.getScanner().nextLine();
                relevantDoctors = account.findInList(
                        FileHelper.getFileData(Doctor.class, FilePath.doctors), specialization, "getSpecialization");
            }

            case "4" -> {
                System.out.println("""
                        Выберите рабочие дни (через пробел):
                        1) Понедельник
                        2) Вторник
                        3) Среда
                        4) Четверг
                        5) Пятница
                        6) Суббота
                        7) Воскресенье""");

                HashSet<String> choiceSet = new HashSet<>(List.of(account.getScanner().nextLine().split(" ")));

                ArrayList<String> days = new ArrayList<>();
                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> days.add("Mo");
                        case "2" -> days.add("Tu");
                        case "3" -> days.add("We");
                        case "4" -> days.add("Th");
                        case "5" -> days.add("Fr");
                        case "6" -> days.add("Sa");
                        case "7" -> days.add("Su");
                    }
                }

                relevantDoctors = FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                        doctor -> days.stream().anyMatch(day -> doctor.getWorkingDays().contains(day))).toList();
            }
        }

        if (relevantDoctors != null && !relevantDoctors.isEmpty()) {
            isFound = true;
            System.out.println(account.makeTableOf(relevantDoctors, FilePath.doctors));
        }

        account.checkConditions(isFound, incorrectInput);
        account.backToMenu();
    }
}
