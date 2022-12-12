package utils;

import models.implementation.appointments.Appointment;
import models.implementation.doctors.Doctor;
import models.implementation.employees.Employee;
import models.implementation.patients.Patient;
import models.implementation.payments.Payment;
import models.implementation.recipes.Recipe;
import models.implementation.treatments.Treatment;

public class ConsolePrinter {
    // Выводит информацию о сотруднике
    public static void printEmployee(Employee employee) {
        System.out.printf("""
                Username: %s
                Password: %s
                Account type: %s
                Doctor ID: %s%n
                """,
                employee.getUsername(), employee.getPassword(), employee.getAccountType(), employee.getDoctorID());
    }

    // Выводит информацию о враче
    public static void printDoctor(Doctor doctor) {
        System.out.printf("""
                ID: %s
                ФИО: %s
                Специализация: %s
                Кабинет: %s
                Режим работы: %s
                Рабочие дни: %s%n
                """,
                doctor.getDoctorID(), doctor.getFullName(), doctor.getSpecialization(), doctor.getCabinetNumber(), doctor.getWorkingRegime(), doctor.getWorkingDays());
    }

    // Выводит информацию о пациенте
    public static void printPatient(Patient patient) {
        System.out.printf("""
                ID: %s
                Full name: %s
                Sex: %s
                Date of birth: %s
                Region: %s
                City: %s
                Address: %s
                Phone number: %s
                Personal Number: %s%n
                """,
                patient.getPatientID(), patient.getFullName(), patient.getSex(), patient.getDateOfBirth(), patient.getRegion(), patient.getCity(), patient.getAddress(), patient.getPhoneNumber(), patient.getPersonalNumber());
    }

    // Выводит информацию о договоре
    public static void printAppointment(Appointment contract) {
        System.out.printf("""
                Appointment ID: %s
                Doctor ID: %s
                Doctor full nane: %s
                Cabinet: %s
                Reception time: %s
                Reception date: %s
                Patient ID: %s
                Patient full name: %s
                Service cost: %s
                Status: %s%n
                """,
                contract.getAppointmentID(), contract.getDoctorID(), contract.getDoctorFullName(), contract.getCabinetNumber(), contract.getReceptionTime(), contract.getReceptionDate(), contract.getCardFileID(), contract.getPatientFullName(), contract.getServiceCost(), contract.getStatus());
    }

    // Выводит информацию о назначенном лечении
    public static void printTreatment(Treatment treatment) {
        System.out.printf("""
                Treatment ID: %s
                Date: %s
                
                Doctor ID: %s
                Doctor full name: %s
                
                Card file ID: %s
                Patient full name: %s
                
                
                
                COMPLAINTS:
                %s
                PRELIMINARY DIAGNOSIS:
                %s
                SURVEY PLAN:
                %s
                SURVEY DATA:
                %s
                CLINICAL DIAGNOSIS:
                %s
                TREATMENT PLAN:
                %s
                TREATMENT PROGRESS:
                %s
                RECOMMENDATIONS:
                %s
                DIRECTED TO:
                %s
                """,
                treatment.getTreatmentID(), treatment.getDate(), treatment.getDoctorID(), treatment.getDoctorFullName(), treatment.getCardFileID(), treatment.getPatientFullName(), treatment.getComplaints().formatted(), treatment.getPreliminaryDiagnosis().formatted(), treatment.getSurveyPlan().formatted(), treatment.getSurveyData().formatted(), treatment.getClinicalDiagnosis().formatted(), treatment.getTreatmentPlan().formatted(), treatment.getTreatmentProgress().formatted(), treatment.getRecommendations().formatted(), treatment.getDirectedTo().formatted());
    }

    // Выводит информацию о выписанном рецепте
    public static void printRecipe(Recipe recipe) {
        System.out.printf("""
                RECIPE #%s
                
                Type of recipe: %s
                Date: %s
                
                Card file ID: %s
                Patient full name: %s
                
                Doctor ID: %s
                Doctor full name: %s
                
                Drug name:
                %s
                """,
                recipe.getRecipeID(), recipe.getTypeOfRecipe(), recipe.getDate(), recipe.getCardFileID(), recipe.getPatientFullName(), recipe.getDoctorID(), recipe.getDoctorFullName(), recipe.getDrugName().formatted());
    }

    // Выводит информацию о платеже
    public static void printPayment(Payment payment) {
        System.out.printf("""
                Payment ID: %s
                Patient ID: %s
                Patient full name: %s
                Total: %s
                Date: %s%n
                """,
                payment.getPaymentID(), payment.getCardFileId(), payment.getPatientFullName(), payment.getTotal(), payment.getDate());
    }

}