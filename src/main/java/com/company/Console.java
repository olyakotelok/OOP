package com.company;

import com.company.interfaces.ICommunicationType;
import java.util.Scanner;


public class Console implements ICommunicationType {
    private Scanner input = new Scanner(System.in);
    private Bot bot = new Bot();
    public Console()
    {
        start();
    }

    private void start() {
        bot.sendWelcomeMsg();
        sendMsg(bot.getAnswer());
        while (true)
        {
            getMsg(input.nextLine());
        }
    }

    public void getMsg(String update)
    {
        bot.communicate(update);
        sendMsg(bot.getAnswer());
    }

    private void sendMsg(String str)
    {
        System.out.println(str);
    }
}
