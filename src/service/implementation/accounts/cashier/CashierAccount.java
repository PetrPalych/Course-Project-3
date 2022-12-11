package service.implementation.accounts.cashier;

import models.implementation.appointments.Appointment;
import models.implementation.payments.Payment;
import models.interfaces.Convertible;
import service.implementation.accounts.Account;
import utils.ConsolePrinter;
import utils.FileHelper;
import utils.FilePath;
import utils.FileWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CashierAccount extends Account {
    // Главное меню кассира
    @Override
    public void showMenu() {
        System.out.println("""
                Для работы с программой введите номер функции.
                Список функций:
                1) Список всех неоплаченных приемов
                2) Подтвердить оплату за прием
                3) Список всех платежей
                4) Поиск платежа
                5) Выйти из программы""");

        switch (scanner.nextLine()) {
            case "1" -> showAllUnpaidAppointments();
            case "2" -> changeStatus();
            case "3" -> showPayments();
            case "4" -> findPayment();

            case "5" -> super.exit();

            default -> showMenu();
        }
    }



    // Метод №1 #Вывод
    private void showAllUnpaidAppointments() {
        System.out.println(
                super.makeTableOf(FileHelper.getFileData(Appointment.class, FilePath.appointments).stream().filter(
                        appointment -> appointment.getStatus().equals("Not Paid")).toList(), FilePath.appointments));

        super.backToMenu();
    }



    // Метод №2 #Изменение #Добавление
    private void changeStatus() {
        boolean isFound = false;
        boolean isChanged = false;

        List<Appointment> appointments = FileHelper.getFileData(Appointment.class, FilePath.appointments);
        String[] csvData = new String[5];

        System.out.println("Введите ID приема: ");
        String id = scanner.nextLine();

        for (Appointment appointment : appointments) {
            if (appointment.getContractID().equals(id) && !appointment.getStatus().equals("Paid")) {
                isFound = true;
                ConsolePrinter.printAppointment(appointment);

                System.out.println("Для того чтобы изменить статус на оплачено, введите \"+\"");
                if (scanner.nextLine().equals("+")) {
                    isChanged = true;
                    appointment.setStatus("Paid");

                    csvData[0] = appointment.getContractID();
                    csvData[1] = appointment.getCardFileID();
                    csvData[2] = appointment.getPatientFullName();
                    csvData[3] = appointment.getServiceCost();

                    LocalDateTime now = LocalDateTime.now();
                    csvData[4] = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                }

                else {
                    isChanged = false;
                }
            }
        }

        if (isChanged) {
            FileWriter.rewriteFile(appointments, FilePath.appointments);
            FileWriter.appendToFile(csvData, FilePath.payments);

            System.out.println("Платеж подтвержден!");
        }

        super.checkConditions(isFound, false);
        super.backToMenu();
    }



    // Метод №3 #Вывод
    private void showPayments() {
        System.out.println(super.makeTableOf(
                FileHelper.getFileData(Payment.class, FilePath.payments), FilePath.payments));

        super.backToMenu();
    }



    // Метод №4 #Поиск
    private void findPayment() {
        boolean isFound = false;

        System.out.println("""
                Выберите по какому критерию искать платеж:
                1) ID платежа
                2) ФИО пациента
                3) Дата платежа""");

        List<? extends Convertible> relevantPayments = null;
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("Введите ID платежа: ");
                String id = scanner.nextLine();
                relevantPayments = super.findInList(
                        FileHelper.getFileData(Payment.class, FilePath.payments), id, "getId");
            }

            case "2" -> {
                System.out.println("Введите ФИО пациента: ");
                String fullName = scanner.nextLine();
                relevantPayments = FileHelper.getFileData(Payment.class, FilePath.payments).stream().filter(
                        payment -> payment.getPatientFullName().contains(fullName)).toList();
            }

            case "3" -> {
                System.out.println("Введите дату платежа: ");
                String date = scanner.nextLine();
                relevantPayments = super.findInList(
                        FileHelper.getFileData(Payment.class, FilePath.payments), date, "getDate");
            }
        }

        if (relevantPayments != null && !relevantPayments.isEmpty()) {
            isFound = true;
            System.out.println(super.makeTableOf(relevantPayments, FilePath.payments));
        }

        super.checkConditions(isFound, false);
        super.backToMenu();
    }
}