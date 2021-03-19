package com.company;


import com.company.interfaces.ICommunicationType;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("Введите t чтобы запустить бота в Telegram, любой другой ответ - бот в консоли");
       // Scanner input = new Scanner(System.in);
       // String type = input.nextLine();
        //if ("t".equals(type))
            start(new Telegram());
        //else
         //   start(new Console());
    }

    private static void start(ICommunicationType communicationType)
    {
        if (communicationType instanceof Telegram)
        {
            Telegram telegram = (Telegram)communicationType;
            try
            {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(telegram);
            }
            catch (TelegramApiException e)
            {
                System.out.println("Не удалось подключиться к Telegram. Поговорим в консоли");
                start(new Console());
            }
        }
    }
}
