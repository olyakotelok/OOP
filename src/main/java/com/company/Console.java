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
            Message message = new Message(input.nextLine(), null);
            getMsg(message, "console");
        }
    }

    public void getMsg(Message update, String id) {
        sendMsg(bot.communicate(update.Text).Text);
    }

    private void sendMsg(String str) {
        System.out.println(str);
    }
}
