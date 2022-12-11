package utils;

public enum FilePath {
    authorization("src/models/implementation/employees/data.csv"),
    doctors("src/models/implementation/doctors/data.csv"),
    patients("src/models/implementation/patients/data.csv"),
    appointments("src/models/implementation/appointments/data.csv"),
    treatments("src/models/implementation/treatments/data.csv"),
    recipes("src/models/implementation/recipes/data.csv"),
    payments("src/models/implementation/payments/data.csv");



    final String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
