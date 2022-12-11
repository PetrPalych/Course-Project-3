package utils;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileHelper {
    // Возвращает список, который хранит данные из файла в виде объектов
    public static <T> List<T> getFileData(Class<T> t, FilePath filePath) {
        try {
            return new CsvToBeanBuilder<T>(new FileReader(filePath.getFilePath()))
                    .withType(t)
                    .build()
                    .parse();
        }

        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Возвращает заголовок файла для построения таблицы
    public static String[] getHeader(FilePath filePath) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filePath.getFilePath()));
            String[] header = csvReader.readNext();
            csvReader.close();

            return header;
        }

        catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для создания пустой таблицы с заголовком.
    public static PrettyTable makeTable(FilePath filePath) {
        PrettyTable prettyTable = new PrettyTable();

        // Добавление заголовка таблицы
        prettyTable.addHeader(FileHelper.getHeader(filePath));

        return prettyTable;
    }
}