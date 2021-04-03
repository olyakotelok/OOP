package com.company;

import com.company.interfaces.ICommunicationType;

import java.util.Scanner;


public class Console implements ICommunicationType {
    private Scanner input = new Scanner(System.in);
    private Bot bot = new Bot();

    public Console() {
        start();
    }

    public void start() {
        sendMsg(bot.getWelcomeMsg());
        while (true) {
            getMsg(input.nextLine(), "console");
        }
    }

    public void getMsg(String update, String id) {
        sendMsg(bot.communicate(update));
    }

    private void sendMsg(String str) {
        System.out.println(str);
    }
}
