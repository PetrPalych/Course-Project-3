package utils;

import com.opencsv.CSVWriter;
import models.interfaces.Convertible;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileWriter {
    // Метод для перезаписи файла
    public static void rewriteFile(List<? extends Convertible> list, FilePath filePath) {
        List<String[]> csvData = new ArrayList<>();
        csvData.add(FileHelper.getHeader(filePath));
        list.forEach(convertible -> csvData.add(convertible.toArray()));

        try {
            CSVWriter csvWriter = new CSVWriter(new java.io.FileWriter(filePath.getFilePath()));
            csvWriter.writeAll(csvData);
            csvWriter.close();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для добавления строки в файл
    public static void appendToFile(String[] csvData, FilePath filePath) {
        try {
            CSVWriter csvWriter = new CSVWriter(new java.io.FileWriter(filePath.getFilePath(), true));
            csvWriter.writeNext(csvData);
            csvWriter.close();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
