package utils;

public enum FilePath {
    authorization("src/models/implementation/employees/data.csv"),
    doctors("src/models/implementation/doctors/data.csv");



    final String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
