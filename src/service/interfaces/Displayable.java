package service.interfaces;

import models.implementation.doctors.Doctor;
import service.implementation.accounts.Account;
import utils.FileHelper;
import utils.FilePath;

public interface Displayable {
    default void showListOfDoctors(Account account) {
        System.out.println(account.makeTableOf(
                FileHelper.getFileData(Doctor.class, FilePath.doctors), FilePath.doctors));
    }
}
