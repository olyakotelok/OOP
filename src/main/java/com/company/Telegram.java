package com.company;

import com.company.interfaces.ICommunicationType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Telegram extends TelegramLongPollingBot implements ICommunicationType {
    private HashMap<String, Bot> bots = new HashMap<String, Bot>();
    private Update update;

    @Override
    public void onUpdateReceived(Update update) {
        this.update = update;
        String text = update.getMessage().getText().toLowerCase();
        String id = update.getMessage().getChatId().toString();
        getMsg(text, id);
        System.out.println(text);
    }

    private void sendMessage(String text) {
        Message msg = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        //keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Города");
        keyboardFirstRow.add("Математика");
        replyKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardFirstRow));

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
    public String getBotUsername() {return System.getenv("BotUsername");}

    @Override
    public String getBotToken() {return System.getenv("BotToken");}

    @Override
    public void getMsg(String message, String id) {
        synchronized(bots) {
            if (!bots.containsKey(id)) {
                bots.put(id, new Bot());
            }
            Bot bot = bots.get(id);
            bot.communicate(message);
            sendMessage(bot.answer);
        }
    }
}