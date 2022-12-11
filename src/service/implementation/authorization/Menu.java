package service.implementation.authorization;

import service.implementation.accounts.chief.ChiefAccount;
import service.implementation.accounts.registrar.RegistrarAccount;
import utils.DataHelper;

import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    // Меню входа
    public static void show() {
        int attempts = 3;

        loop:
        while (true) {
            System.out.println( "Добро пожаловать!\n" + "Для авторизации введите логин своей учетной записи: ");
            String username = scanner.nextLine();

            System.out.println("Введите свой пароль: ");
            String password = scanner.nextLine();

            String[] checked = DataHelper.checkSignInData(username, password);
            switch (checked[0]) {
                case "Registrar" -> {
                    RegistrarAccount account = new RegistrarAccount();
                    account.setScanner(new Scanner(System.in));
                    account.showMenu();
                    scanner.close();
                    break loop;
                }

                case "Cashier" -> {
//                    CashierAccount account = new CashierAccount();
//                    account.setScanner(new Scanner(System.in));
//                    account.showMenu();
                    scanner.close();
                    break loop;
                }

                case "Doctor" -> {
//                    DoctorAccount account = new DoctorAccount(checked[1]);
//                    account.setScanner(new Scanner(System.in));
//                    account.showMenu();
                    scanner.close();
                    break loop;
                }

                case "Chief doctor" -> {
                    ChiefAccount account = new ChiefAccount();
                    account.setScanner(new Scanner(System.in));
                    account.showMenu();
                    scanner.close();
                    break loop;
                }

                case "Error" -> {
                    attempts--;

                    if (attempts < 0) {
                        System.out.println("""
                                Логин или пароль учетной записи был неправильно введен более 3 раз.
                                Если вы забыли свой логин или пароль обратитесь к руководителю!""");

                        break loop;
                    }

                    else {
                        System.out.println("Неправильно введен логин или пароль учетной записи!");
                    }
                }
            }
        }
    }
}