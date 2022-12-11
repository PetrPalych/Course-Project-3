package utils;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
}