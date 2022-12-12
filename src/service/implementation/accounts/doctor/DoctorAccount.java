package service.implementation.accounts.doctor;

import models.implementation.doctors.Doctor;
import models.implementation.patients.Patient;
import models.implementation.recipes.Recipe;
import models.implementation.treatments.Treatment;
import service.implementation.accounts.Account;
import service.interfaces.Printable;
import service.interfaces.Searchable;
import utils.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class DoctorAccount extends Account implements Printable, Searchable {
    private Doctor currentDoctor;

    public DoctorAccount(String doctorID) {
        Optional<Doctor> relevantDoctor = FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                doctor -> doctor.getDoctorID().equals(doctorID)).findFirst();

        relevantDoctor.ifPresent(doctor -> this.currentDoctor = doctor);
    }

    // Главное меню врача
    @Override
    public void showMenu() {
        System.out.println("""
                Для работы с программой введите номер функции.
                Список функций:
                1)  Показать картотеку
                2)  Поиск амбулаторной карты
                
                3)  Список врачей
                4)  Поиск врача
                
                5)  Список записей на приём
                6)  Поиск записи на прием
                
                7)  Поиск назначенного лечения
                8)  Назначить лечение
                9)  Изменить назначенное лечение
                10) Удалить назначенное лечение
                
                11) Поиск рецепта
                12) Выписать рецепт
                13) Изменить выписанный рецепт
                14) Удалить выписанный рецепт
                
                15) Выход""");

        switch (scanner.nextLine()) {
            case "1" -> showCardFile(this);
            case "2" -> findPatientFile(this);

            case "3" -> showListOfDoctors(this);
            case "4" -> findDoctor(this);

            case "5" -> showListOfAppointments(this);
            case "6" -> findAppointment(this);

            case "7" -> findPrescribedTreatment(this);
            case "8" -> prescribeTreatment();
            case "9" -> changeTreatment();
            case "10" -> deleteTreatment();

            case "11" -> findRecipe(this);
            case "12" -> writeRecipe();
            case "13" -> changeRecipe();
            case "14" -> deleteRecipe();

            case "15" -> super.exit();
        }
    }



    // Под-метод для заполнения бланка о назначенном лечении или выписанном рецепте
    private StringBuilder askData(String additionalInfo) {
        StringBuilder data = new StringBuilder();
        String line;

        System.out.println(additionalInfo);
        do {
            line = scanner.nextLine();
            data.append(line).append("%n");
        } while (!line.equals(""));

        return data;
    }

    // Метод №8
    private void prescribeTreatment() {
        boolean incorrectInput = false;

        String[] csvData = new String[15];
        csvData[0] = String.valueOf(DataHelper.generateId(FilePath.treatments));

        LocalDateTime now = LocalDateTime.now();
        csvData[1] = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        csvData[2] = currentDoctor.getDoctorID();
        csvData[3] = currentDoctor.getFullName();

        System.out.println("Введите ID амбулаторной карты пациента: ");
        csvData[4] = scanner.nextLine();

        Optional<Patient> relevantPatient = FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                patient -> patient.getPatientID().equals(csvData[4])).findFirst();

        if (relevantPatient.isPresent()) {
            csvData[5] = relevantPatient.get().getFullName();

            System.out.println("Если Вы закончили заполнять, нажмите Enter");
            csvData[6] = askData("Жалобы пациента:").toString();
            csvData[7] = askData("Предварительный диагноз:").toString();
            csvData[8] = askData("План обследования:").toString();
            csvData[9] = askData("Результаты обследования:").toString();
            csvData[10] = askData("Клинический диагноз:").toString();
            csvData[11] = askData("План лечения:").toString();
            csvData[12] = askData("Ход лечения:").toString();
            csvData[13] = askData("Рекомендации:").toString();
            csvData[14] = askData("Направлен к:").toString();
        }

        else {
            incorrectInput = true;
        }

        if (!incorrectInput) {
            FileWriter.appendToFile(csvData, FilePath.treatments);
            System.out.println("Лечение успешно назначено!");
        }

        super.checkConditions(true, incorrectInput);
        backToMenu();
    }



    // Метод №9
    private void changeTreatment() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Treatment> treatments = FileHelper.getFileData(Treatment.class, FilePath.treatments);

        System.out.println("Введите ID назначенного лечения: ");
        String treatmentID = scanner.nextLine();

        for (Treatment treatment : treatments) {
            if (treatment.getTreatmentID().equals(treatmentID)) {
                isFound = true;
                ConsolePrinter.printTreatment(treatment);

                System.out.println("""
                        Выберите какое поле вы хотите изменить:
                        1)  Пациент
                        2)  Жалобы
                        3)  Предварительный диагноз
                        4)  План обследования
                        5)  Результаты обследования
                        6)  Клинический диагноз
                        7)  План лечения
                        8)  Ход лечения
                        9)  Рекомендации
                        10) Направлен к
                        Для множественного выбора введите цифры через пробел""");

                // Для удаления повторяющихся элементов
                HashSet<String> choiceSet = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));

                loop:
                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> {
                            System.out.println("Введите ID амбулаторной карты пациента: ");
                            String cardFileID = scanner.nextLine();

                            Optional<Patient> relevantPatient =
                                    FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                                    patient -> patient.getPatientID().equals(cardFileID)).findFirst();

                            if (relevantPatient.isPresent()) {
                                treatment.setCardFileID(relevantPatient.get().getPatientID());
                                treatment.setPatientFullName(relevantPatient.get().getFullName());
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "2" -> treatment.setComplaints(askData("Жалобы: ").toString());
                        case "3" -> treatment.setPreliminaryDiagnosis(askData("Предварительный диагноз: ").toString());
                        case "4" -> treatment.setSurveyPlan(askData("План обследования: ").toString());
                        case "5" -> treatment.setSurveyData(askData("Результаты обследования: ").toString());
                        case "6" -> treatment.setClinicalDiagnosis(askData("Клинический диагноз: ").toString());
                        case "7" -> treatment.setTreatmentPlan(askData("План лечения:").toString());
                        case "8" -> treatment.setTreatmentProgress(askData("Ход лечения:").toString());
                        case "9" -> treatment.setRecommendations(askData("Рекомендации:").toString());
                        case "10" -> treatment.setDirectedTo(askData("Направлен к:").toString());
                    }
                }
            }
        }

        if (isFound && !incorrectInput) {
            FileWriter.rewriteFile(treatments, FilePath.treatments);
            System.out.println("Информация о назначенном лечении успешно изменена!");
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }



    // Метод №10
    private void deleteTreatment() {
        List<Treatment> treatments = FileHelper.getFileData(Treatment.class, FilePath.treatments);

        System.out.println("Введите ID назначенного лечения: ");
        String treatmentID = scanner.nextLine();

        boolean isFound = treatments.removeIf(treatment -> treatment.getTreatmentID().equals(treatmentID));

        if (isFound) {
            FileWriter.rewriteFile(treatments, FilePath.treatments);
            System.out.println("Назначенное лечение успешно удалено!");
        }

        super.checkConditions(isFound, false);
        backToMenu();
    }



    // Метод №12
    private void writeRecipe() {
        boolean incorrectInput = false;

        String[] csvData = new String[9];
        csvData[0] = String.valueOf(DataHelper.generateId(FilePath.recipes));

        LocalDateTime now = LocalDateTime.now();
        csvData[1] = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        System.out.println("Введите ID амбулаторной карты пациента: ");
        String cardFileID = scanner.nextLine();

        Optional<Patient> relevantPatient = FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                patient -> patient.getPatientID().equals(cardFileID)).findFirst();

        if (relevantPatient.isPresent()) {
            csvData[2] = relevantPatient.get().getPatientID();
            csvData[3] = relevantPatient.get().getFullName();
            csvData[4] = relevantPatient.get().getDateOfBirth();

            csvData[5] = currentDoctor.getDoctorID();
            csvData[6] = currentDoctor.getFullName();

            System.out.println("""
                    Выберите тип рецепта:
                    1) Взрослый
                    2) Детский""");

            switch (scanner.nextLine()) {
                case "1" -> csvData[7] = "Adult";
                case "2" -> csvData[7] = "Child";

                default -> incorrectInput = true;
            }

            if (!incorrectInput) {
                csvData[8] = askData("Введите наименование препарата:").toString();
            }
        }

        else {
            incorrectInput = true;
        }

        if (!incorrectInput) {
            FileWriter.appendToFile(csvData, FilePath.recipes);
            System.out.println("Рецепт на препарат успешно добавлен!");
        }

        super.checkConditions(true, incorrectInput);
        backToMenu();
    }



    // Метод №13
    private void changeRecipe() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Recipe> recipes = FileHelper.getFileData(Recipe.class, FilePath.recipes);

        System.out.println("Введите ID выписанного рецепта: ");
        String recipeID = scanner.nextLine();

        for (Recipe recipe : recipes) {
            if (recipe.getRecipeID().equals(recipeID)) {
                isFound = true;
                ConsolePrinter.printRecipe(recipe);

                System.out.println("""
                        Выберите какое поле вы хотите изменить:
                        1) Пациент
                        2) Тип рецепта
                        3) Наименование препарата
                        Для множественного выбора введите цифры через пробел""");

                // Для удаления повторяющихся элементов
                HashSet<String> choiceSet = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));

                loop:
                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> {
                            System.out.println("Введите ID амбулаторной карты пациента: ");
                            String cardFileID = scanner.nextLine();

                            Optional<Patient> relevantPatient =
                                    FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                                    patient -> patient.getPatientID().equals(cardFileID)).findFirst();

                            if (relevantPatient.isPresent()) {
                                recipe.setCardFileID(relevantPatient.get().getPatientID());
                                recipe.setPatientFullName(relevantPatient.get().getFullName());
                                recipe.setAge(relevantPatient.get().getDateOfBirth());
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "2" -> {
                            System.out.println("""
                                Выберите тип рецепта:
                                1) Взрослый
                                2) Детский""");

                            switch (scanner.nextLine()) {
                                case "1" -> recipe.setTypeOfRecipe("Adult");
                                case "2" -> recipe.setTypeOfRecipe("Child");

                                default -> {
                                    incorrectInput = true;
                                    break loop;
                                }
                            }
                        }

                        case "3" -> recipe.setDrugName(askData("Введите наименование препарата:").toString());
                    }
                }
            }
        }

        if (isFound && !incorrectInput) {
            FileWriter.rewriteFile(recipes, FilePath.recipes);
            System.out.println("Данные выписанного рецепта успешно изменены!");
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }



    // Метод №14
    private void deleteRecipe() {
        List<Recipe> recipes = FileHelper.getFileData(Recipe.class, FilePath.recipes);

        System.out.println("Введите ID выписанного рецепта: ");
        String recipeID = scanner.nextLine();

        boolean isFound = recipes.removeIf(recipe -> recipe.getRecipeID().equals(recipeID));

        if (isFound) {
            FileWriter.rewriteFile(recipes, FilePath.recipes);
            System.out.println("Рецепт успешно удален!");
        }

        super.checkConditions(isFound, false);
        backToMenu();
    }
}
