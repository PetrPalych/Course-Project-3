package service.implementation.accounts.registrar;

import models.implementation.appointments.Appointment;
import models.implementation.doctors.Doctor;
import models.implementation.patients.Patient;
import models.implementation.recipes.Recipe;
import models.implementation.treatments.Treatment;
import models.interfaces.Convertible;
import service.implementation.accounts.Account;
import service.interfaces.Displayable;
import service.interfaces.Searchable;
import utils.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class RegistrarAccount extends Account implements Displayable, Searchable {
    // Главное меню регистратора
    @Override
    public void showMenu() {
        System.out.println("""
                Для работы с программой введите номер функции.
                Список функций:
                1)  Показать картотеку
                2)  Поиск амбулаторной карты
                3)  Изменить информацию в амбулаторной карте пациента
                4)  Добавить амбулаторную карту
                5)  Удалить амбулаторную карту
                
                6)  Список врачей
                7)  Поиск врача
                
                8)  Список записей на приём
                9)  Поиск записи на приём
                10) Изменить запись на приём
                11) Добавить запись на приём
                12) Удалить запись на приём
                
                13) Поиск назначенного лечения
                14) Поиск выписанного рецепта
                
                15) Выход""");

        switch (scanner.nextLine()) {
            case "1" -> showCardFile();
            case "2" -> findPatientFile();
            case "3" -> changePatientFile();
            case "4" -> addPatientFile();
            case "5" -> deleteCardFile();

            case "6" -> showListOfDoctors(this);
            case "7" -> findDoctor(this);

            case "8" -> showListOfAppointments();
            case "9" -> findAppointment();
            case "10" -> changeAppointment();
            case "11" -> addAppointment();
            case "12" -> deleteAppointment();

            case "13" -> findPrescribedTreatment();
            case "14" -> findRecipe();

            case "15" -> super.exit();

            default -> showMenu();
        }
    }



    // Метод №1 #Вывод
    private void showCardFile() {
        System.out.println(super.makeTableOf(
                FileHelper.getFileData(Patient.class, FilePath.patients), FilePath.patients));

        super.backToMenu();
    }

    // Метод №2 #Поиск
    private void findPatientFile() {
        boolean isFound = false;
        boolean incorrectInput = false;

        System.out.println("""
                Выберите по какому критерию искать:
                1) ID
                2) ФИО
                3) Персональный номер""");

        List<? extends Convertible> relevantPatients = null;
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID амбулаторной карты: ");
                String id = scanner.nextLine();
                relevantPatients = super.findInList(
                        FileHelper.getFileData(Patient.class, FilePath.patients), id, "getId");
            }

            case "2" -> {
                System.out.println("Введите ФИО пациента: ");
                String fullName = scanner.nextLine();
                relevantPatients = FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                        patient -> patient.getFullName().contains(fullName)).toList();
            }

            case "3" -> {
                System.out.println("Введите персональный номер пациента: ");
                String personalNumber = scanner.nextLine();
                relevantPatients = super.findInList(
                        FileHelper.getFileData(Patient.class, FilePath.patients),
                        personalNumber,
                        "getPersonalNumber");
            }
        }

        if (relevantPatients != null && !relevantPatients.isEmpty()) {
            isFound = true;
            System.out.println(super.makeTableOf(relevantPatients, FilePath.patients));
        }

        super.checkConditions(isFound, incorrectInput);
        super.backToMenu();
    }

    // Метод №3 #Изменение
    private void changePatientFile() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Patient> patients = FileHelper.getFileData(Patient.class, FilePath.patients);

        System.out.println("Введите ID пациента или его персональный номер: ");
        String idOrPersonalNumber = scanner.nextLine();

        for (Patient patient : patients) {
            if (String.valueOf(patient.getPatientID()).equals(idOrPersonalNumber) ||
                    patient.getPersonalNumber().equals(idOrPersonalNumber)) {

                isFound = true;
                ConsolePrinter.printPatient(patient);

                System.out.println("""
                        Выберите какое поле Вы хотите изменить:
                        1) ФИО
                        2) Пол
                        3) Дату рождения
                        4) Регион
                        5) Город
                        6) Адрес
                        7) Телефонный номер
                        8) Персональный номер
                        Для множественного выбора введите цифры через пробел""");

                HashSet<String> choiceSet = new HashSet<>(List.of(scanner.nextLine().split(" ")));

                loop:
                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> {
                            System.out.println("Введите новое ФИО пациента: ");
                            patient.setFullName(scanner.nextLine());
                        }

                        case "2" -> {
                            System.out.println("""
                                    Выберите новый пол пациента:
                                    1) Мужской
                                    2) Женский""");

                            switch (scanner.nextLine()) {
                                case "1" -> patient.setSex("M");
                                case "2" -> patient.setSex("F");

                                default -> {
                                    incorrectInput = true;
                                    break loop;
                                }
                            }
                        }

                        case "3" -> {
                            System.out.println("Введите новую дату рождения пациента: (dd.MM.yyyy)");
                            String dateOfBirth = scanner.nextLine();

                            if (DataHelper.dateIsValid(dateOfBirth)) {
                                patient.setDateOfBirth(DataHelper.formatDate(dateOfBirth));
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "4" -> {
                            System.out.println("Введите новый регион проживания: ");
                            patient.setRegion(scanner.nextLine());
                        }

                        case "5" -> {
                            System.out.println("Введите новый город проживания: ");
                            patient.setCity(scanner.nextLine());
                        }

                        case "6" -> {
                            System.out.println("Введите новый адрес: ");
                            patient.setAddress(scanner.nextLine());
                        }

                        case "7" -> {
                            System.out.println("Введите новый телефонный номер (+x xxx xxx xxx): ");
                            String newPhoneNumber = scanner.nextLine();

                            if (DataHelper.phoneNumberIsValid(newPhoneNumber)) {
                                patient.setPhoneNumber(newPhoneNumber);
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }

                        }

                        case "8" -> {
                            System.out.println("Введите новый персональный номер: ");
                            patient.setPersonalNumber(scanner.nextLine());
                        }
                    }
                }
            }
        }

        if (isFound && !incorrectInput) {
            FileWriter.rewriteFile(patients, FilePath.patients);
            System.out.println("Данные пациента успешно изменены!");
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }

    // Метод №4 #Добавление
    private void addPatientFile() {
        boolean incorrectInput = false;

        String[] csvData = new String[9];
        csvData[0] = String.valueOf(DataHelper.generateId(FilePath.patients));

        System.out.println("Введите ФИО нового пациента: ");
        csvData[1] = scanner.nextLine();

        System.out.println("""
                Выберите пол нового пациента:
                1) Мужской
                2) Женский""");

        switch (scanner.nextLine()) {
            case "1" -> csvData[2] = "M";
            case "2" -> csvData[2] = "F";

            default -> incorrectInput = true;
        }

        if (!incorrectInput) {
            System.out.println("Введите дату рождения нового пациента (dd.MM.yyyy): ");
            String dateOfBirth = scanner.nextLine();

            incorrectInput = !DataHelper.dateIsValid(dateOfBirth);
            if (!incorrectInput) {
                csvData[3] = DataHelper.formatDate(dateOfBirth);

                System.out.println("Введите регион проживания нового пациента: ");
                csvData[4] = scanner.nextLine();

                System.out.println("Введите город проживания нового пациента: ");
                csvData[5] = scanner.nextLine();

                System.out.println("Введите адрес проживания нового пациента: ");
                csvData[6] = scanner.nextLine();

                System.out.println("Введите номер телефона нового пациента (+x xxx xxx xxx): ");
                csvData[7] = scanner.nextLine();

                incorrectInput = !DataHelper.phoneNumberIsValid(csvData[7]);
                if (!incorrectInput) {
                    System.out.println("Введите персональный номер пациента: ");
                    csvData[8] = scanner.nextLine();

                    incorrectInput = !DataHelper.personalNumberIsUnique(csvData[8]);
                    if (!incorrectInput) {
                        FileWriter.appendToFile(csvData, FilePath.patients);
                        System.out.println("Новый пациент успешно добавлен!");
                    }
                }
            }
        }

        super.checkConditions(true, incorrectInput);
        super.backToMenu();
    }

    // Метод №5 #Удаление
    private void deleteCardFile() {
        boolean isFound = false;

        List<Patient> cardFile = FileHelper.getFileData(Patient.class, FilePath.patients);

        System.out.println("""
                Выберите способ удаления:
                1) Удаление по ID
                2) Множественное удаление""");

        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID амбулаторной карты пациента: ");
                String id = scanner.nextLine();
                isFound = cardFile.removeIf(patient -> patient.getPatientID().equals(id));
            }

            case "2" -> {
                System.out.println(super.makeTableOf(FileHelper.getFileData(
                        Patient.class, FilePath.patients), FilePath.patients) +
                        "\nВведите ID амбулаторных(-ой) карт(-ы), которые(-ую) хотите удалить: ");

                HashSet<String> idForDeleting = new HashSet<>(List.of(scanner.nextLine().split(" ")));
                for (String id : idForDeleting) {
                    isFound = cardFile.removeIf(patient -> patient.getPatientID().equals(id));
                }
            }
        }

        if (isFound) {
            FileWriter.rewriteFile(cardFile, FilePath.patients);
            System.out.println("Амбулаторная(-ые) карта(-ы) удалена(-ы) из картотеки!");
        }

        super.checkConditions(isFound, false);
        super.backToMenu();
    }



    // Метод №8 #Вывод
    private void showListOfAppointments() {
        System.out.println(super.makeTableOf(
                FileHelper.getFileData(Appointment.class, FilePath.appointments), FilePath.appointments));

        super.backToMenu();
    }

    // Метод №9 #Вывод
    private void findAppointment() {
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
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID приема: ");
                String contractId = scanner.nextLine();
                relevantContracts = super.findInList(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments),
                        contractId,
                        "getContractId");
            }

            case "2" -> {
                System.out.println("Введите ID врача: ");
                String doctorId = scanner.nextLine();
                relevantContracts = super.findInList(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments),
                        doctorId,
                        "getDoctorId");
            }

            case "3" -> {
                System.out.println("Введите ФИО врача: ");
                String doctorFullName = scanner.nextLine();
                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getDoctorFullName().contains(doctorFullName)).toList();
            }

            case "4" -> {
                System.out.println("Введите время приема: (24:60)");
                String receptionTime = scanner.nextLine();
                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getReceptionTime().contains(receptionTime)).toList();
            }

            case "5" -> {
                System.out.println("Введите дату приема: (dd.MM.yyyy)");
                String receptionDate = scanner.nextLine();
                relevantContracts = super.findInList(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments),
                        receptionDate,
                        "getReceptionDate");
            }

            case "6" -> {
                System.out.println("Введите время приема: (24:60)");
                String receptionTime = scanner.nextLine();

                System.out.println("Введите дату приема: (dd.MM.yyyy)");
                String receptionDate = scanner.nextLine();

                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getReceptionTime().contains(receptionTime) &&
                                contract.getReceptionDate().equals(receptionDate)).toList();
            }

            case "7" -> {
                System.out.println("Введите ФИО пациента:");
                String patientFullName = scanner.nextLine();
                relevantContracts = FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        contract -> contract.getPatientFullName().contains(patientFullName)).toList();
            }

            case "8" -> {
                System.out.println("""
                        Выберите тип статуса оплаты:
                        1) Оплачено
                        2) Не оплачено""");

                switch (scanner.nextLine()) {
                    case "1" -> relevantContracts = super.findInList(
                            FileHelper.getFileData(Appointment.class, FilePath.appointments),
                            "Paid",
                            "getStatus");

                    case "2" -> relevantContracts = super.findInList(
                            FileHelper.getFileData(Appointment.class, FilePath.appointments),
                            "Not Paid",
                            "getStatus");
                }
            }
        }

        if (relevantContracts != null && !relevantContracts.isEmpty()) {
            isFound = true;
            System.out.println(super.makeTableOf(relevantContracts, FilePath.appointments));
        }

        super.checkConditions(isFound, false);
        super.backToMenu();
    }

    // Метод №10 #Изменение
    private void changeAppointment() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Appointment> contracts = FileHelper.getFileData(Appointment.class, FilePath.appointments);

        System.out.println("Введите ID приема: ");
        String contractId = scanner.nextLine();

        for (Appointment contract : contracts) {
            if (contract.getContractID().equals(contractId)) {
                isFound = true;
                ConsolePrinter.printAppointment(contract);

                System.out.println("""
                        Выберите какое поле вы хотите изменить:
                        1) Врач
                        2) Номер кабинета
                        3) Время приема
                        4) Дата приема
                        5) Пациент
                        Для множественного выбора введите цифры через пробел""");

                // Для удаления повторяющихся элементов
                HashSet<String> choiceSet = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));

                loop:
                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> {
                            System.out.println("Введите ID врача: ");
                            String doctorId = scanner.nextLine();

                            Optional<Doctor> relevantDoctor =
                                    FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                                            doctor -> doctor.getDoctorID().equals(doctorId)).findFirst();

                            if (relevantDoctor.isPresent()) {
                                contract.setDoctorID(relevantDoctor.get().getDoctorID());
                                contract.setDoctorFullName(relevantDoctor.get().getFullName());
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "2" -> {
                            System.out.println("Введите номер кабинета: ");
                            contract.setCabinetNumber(scanner.nextLine());
                        }

                        case "3" -> {
                            System.out.println("Введите время приема: ");
                            String receptionTime = scanner.nextLine();

                            if (DataHelper.timeIsValid(receptionTime)) {
                                contract.setReceptionTime(receptionTime);
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "4" -> {
                            System.out.println("Введите дату приема: ");
                            String receptionDate = scanner.nextLine();

                            if (DataHelper.dateIsValid(receptionDate)) {
                                contract.setReceptionDate(receptionDate);
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "5" -> {
                            System.out.println("Введите ID амбулаторной карты пациента: ");
                            String patientCardFileId = scanner.nextLine();

                            Optional<Patient> relevantPatient =
                                    FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                                            patient -> patient.getPatientID().equals(patientCardFileId)).findFirst();

                            if (relevantPatient.isPresent()) {
                                contract.setCardFileID(relevantPatient.get().getPatientID());
                                contract.setPatientFullName(relevantPatient.get().getFullName());
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }
                    }
                }
            }
        }

        if (isFound && !incorrectInput) {
            FileWriter.rewriteFile(contracts, FilePath.appointments);
            System.out.println("Данные приема успешно изменены!");
        }

        super.checkConditions(isFound, incorrectInput);
        super.backToMenu();
    }

    // Метод №11 #Добавление
    private void addAppointment() {
        boolean incorrectInput;

        String[] csvData = new String[10];
        csvData[0] = String.valueOf(DataHelper.generateId(FilePath.appointments));

        System.out.println("Введите ID врача, к которому назначен прием: ");
        csvData[1] = scanner.nextLine();

        Optional<Doctor> relevantDoctor = FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                doctor -> doctor.getDoctorID().equals(csvData[1])).findFirst();

        if (relevantDoctor.isPresent()) {
            csvData[2] = relevantDoctor.get().getFullName();

            System.out.println("Введите номер кабинета: ");
            csvData[3] = scanner.nextLine();

            System.out.println("Введите время приема: ");
            csvData[4] = scanner.nextLine();

            incorrectInput = !DataHelper.timeIsValid(csvData[4]);
            if (!incorrectInput) {
                System.out.println("Введите дату приема: ");
                csvData[5] = scanner.nextLine();

                incorrectInput = !DataHelper.dateIsValid(csvData[5]);
                if (!incorrectInput) {
                    System.out.println("Введите ID амбулаторной карты пациента: ");
                    csvData[6] = scanner.nextLine();

                    Optional<Patient> relevantPatient =
                            FileHelper.getFileData(Patient.class, FilePath.patients).stream().filter(
                                    patient -> patient.getPatientID().equals(csvData[6])).findFirst();

                    if (relevantPatient.isPresent()) {
                        csvData[7] = relevantPatient.get().getFullName();

                        System.out.println("Введите стоимость услуги: ");
                        csvData[8] = scanner.nextLine();
                        csvData[9] = "Not Paid";
                    }

                    else {
                        incorrectInput = true;
                    }
                }
            }
        }

        else {
            incorrectInput = true;
        }

        if (!incorrectInput) {
            FileWriter.appendToFile(csvData, FilePath.appointments);
        }

        super.checkConditions(true, incorrectInput);
        super.backToMenu();
    }

    // Метод №12 #Удаление
    private void deleteAppointment() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Appointment> contracts = FileHelper.getFileData(Appointment.class, FilePath.appointments);

        System.out.println("""
                Выберите способ удаления:
                1) Удаление по ID
                2) Множественное удаление""");

        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID приема: ");
                String contractId = scanner.nextLine();
                isFound = contracts.removeIf(contract -> contract.getContractID().equals(contractId));
            }

            case "2" -> {
                System.out.println(super.makeTableOf(
                        FileHelper.getFileData(Appointment.class, FilePath.appointments), FilePath.appointments) +
                        "\nВведите ID приема(-ов), который(-ые) хотите удалить: ");

                HashSet<String> idForDeleting = new HashSet<>(List.of(scanner.nextLine().split(" ")));
                for (String id : idForDeleting) {
                    isFound = contracts.removeIf(contract -> contract.getContractID().equals(id));
                }
            }
        }

        if (isFound && !incorrectInput) {
            FileWriter.rewriteFile(contracts, FilePath.appointments);
            System.out.println("Прием(-ы) удален(-ы)!");
        }

        super.checkConditions(isFound, incorrectInput);
        super.backToMenu();
    }



    // Метод №13
    private void findPrescribedTreatment() {
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
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID лечения: ");
                String treatmentID = scanner.nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getTreatmentID().equals(treatmentID)).toList();
            }

            case "2" -> {
                System.out.println("Введите дату назначения лечения: ");
                String date = scanner.nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getDate().equals(date)).toList();
            }

            case "3" -> {
                System.out.println("Введите ID врача: ");
                String doctorID = scanner.nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getDoctorID().equals(doctorID)).toList();
            }

            case "4" -> {
                System.out.println("Введите ФИО врача: ");
                String doctorFullName = scanner.nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getDoctorFullName().contains(doctorFullName)).toList();
            }

            case "5" -> {
                System.out.println("Введите ID амбулаторной карты пациента: ");
                String cardFileID = scanner.nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getCardFileID().equals(cardFileID)).toList();
            }

            case "6" -> {
                System.out.println("Введите ФИО пациента: ");
                String patientFullName = scanner.nextLine();
                relevantTreatments = FileHelper.getFileData(Treatment.class, FilePath.treatments).stream().filter(
                        treatment -> treatment.getPatientFullName().contains(patientFullName)).toList();
            }
        }

        if (relevantTreatments != null && !relevantTreatments.isEmpty()) {
            PrettyTable prettyTable = new PrettyTable();
            prettyTable.addHeader("Treatment ID", "Date", "Doctor ID", "Doctor full name", "Card file ID", "Patient full name");
            relevantTreatments.forEach(treatment -> prettyTable.addRow(treatment.toShortArray()));

            System.out.println(prettyTable + "\nДля вывода полной информации о назначенном лечении, введите его ID: ");
            String treatmentID = scanner.nextLine();

            for (Treatment treatment : relevantTreatments) {
                if (treatment.getTreatmentID().equals(treatmentID)) {
                    isFound = true;
                    ConsolePrinter.printTreatment(treatment);
                }
            }
        }

        super.checkConditions(isFound, incorrectInput);
        super.backToMenu();
    }

    // Метод №14
    private void findRecipe() {
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
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID рецепта: ");
                String recipeID = scanner.nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getRecipeID().equals(recipeID)).toList();
            }

            case "2" -> {
                System.out.println("Введите ID амбулаторной карты: ");
                String cardFileID = scanner.nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getCardFileID().equals(cardFileID)).toList();
            }

            case "3" -> {
                System.out.println("Введите ФИО пациента: ");
                String patientFullName = scanner.nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getPatientFullName().contains(patientFullName)).toList();
            }

            case "4" -> {
                System.out.println("Введите ID врача: ");
                String doctorID = scanner.nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getDoctorID().contains(doctorID)).toList();
            }

            case "5" -> {
                System.out.println("Введите ФИО врача: ");
                String doctorFullName = scanner.nextLine();
                relevantRecipes = FileHelper.getFileData(Recipe.class, FilePath.recipes).stream().filter(
                        recipe -> recipe.getDoctorFullName().contains(doctorFullName)).toList();
            }
        }

        if (relevantRecipes != null && !relevantRecipes.isEmpty()) {
            PrettyTable prettyTable = new PrettyTable();
            prettyTable.addHeader("Recipe ID", "Date", "Card file ID", "Patient full name", "Doctor ID", "Doctor full name");
            relevantRecipes.forEach(recipe -> prettyTable.addRow(recipe.toShortArray()));

            System.out.println(prettyTable + "\nДля вывода полной информации о назначенном лечении, введите его ID: ");
            String recipeID = scanner.nextLine();

            for (Recipe recipe : relevantRecipes) {
                if (recipe.getRecipeID().equals(recipeID)) {
                    isFound = true;
                    ConsolePrinter.printRecipe(recipe);
                }
            }
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }
}
