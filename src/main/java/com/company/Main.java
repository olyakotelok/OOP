package com.company;

import com.company.interfaces.ICommunicationType;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите t чтобы запустить бота в Telegram, любой другой ответ - бот в консоли");
        Scanner input = new Scanner(System.in);
        String type = input.nextLine();
        ICommunicationType communicationType =
                "t".equals(type)
                        ? new Telegram()
                        : new Console();

        try {
            communicationType.start();
        } catch (Exception e) {
            System.out.println("Не удалось запустить бота в выбранной среде, потому что вот что:");
            e.printStackTrace();
        }
    }
}
