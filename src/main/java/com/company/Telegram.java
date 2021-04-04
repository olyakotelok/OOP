package com.company;

import com.company.interfaces.ICommunicationType;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.company.WikiApi;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Telegram extends TelegramLongPollingBot implements ICommunicationType {
    private final HashMap<String, Bot> bots = new HashMap<>();
    private Update update;
    private final Dotenv dotenv = Dotenv.load();

    @Override
    public void onUpdateReceived(Update update) {
        this.update = update;
        var tgMsg = update.getMessage();
        Message message = new Message(tgMsg.getText());
        String id = update.getMessage().getChatId().toString();
        if (message.Text != null) {
            getMsg(message, id);
            System.out.println(message.Text);
        } else {
            sendMessage("Введите корректное значение", false);
        }
    }

    private void sendMessage(String text, Boolean inGame) {
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        if (inGame) {
            keyboardFirstRow.add("Хватит");
            keyboardFirstRow.add("Правила");
            if (text.equals("Чтобы сохранить игру, введи сохранить. Иначе введи не сохранять.")) {
                keyboardFirstRow.remove("Хватит");
                keyboardFirstRow.remove("Правила");
                keyboardFirstRow.add("Сохранить");
                keyboardFirstRow.add("Не сохранять");
            }
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

    private void sendPhoto(Message message){
        String chatId = Long.toString(update.getMessage().getChatId());
        SendPhoto sendPhoto = SendPhoto.
                builder()
                .parseMode(ParseMode.HTML)
                .chatId(chatId)
                .photo(new InputFile(message.Image))
                .caption(message.Text)
                .build();
        try {
            execute(sendPhoto);
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
    public void getMsg(Message message, String id) {
        synchronized (bots) {
            if (!bots.containsKey(id)) {
                bots.put(id, new Bot());
            }
            Bot bot = bots.get(id);
            var resMsg = bot.communicate(message.Text);
            if (resMsg.Image == null) {
                sendMessage(resMsg.Text, bot.inGame());
            }
            else {
                sendPhoto(resMsg);
            }
        }
    }

    @Override
    public void start() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }
}