package com.company;

import com.company.interfaces.ICommunicationType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Telegram extends TelegramLongPollingBot implements ICommunicationType {
    private Bot bot = new Bot();
    private Update update;

    @Override
    public void onUpdateReceived(Update update) {
        this.update = update;
        String text = update.getMessage().getText().toLowerCase();
        getMsg(text);
        System.out.println(text);
    }

    private void sendMessage(String text) {
        Message msg = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername()
    {
        return "GameOJ_bot";
    }

    @Override
    public String getBotToken()
    {
        return "924346334:AAEUVoLozCIkgjMOv0N9M8b7ndd-aDQFf1A";

    }

    @Override
    public void getMsg(String update) {
        bot.communicate(update);
        sendMessage(bot.answer);
    }
}