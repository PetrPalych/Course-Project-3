package models.implementation.treatments;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Treatment implements Convertible {
    @CsvBindByName(column = "Treatment ID")
    private String treatmentID;

    @CsvBindByName(column = "Date")
    private String date;

    @CsvBindByName(column = "Doctor ID")
    private String doctorID;

    @CsvBindByName(column = "Doctor full name")
    private String doctorFullName;

    @CsvBindByName(column = "Card file ID")
    private String cardFileID;

    @CsvBindByName(column = "Patient full name")
    private String patientFullName;

    @CsvBindByName(column = "Complaints")
    private String complaints;

    @CsvBindByName(column = "Preliminary diagnosis")
    private String preliminaryDiagnosis;

    @CsvBindByName(column = "Survey plan")
    private String surveyPlan;

    @CsvBindByName(column = "Survey data")
    private String surveyData;

    @CsvBindByName(column = "Clinical diagnosis")
    private String clinicalDiagnosis;

    @CsvBindByName(column = "Treatment plan")
    private String treatmentPlan;

    @CsvBindByName(column = "Treatment progress")
    private String treatmentProgress;

    @CsvBindByName(column = "Recommendations")
    private String recommendations;

    @CsvBindByName(column = "Directed to")
    private String directedTo;

    public String getTreatmentID() {
        return treatmentID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDoctorFullName() {
        return doctorFullName;
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

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getPreliminaryDiagnosis() {
        return preliminaryDiagnosis;
    }

    public void setPreliminaryDiagnosis(String preliminaryDiagnosis) {
        this.preliminaryDiagnosis = preliminaryDiagnosis;
    }

    public String getSurveyPlan() {
        return surveyPlan;
    }

    public void setSurveyPlan(String surveyPlan) {
        this.surveyPlan = surveyPlan;
    }

    public String getSurveyData() {
        return surveyData;
    }

    public void setSurveyData(String surveyData) {
        this.surveyData = surveyData;
    }

    public String getClinicalDiagnosis() {
        return clinicalDiagnosis;
    }

    public void setClinicalDiagnosis(String clinicalDiagnosis) {
        this.clinicalDiagnosis = clinicalDiagnosis;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getTreatmentProgress() {
        return treatmentProgress;
    }

    public void setTreatmentProgress(String treatmentProgress) {
        this.treatmentProgress = treatmentProgress;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getDirectedTo() {
        return directedTo;
    }

    public void setDirectedTo(String directedTo) {
        this.directedTo = directedTo;
    }

    public String[] toShortArray() {
        return new String[] {treatmentID, date, doctorID, doctorFullName, cardFileID, patientFullName};
    }

    @Override
    public String[] toArray() {
        return new String[] {treatmentID, date, doctorID, doctorFullName, cardFileID, patientFullName, complaints, preliminaryDiagnosis, surveyPlan, surveyData, clinicalDiagnosis, treatmentPlan, treatmentProgress, recommendations, directedTo};
    }
}
