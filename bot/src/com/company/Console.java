package com.company;

import java.util.Scanner;

public class Console
{
    private Scanner input = new Scanner(System.in);
    private Bot bot;
    public Console()
    {
        start();
    }

    private void start()
    {

        System.out.println("Hello! There will be information about me. \n\n What topic is interesting for you?");
        System.out.println("1. History \n2. Physics");
        System.out.print("Enter number: ");


        bot = new Bot();
        while (true)
        {
            getMessage(input.nextLine());
        }
    }

    public void getMessage(String update)
    {
        bot.communicate(update);
        sendMessage(bot.getAnswer());
    }

    private void sendMessage(String str)
    {
        System.out.println(str);
    }
}

