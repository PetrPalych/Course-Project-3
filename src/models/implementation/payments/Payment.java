package models.implementation.payments;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Payment implements Convertible {
    @CsvBindByName(column = "Payment ID")
    private String paymentID;

    @CsvBindByName(column = "Card file ID")
    private String cardFileId;

    @CsvBindByName(column = "Patient full name")
    private String patientFullName;

    @CsvBindByName(column = "Total")
    private String total;

    @CsvBindByName(column = "Date")
    private String date;

    public String getPaymentID() {
        return paymentID;
    }

    public String getCardFileId() {
        return cardFileId;
    }

    public void setCardFileId(String cardFileId) {
        this.cardFileId = cardFileId;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String[] toArray() {
        return new String[] {paymentID, cardFileId, patientFullName, total, date};
    }
}
