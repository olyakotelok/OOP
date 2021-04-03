package com.company;

import com.company.interfaces.ICommunicationType;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Telegram extends TelegramLongPollingBot implements ICommunicationType {
    private HashMap<String, Bot> bots = new HashMap<String, Bot>();
    private Update update;
    private Dotenv dotenv = Dotenv.load();

    @Override
    public void onUpdateReceived(Update update) {
        this.update = update;
        String text = update.getMessage().getText();
        String id = update.getMessage().getChatId().toString();
        System.out.println(text);
        if (text != null) {
            getMsg(text, id);
            System.out.println(text);
        } else {
            sendMessage("Введите корректное значение", false);
        }
    }

    private void sendMessage(String text, Boolean inGame) {
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        if (inGame) {
            keyboardFirstRow.add("Закончить");
            keyboardFirstRow.add("Сохранить");
            keyboardFirstRow.add("/help");
        } else {
            keyboardFirstRow.add("Города");
            keyboardFirstRow.add("Математика");
        }
        List<KeyboardRow> keyboard = Collections.singletonList(keyboardFirstRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .keyboard(keyboard)
                .build();

        String chatId = Long.toString(update.getMessage().getChatId());
        SendMessage sendMessage = SendMessage.
                builder()
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .chatId(chatId)
                .text(text)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return dotenv.get("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return dotenv.get("BOT_TOKEN");
    }

    @Override
    public void getMsg(String message, String id) {
        synchronized (bots) {
            if (!bots.containsKey(id)) {
                bots.put(id, new Bot());
            }
            Bot bot = bots.get(id);
            sendMessage(bot.communicate(message), bot.inGame());
        }
    }

    @Override
    public void start() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }
}