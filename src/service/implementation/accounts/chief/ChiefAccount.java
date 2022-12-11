package service.implementation.accounts.chief;

import models.implementation.doctors.Doctor;
import models.implementation.employees.Employee;
import models.interfaces.Convertible;
import service.implementation.accounts.Account;
import utils.*;

import java.util.*;

public class ChiefAccount extends Account {
    @Override
    public void showMenu() {
        System.out.println("""
                Для работы с программой введите номер функции.
                Список функций:
                1)  Список учетных записей
                2)  Поиск учетной записи
                3)  Изменить данные учетной записи
                4)  Добавить новую учетную запись
                5)  Удалить учетную запись
                
                6)  Список врачей
                7)  Поиск врача
                8)  Изменить информацию о враче
                9)  Добавить врача
                10) Удалить врача
                
                11) Выйти из программы""");

        switch (scanner.nextLine()) {
            case "1" -> showListOfAccounts();
            case "2" -> findAccount();
            case "3" -> changeAccountData();
            case "4" -> addAccount();
            case "5" -> deleteAccount();

            case "6" -> showListOfDoctors();
            case "7" -> findDoctor();
            case "8" -> changeDoctorsData();
            case "9" -> addDoctor();
            case "10" -> deleteDoctor();

            case "11" -> super.exit();

            default -> showMenu();
        }
    }



    // Метод №1 #Вывод
    private void showListOfAccounts() {
        System.out.println(super.makeTableOf(
                FileHelper.getFileData(Employee.class, FilePath.authorization), FilePath.authorization));

        super.backToMenu();
    }

    // Метод №2 #Поиск
    private void findAccount() {
        boolean isFound = false;

        System.out.println("""
                Выберите по какому критерию искать:
                1) Логин
                2) ФИО""");

        List<? extends Convertible> relevantEmployees = null;
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите логин сотрудника: ");
                String username = scanner.nextLine();
                relevantEmployees = super.findInList(
                        FileHelper.getFileData(Employee.class, FilePath.authorization),
                        username,
                        "getUsername");
            }

