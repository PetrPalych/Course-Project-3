package models.implementation.recipes;

import com.opencsv.bean.CsvBindByName;
import models.interfaces.Convertible;

public class Recipe implements Convertible {
    @CsvBindByName(column = "Recipe ID")
    private String recipeID;

    @CsvBindByName(column = "Date")
    private String date;

    @CsvBindByName(column = "Card file ID")
    private String cardFileID;

    @CsvBindByName(column = "Patient full name")
    private String patientFullName;

    @CsvBindByName(column = "Age")
    private String age;

    @CsvBindByName(column = "Doctor ID")
    private String doctorID;

    @CsvBindByName(column = "Doctor full name")
    private String doctorFullName;

    @CsvBindByName(column = "Type of recipe")
    private String typeOfRecipe;

    @CsvBindByName(column = "Drug name")
    private String drugName;

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getTypeOfRecipe() {
        return typeOfRecipe;
    }

    public void setTypeOfRecipe(String typeOfRecipe) {
        this.typeOfRecipe = typeOfRecipe;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String[] toShortArray() {
        return new String[] {recipeID, date, cardFileID, patientFullName, doctorID, doctorFullName};
    }

    @Override
    public String[] toArray() {
        return new String[] {recipeID, date, cardFileID, patientFullName, age, doctorID, doctorFullName, typeOfRecipe, drugName};
    }
}
