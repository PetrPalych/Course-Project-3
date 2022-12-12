package models.implementation.appointments;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Appointment implements Convertible {
    @CsvBindByName(column = "Appointment ID")
    private String appointmentID;

    @CsvBindByName(column = "Doctor ID")
    private String doctorID;

    @CsvBindByName(column = "Doctor full name")
    private String doctorFullName;

    @CsvBindByName(column = "Cabinet number")
    private String cabinetNumber;

    @CsvBindByName(column = "Reception time")
    private String receptionTime;

    @CsvBindByName(column = "Reception date")
    private String receptionDate;

    @CsvBindByName(column = "Patient card file ID")
    private String cardFileID;

    @CsvBindByName(column = "Patient full name")
    private String patientFullName;

    @CsvBindByName(column = "Service cost")
    private String serviceCost ;

    @CsvBindByName(column = "Status")
    private String status;

    public String getAppointmentID() {
        return appointmentID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorFullName() {
        return doctorFullName;
    }

    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public String getReceptionTime() {
        return receptionTime;
    }

    public void setReceptionTime(String receptionTime) {
        this.receptionTime = receptionTime;
    }

    public String getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(String receptionDate) {
        this.receptionDate = receptionDate;
    }

    public String getCardFileID() {
        return cardFileID;
    }

    public void setCardFileID(String cardFileID) {
        this.cardFileID = cardFileID;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String[] toArray() {
        return new String[] {appointmentID, doctorID, doctorFullName, cabinetNumber, receptionTime, receptionDate, cardFileID, patientFullName, serviceCost, status};
    }
}
