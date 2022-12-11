package utils;

public enum FilePath {
    authorization("src/models/implementation/employees/data.csv");



    final String filePath;

    FilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
