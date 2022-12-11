package service.interfaces;

import models.implementation.appointments.Appointment;
import models.implementation.doctors.Doctor;
import models.implementation.patients.Patient;
import models.implementation.recipes.Recipe;
import models.implementation.treatments.Treatment;
import models.interfaces.Convertible;
import service.implementation.accounts.Account;
import utils.ConsolePrinter;
import utils.FileHelper;
import utils.FilePath;
import utils.PrettyTable;

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

    default void findPatientFile(Account account) {
        boolean isFound = false;
        boolean incorrectInput = false;

        System.out.println("""
                Выберите по какому критерию искать:
                1) ID
                2) ФИО
                3) Персональный номер""");

        List<? extends Convertible> relevantPatients = null;
        switch (account.getScanner().nextLine()) {
            case "1" -> {
                System.out.println("Введите ID амбулаторной карты: ");
                String id = account.getScanner().nextLine();
                relevantPatients = account.findInList(
                        FileHelper.getFileData(Patient.class, FilePath.patients), id, "getId");
            }

            case "2" -> {
                System.out.println("Введите ФИО пациента: ");
                String fullName = account.getScanner().nextLine();
                relevantPatients = FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                        patient -> patient.getFullName().contains(fullName)).toList();
            }

            case "3" -> {
                System.out.println("Введите персональный номер пациента: ");
                String personalNumber = account.getScanner().nextLine();
                relevantPatients = account.findInList(
                        FileHelper.getFileData(Patient.class, FilePath.patients),
                        personalNumber,
                        "getPersonalNumber");
            }
        }

        if (relevantPatients != null && !relevantPatients.isEmpty()) {
            isFound = true;
            System.out.println(account.makeTableOf(relevantPatients, FilePath.patients));
        }

        account.checkConditions(isFound, incorrectInput);
        account.backToMenu();
    }

    default void findAppointment(Account account) {
        boolean isFound = false;

        System.out.println("""
                Выберите ко какому критерию искать прием(-ы):
                1) ID приема
                2) ID врача
                3) ФИО врача
                4) Время приема
                5) Дата приема
                6) Время и дата приема
                7) ФИО пациента
                8) Статус оплаты""");

        List<? extends Convertible> relevantContracts = null;
        switch (account.getScanner().nextLine()) {
            case "1" -> {
                System.out.println("Введите ID приема: ");
                String contractId = account.getScanner().nextLine();
                relevantContracts = account.findInList(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments),
                        contractId,
                        "getContractId");
            }

            case "2" -> {
                System.out.println("Введите ID врача: ");
                String doctorId = account.getScanner().nextLine();
                relevantContracts = account.findInList(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments),
                        doctorId,
                        "getDoctorId");
            }

            case "3" -> {
                System.out.println("Введите ФИО врача: ");
                String doctorFullName = account.getScanner().nextLine();
                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getDoctorFullName().contains(doctorFullName)).toList();
            }

            case "4" -> {
                System.out.println("Введите время приема: (24:60)");
                String receptionTime = account.getScanner().nextLine();
                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getReceptionTime().contains(receptionTime)).toList();
            }

            case "5" -> {
                System.out.println("Введите дату приема: (dd.MM.yyyy)");
                String receptionDate = account.getScanner().nextLine();
                relevantContracts = account.findInList(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments),
                        receptionDate,
                        "getReceptionDate");
            }

            case "6" -> {
                System.out.println("Введите время приема: (24:60)");
                String receptionTime = account.getScanner().nextLine();

                System.out.println("Введите дату приема: (dd.MM.yyyy)");
                String receptionDate = account.getScanner().nextLine();

                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getReceptionTime().contains(receptionTime) &&
                                contract.getReceptionDate().equals(receptionDate)).toList();
            }

            case "7" -> {
                System.out.println("Введите ФИО пациента:");
                String patientFullName = account.getScanner().nextLine();
                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getPatientFullName().contains(patientFullName)).toList();
            }

            case "8" -> {
                System.out.println("""
                        Выберите тип статуса оплаты:
                        1) Оплачено
                        2) Не оплачено""");

                switch (account.getScanner().nextLine()) {
                    case "1" -> relevantContracts = account.findInList(
                            FileHelper.getFileData(Appointment.class, FilePath.appointments),
                            "Paid",
                            "getStatus");

                    case "2" -> relevantContracts = account.findInList(
                            FileHelper.getFileData(Appointment.class, FilePath.appointments),
                            "Not Paid",
                            "getStatus");
                }
            }
        }

        if (relevantContracts != null && !relevantContracts.isEmpty()) {
            isFound = true;
            System.out.println(account.makeTableOf(relevantContracts, FilePath.appointments));
        }

        account.checkConditions(isFound, false);
        account.backToMenu();
    }

    default void findPrescribedTreatment(Account account) {
        boolean isFound = false;
        boolean incorrectInput = false;

        System.out.println("""
                Выберите по какому критерию искать назначенное лечение:
                1) ID лечения
                2) Дата назначения
                3) ID врача
                4) ФИО врача
                5) ID амбулаторной карты пациента
                6) ФИО пациента""");

        List<Treatment> relevantTreatments = null;
        switch (account.getScanner().nextLine()) {
            case "1" -> {
                System.out.println("Введите ID лечения: ");
                String treatmentID = account.getScanner().nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getTreatmentID().equals(treatmentID)).toList();
            }

            case "2" -> {
                System.out.println("Введите дату назначения лечения: ");
                String date = account.getScanner().nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getDate().equals(date)).toList();
            }

            case "3" -> {
                System.out.println("Введите ID врача: ");
                String doctorID = account.getScanner().nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getDoctorID().equals(doctorID)).toList();
            }

            case "4" -> {
                System.out.println("Введите ФИО врача: ");
                String doctorFullName = account.getScanner().nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getDoctorFullName().contains(doctorFullName)).toList();
            }

            case "5" -> {
                System.out.println("Введите ID амбулаторной карты пациента: ");
                String cardFileID = account.getScanner().nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getCardFileID().equals(cardFileID)).toList();
            }

            case "6" -> {
                System.out.println("Введите ФИО пациента: ");
                String patientFullName = account.getScanner().nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getPatientFullName().contains(patientFullName)).toList();
            }
        }

        if (relevantTreatments != null && !relevantTreatments.isEmpty()) {
            PrettyTable prettyTable = new PrettyTable();
            prettyTable.addHeader("Treatment ID", "Date", "Doctor ID", "Doctor full name", "Card file ID", "Patient full name");
            relevantTreatments.forEach(treatment -> prettyTable.addRow(treatment.toShortArray()));

            System.out.println(prettyTable + "\nДля вывода полной информации о назначенном лечении, введите его ID: ");
            String treatmentID = account.getScanner().nextLine();

            for (Treatment treatment : relevantTreatments) {
                if (treatment.getTreatmentID().equals(treatmentID)) {
                    isFound = true;
                    ConsolePrinter.printTreatment(treatment);
                }
            }
        }

        account.checkConditions(isFound, incorrectInput);
        account.backToMenu();
    }

    default void findRecipe(Account account) {
        boolean isFound = false;
        boolean incorrectInput = false;

        System.out.println("""
                Выберите по какому критерию искать назначенное лечение:
                1) ID рецепта
                2) ID амбулаторной карты
                3) ФИО пациента
                4) ID врача
                5) ФИО врача""");

        List<Recipe> relevantRecipes = null;
        switch (account.getScanner().nextLine()) {
            case "1" -> {
                System.out.println("Введите ID рецепта: ");
                String recipeID = account.getScanner().nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getRecipeID().equals(recipeID)).toList();
            }

            case "2" -> {
                System.out.println("Введите ID амбулаторной карты: ");
                String cardFileID = account.getScanner().nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getCardFileID().equals(cardFileID)).toList();
            }

            case "3" -> {
                System.out.println("Введите ФИО пациента: ");
                String patientFullName = account.getScanner().nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getPatientFullName().contains(patientFullName)).toList();
            }

            case "4" -> {
                System.out.println("Введите ID врача: ");
                String doctorID = account.getScanner().nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getDoctorID().contains(doctorID)).toList();
            }

            case "5" -> {
                System.out.println("Введите ФИО врача: ");
                String doctorFullName = account.getScanner().nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getDoctorFullName().contains(doctorFullName)).toList();
            }
        }

        if (relevantRecipes != null && !relevantRecipes.isEmpty()) {
            PrettyTable prettyTable = new PrettyTable();
            prettyTable.addHeader("Recipe ID", "Date", "Card file ID", "Patient full name", "Doctor ID", "Doctor full name");
            relevantRecipes.forEach(recipe -> prettyTable.addRow(recipe.toShortArray()));

            System.out.println(prettyTable + "\nДля вывода полной информации о назначенном лечении, введите его ID: ");
            String recipeID = account.getScanner().nextLine();

            for (Recipe recipe : relevantRecipes) {
                if (recipe.getRecipeID().equals(recipeID)) {
                    isFound = true;
                    ConsolePrinter.printRecipe(recipe);
                }
            }
        }

        account.checkConditions(isFound, incorrectInput);
        account.backToMenu();
    }
}