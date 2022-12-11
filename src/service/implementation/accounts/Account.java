package service.implementation.accounts;

import models.interfaces.Convertible;
import utils.FileHelper;
import utils.FilePath;
import utils.PrettyTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Account {
    // У каждого аккаунта свой собственный сканер.
    protected Scanner scanner;

    // Возвращает сканер
    public Scanner getScanner() {
        return scanner;
    }

    // Метод для установки сканера.
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    // Абстрактный метод, который выводит меню аккаунта в консоль.
    public abstract void showMenu() throws Exception;

    // Метод для проверки булевых значений после выполнения метода.
    public void checkConditions(boolean isFound, boolean incorrectInput) {
        if (!isFound) {
            System.out.println("Не было найдено ничего, соответствующего Вашему запросу!");
        }

        else if (incorrectInput) {
            System.out.println("Ошибка ввода! Попробуйте снова.");
        }
    }

    // Метод для возвращения в главное меню (одинаково для всех аккаунтов).
    public void backToMenu() {
        while (true) {
            System.out.println("Чтобы вернуться в меню, нажмите Enter");
            if (scanner.nextLine().equals("")) {
                try {
                    showMenu();
                }

                catch (Exception e) {
                    throw new RuntimeException(e);
                }

                break;
            }
        }
    }

    // Метод для создания готовой заполненной таблицы.
    public PrettyTable makeTableOf(List<? extends Convertible> list, FilePath filePath) {
        PrettyTable prettyTable = FileHelper.makeTable(filePath);

        // Заполнение
        list.forEach(convertible -> prettyTable.addRow(convertible.toArray()));

        return prettyTable;
    }

    /*
     Метод для поиска определенного объекта в списке.
     List<?> list - список в котором мы ищем.
     String data - строка, которая содержится в поле.
     String methodName - метод, с помощью которого можно получить искомую строку из поля.
    */
    public List<Convertible> findInList(List<?> list, String data, String methodName) {
        List<Convertible> relevantObjects = new ArrayList<>();

        if (!list.isEmpty()) {
            try {
                Method method = list.get(0).getClass().getMethod(methodName);

                for (Object object : list) {
                    if (method.invoke(object).equals(data)) {
                        relevantObjects.add((Convertible) object);
                    }
                }
            }

            catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return relevantObjects;
    }

    // Выход из программы.
    protected void exit() {
        System.out.println("Вы вышли из программы! Мы будем рады Вашему возвращению!");
        scanner.close();
    }
}
