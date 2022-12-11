package models.implementation.doctors;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Doctor implements Convertible {
    @CsvBindByName(column = "Doctor ID")
    private String doctorID;

    @CsvBindByName(column = "Full name")
    private String fullName;

    @CsvBindByName(column = "Specialization")
    private String specialization;

    @CsvBindByName(column = "Cabinet number")
    private String cabinetNumber;

    @CsvBindByName(column = "Working regime")
    private String workingRegime;

    @CsvBindByName(column = "Working days")
    private String workingDays;

    public String getDoctorID() {
        return doctorID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public String getWorkingRegime() {
        return workingRegime;
    }

    public void setWorkingRegime(String workingRegime) {
        this.workingRegime = workingRegime;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    @Override
    public String[] toArray() {
        return new String[] {doctorID, fullName, specialization, cabinetNumber, workingRegime, workingDays};
    }
}
