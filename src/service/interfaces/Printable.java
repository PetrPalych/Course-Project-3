package service.interfaces;

import models.implementation.appointments.Appointment;
import models.implementation.doctors.Doctor;
import models.implementation.patients.Patient;
import service.implementation.accounts.Account;
import utils.FileHelper;
import utils.FilePath;

public interface Printable {
    default void showListOfDoctors(Account account) {
        System.out.println(account.makeTableOf(
                FileHelper.getFileData(Doctor.class, FilePath.doctors), FilePath.doctors));
    }

    default void showCardFile(Account account) {
        System.out.println(account.makeTableOf(
                FileHelper.getFileData(Patient.class, FilePath.patients), FilePath.patients));

        account.backToMenu();
    }

    default void showListOfAppointments(Account account) {
        System.out.println(account.makeTableOf(
                FileHelper.getFileData(Appointment.class, FilePath.appointments), FilePath.appointments));

        account.backToMenu();
    }
}
