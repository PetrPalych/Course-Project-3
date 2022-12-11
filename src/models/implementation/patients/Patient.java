package models.implementation.patients;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Patient implements Convertible {
    @CsvBindByName(column = "Patient ID")
    private String patientID;

    @CsvBindByName(column = "Full name")
    private String fullName;

    @CsvBindByName(column = "Sex")
    private String sex;

    @CsvBindByName(column = "Date of birth")
    private String dateOfBirth;

    @CsvBindByName(column = "Region")
    private String region;

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "Address")
    private String address;

    @CsvBindByName(column = "Phone number")
    private String phoneNumber;

    @CsvBindByName(column = "Personal number")
    private String personalNumber;

    public String getPatientID() {
        return patientID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    @Override
    public String[] toArray() {
        return new String[] {patientID, fullName, sex, dateOfBirth, region, city, address, phoneNumber, personalNumber};
    }
}