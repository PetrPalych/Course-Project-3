package models.implementation.employees;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Employee implements Convertible {
    @CsvBindByName(column = "Username")
    private String username;

    @CsvBindByName(column = "Password")
    private String password;

    @CsvBindByName(column = "Account type")
    private String accountType;

    @CsvBindByName(column = "Doctor ID")
    private String doctorID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    @Override
    public String[] toArray() {
        return new String[] {username, password, accountType, doctorID};
    }
}
