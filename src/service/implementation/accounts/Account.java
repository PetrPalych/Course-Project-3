package service.implementation.accounts;

import java.util.Scanner;

public abstract class Account {
    // У каждого аккаунта свой собственный сканер.
    protected Scanner scanner;

    // Метод для установки сканера.
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    // Абстрактный метод, который выводит меню аккаунта в консоль.
    public abstract void showMenu() throws Exception;

}