            case "2" -> {
                System.out.println("Введите ФИО сотрудника: ");
                String fullName = scanner.nextLine();
                relevantEmployees = FileHelper.getFileData(Employee.class, FilePath.authorization).stream().filter(
                        employee -> employee.getDoctorID().contains(fullName)).toList();
            }
        }

        if (relevantEmployees != null && !relevantEmployees.isEmpty()) {
            isFound = true;
            System.out.println(super.makeTableOf(relevantEmployees, FilePath.authorization));
        }

        super.checkConditions(isFound, false);
        super.backToMenu();
    }

    // Метод №3 #Изменение
    private void changeAccountData() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Employee> employees = FileHelper.getFileData(Employee.class, FilePath.authorization);

        System.out.println("Введите логин сотрудника: ");
        String username = scanner.nextLine();

        for (Employee employee : employees) {
            if (employee.getUsername().equals(username)) {
                isFound = true;
                ConsolePrinter.printEmployee(employee);

                System.out.println("""
                        Выберите какое поле вы хотите изменить:
                        1) Логин
                        2) Пароль
                        3) Тип учетной записи
                        4) ID врача
                        Для множественного выбора введите цифры через пробел""");

                // Для удаления повторяющихся элементов
                HashSet<String> choiceSet = new HashSet<>(Arrays.asList(scanner.nextLine().split(" ")));

                loop:
                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> {
                            System.out.println("Введите новый логин сотрудника: ");
                            String newUsername = scanner.nextLine();

                            if (DataHelper.usernameIsUnique(newUsername)) {
                                employee.setUsername(newUsername);
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "2" -> {
                            System.out.println("Введите новый пароль сотрудника: ");
                            employee.setPassword(scanner.nextLine());
                        }

                        case "3" -> {
                            System.out.println("""
                                Выберите новый тип учетной записи сотрудника:
                                1) Регистратор
                                2) Кассир
                                3) Врач""");

                            switch (scanner.nextLine()) {
                                case "1" -> {
                                    employee.setAccountType("Registrar");
                                    employee.setDoctorID("0");
                                }

                                case "2" -> {
                                    employee.setAccountType("Cashier");
                                    employee.setDoctorID("0");
                                }

                                case "3" -> {
                                    employee.setAccountType("Doctor");

                                    System.out.println("Введите ID врача: ");
                                    String doctorID = scanner.nextLine();

                                    Optional<Doctor> relevantDoctor =
                                            FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                                                    doctor -> doctor.getDoctorID().equals(doctorID)).findFirst();

                                    if (relevantDoctor.isPresent()) {
                                        employee.setDoctorID(doctorID);
                                    }

                                    else {
                                        incorrectInput = true;
                                        break loop;
                                    }
                                }

                                default -> {
                                    incorrectInput = true;
                                    break loop;
                                }
                            }
                        }

                        case "4" -> {
                            System.out.println("Введите новый ID врача: ");
                            String doctorID = scanner.nextLine();

                            Optional<Doctor> relevantDoctor =
                                    FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                                            doctor -> doctor.getDoctorID().equals(doctorID)).findFirst();

                            if (relevantDoctor.isPresent() && employee.getAccountType().equals("Doctor")) {
                                employee.setDoctorID(scanner.nextLine());
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
            FileWriter.rewriteFile(employees, FilePath.authorization);
            System.out.println("Данные сотрудника успешно изменены!");
        }

        super.checkConditions(isFound, incorrectInput);
        super.backToMenu();
    }

    // Метод №4 #Добавление
    private void addAccount() {
        boolean incorrectInput;

        String[] csvData = new String[4];

        System.out.println("Введите логин новой учетной записи: ");
        csvData[0] = scanner.nextLine();

        incorrectInput = !DataHelper.usernameIsUnique(csvData[0]);
        if (!incorrectInput) {
            System.out.println("Введите пароль новой учетной записи: ");
            csvData[1] = scanner.nextLine();

            System.out.println("""
                    Выберите тип учетной записи:
                    1) Регистратор
                    2) Кассир
                    3) Доктор""");

            incorrectInput = switch (scanner.nextLine()) {
                case "1" -> {
                    csvData[2] = "Registrar";
                    csvData[3] = "0";
                    yield false;
                }

                case "2" -> {
                    csvData[2] = "Cashier";
                    csvData[3] = "0";
                    yield false;
                }

                case "3" -> {
                    csvData[2] = "Doctor";

                    System.out.println("Введите ID врача: ");
                    csvData[3] = scanner.nextLine();

                    yield FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().noneMatch(
                            doctor -> doctor.getDoctorID().equals(csvData[3]));
                }

                default -> true;
            };
        }

        if (!incorrectInput) {
            FileWriter.appendToFile(csvData, FilePath.authorization);
            System.out.println("Новый сотрудник успешно добавлен!");
        }

        super.checkConditions(true, incorrectInput);
        super.backToMenu();
    }

    // Метод №5 #Удаление
    private void deleteAccount() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Employee> employees = FileHelper.getFileData(Employee.class, FilePath.authorization);

        System.out.println("""
                Выберите способ удаления:
                1) Удаление по логину
                2) Множественное удаление""");

        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите логин сотрудника: ");
                String username = scanner.nextLine();

                isFound = employees.removeIf(employee -> employee.getUsername().equals(username));
            }

            case "2" -> {
                System.out.println(super.makeTableOf(employees, FilePath.authorization) +
                        "\nВведите ID аккаунта(-ов), который(-ые) хотите удалить: ");

                HashSet<String> idForDeleting = new HashSet<>(List.of(scanner.nextLine().split(" ")));
                for (String id : idForDeleting) {
                    isFound = employees.removeIf(
                            employee -> employee.getDoctorID().equals(id) &&
                                    !employee.getAccountType().equals("Chief doctor"));
                }
            }
        }

        if (isFound && !incorrectInput){
            FileWriter.rewriteFile(employees, FilePath.authorization);
            System.out.println("Сотрудник(-и) удалён(-ы)!");
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }



    // Метод №6 #Вывод
    private void showListOfDoctors() {
        System.out.println(super.makeTableOf(FileHelper.getFileData(Doctor.class, FilePath.doctors), FilePath.doctors));
        backToMenu();
    }

    // Метод №7 #Поиск
    private void findDoctor() {
        boolean isFound = false;
        boolean incorrectInput = false;

        System.out.println("""
                Выберите по какому критерию искать врача:
                1) ID
                2) ФИО
                3) Специализация
                4) Рабочие дни""");

        List<? extends Convertible> relevantDoctors = null;
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID врача: ");
                String id = scanner.nextLine();
                relevantDoctors = super.findInList(
                        FileHelper.getFileData(Doctor.class, FilePath.doctors), id, "getId");
            }

            case "2" -> {
                System.out.println("Введите ФИО врача: ");
                String fullName = scanner.nextLine();
                relevantDoctors = FileHelper.getFileData(Doctor.class, FilePath.doctors).stream().filter(
                        doctor -> doctor.getFullName().contains(fullName)).toList();
            }

            case "3" -> {
                System.out.println("Введите специализацию врача: ");
                String specialization = scanner.nextLine();
                relevantDoctors = super.findInList(
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

                // Для удаления повторяющихся элементов
                HashSet<String> choiceSet = new HashSet<>(List.of(scanner.nextLine().split(" ")));

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
                        doctor -> {
                            for (String day : days) {
                                if (doctor.getWorkingDays().contains(day)) {
                                    return true;
                                }
                            }
                            return false;
                        }).toList();
            }
        }

        if (relevantDoctors != null && !relevantDoctors.isEmpty()) {
            isFound = true;
            System.out.println(super.makeTableOf(relevantDoctors, FilePath.doctors));
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }

    // Метод №8 #Изменение
    private void changeDoctorsData() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Doctor> doctors = FileHelper.getFileData(Doctor.class, FilePath.doctors);

        System.out.println("Введите ID врача: ");
        String id = scanner.nextLine();

        loop:
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorID().equals(id)) {
                isFound = true;

                System.out.println("""
                        Какое поле Вы хотите изменить?
                        1) ФИО
                        2) Специализация
                        3) Номер кабинета
                        4) Рабочий режим
                        5) Рабочие дни
                        Для множественного выбора введите цифры через пробел""");

                HashSet<String> choiceSet = new HashSet<>(List.of(scanner.nextLine().split(" ")));

                for (String s : choiceSet) {
                    switch (s) {
                        case "1" -> {
                            System.out.println("Введите новое ФИО врача: ");
                            doctor.setFullName(scanner.nextLine());
                        }

                        case "2" -> {
                            System.out.println("Введите новую специализацию врача: ");
                            doctor.setSpecialization(scanner.nextLine());
                        }

                        case "3" -> {
                            System.out.println("Введите новый номер кабинета врача: ");
                            doctor.setCabinetNumber(scanner.nextLine());
                        }

                        case "4" -> {
                            System.out.println("Введите новый рабочий режим (24:60-24:60): ");
                            String workingRegime = scanner.nextLine();

                            if (DataHelper.workingRegimeIsValid(workingRegime)) {
                                doctor.setWorkingRegime(DataHelper.formatWorkingRegime(workingRegime));
                            }

                            else {
                                incorrectInput = true;
                                break loop;
                            }
                        }

                        case "5" -> {
                            System.out.println("""
                                Выберите новые рабочие дни врача (через пробел):
                                1) Понедельник
                                2) Вторник
                                3) Среда
                                4) Четверг
                                5) Пятница
                                6) Суббота
                                7) Воскресенье""");

                            String chosenDays = scanner.nextLine();

                            StringBuilder newDays = new StringBuilder();
                            for (String day : chosenDays.split(" ")) {
                                switch (day) {
                                    case "1" -> newDays.append("Mo ");
                                    case "2" -> newDays.append("Tu ");
                                    case "3" -> newDays.append("We ");
                                    case "4" -> newDays.append("Th ");
                                    case "5" -> newDays.append("Fr ");
                                    case "6" -> newDays.append("Sa ");
                                    case "7" -> newDays.append("Su ");
                                }
                            }

                            doctor.setWorkingDays(newDays.toString().trim());
                        }
                    }
                }
            }
        }

        if (isFound && !incorrectInput){
            FileWriter.rewriteFile(doctors, FilePath.doctors);
            System.out.println("Данные врача успешно изменены!");
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }

    // Метод №9 #Добавление
    private void addDoctor() {
        boolean incorrectInput;

        String[] csvData = new String[6];
        csvData[0] = String.valueOf(DataHelper.generateId(FilePath.doctors));

        System.out.println("Введите ФИО нового врача: ");
        csvData[1] = scanner.nextLine();

        System.out.println("Введите специализацию врача: ");
        csvData[2] = scanner.nextLine();

        System.out.println("Введите кабинет врача: ");
        csvData[3] = scanner.nextLine();

        System.out.println("Введите рабочий график врача (24:60-24:60): ");
        String workingRegime = scanner.nextLine();

        incorrectInput = !DataHelper.workingRegimeIsValid(workingRegime);
        if (!incorrectInput) {
            csvData[4] = DataHelper.formatWorkingRegime(workingRegime);

            System.out.println("""
                                Выберите новые рабочие дни врача (через пробел):
                                1) Понедельник
                                2) Вторник
                                3) Среда
                                4) Четверг
                                5) Пятница
                                6) Суббота
                                7) Воскресенье""");

            StringBuilder newDays = new StringBuilder();
            for (String day : scanner.nextLine().split(" ")) {
                switch (day) {
                    case "1" -> newDays.append("Mo ");
                    case "2" -> newDays.append("Tu ");
                    case "3" -> newDays.append("We ");
                    case "4" -> newDays.append("Th ");
                    case "5" -> newDays.append("Fr ");
                    case "6" -> newDays.append("Sa ");
                    case "7" -> newDays.append("Su ");
                }
            }

            csvData[5] = newDays.toString().trim();
        }

        if (!incorrectInput) {
            FileWriter.appendToFile(csvData, FilePath.doctors);
            System.out.println("Новый врач успешно добавлен!");
        }

        super.checkConditions(true, incorrectInput);
        super.backToMenu();
    }

    // Метод №10 #Удаление
    private void deleteDoctor() {
        boolean isFound = false;
        boolean incorrectInput = false;

        List<Doctor> doctors = FileHelper.getFileData(Doctor.class, FilePath.doctors);

        System.out.println("""
                Выберите способ удаления:
                1) Удаление по ID
                2) Множественное удаление""");

        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID врача: ");
                String id = scanner.nextLine();
                isFound = doctors.removeIf(doctor -> doctor.getDoctorID().equals(id));
            }

            case "2" -> {
                System.out.println(super.makeTableOf(
                        FileHelper.getFileData(Doctor.class, FilePath.doctors), FilePath.doctors) +
                        "\nВведите ID аккаунта(-ов), который(-ые) хотите удалить: ");

                HashSet<String> idForDeleting = new HashSet<>(List.of(scanner.nextLine().split(" ")));
                for (String id : idForDeleting) {
                    isFound = doctors.removeIf(doctor -> doctor.getDoctorID().equals(id));
                }
            }
        }

        if (isFound && !incorrectInput) {
            FileWriter.rewriteFile(doctors, FilePath.doctors);
            System.out.println("Доктор(-а) удалён(-ы)!");
        }

        super.checkConditions(isFound, incorrectInput);
        backToMenu();
    }
}
