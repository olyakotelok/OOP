package com.company;

import com.company.games.Goroda;
import com.company.games.MathGame;
import com.company.interfaces.IGame;


public class Bot {
    private IGame game;
    private IGame[] games = new IGame[]{new Goroda(), new MathGame()};
    private MemoryGame memory = new MemoryGame();
    private boolean sendWelcomeMsg = false;
    private Integer numberGame;

    public String getWelcomeMsg() {
        StringBuilder output = new StringBuilder();
        output.append("Выберите игру:\n");
        for (int i = 0; i < games.length; i++) {
            output.append(games[i].getName() + "\n");
        }
        sendWelcomeMsg = true;
        return output.toString();
    }

    private Integer getGameNumber(String userChoice) {
        if (userChoice.equals("города")) {
            return 0;
        } else if (userChoice.equals("математика")) {
            return 1;
        } else {
            return null;
        }
    }

    public boolean inGame() {
        return game != null;
    }

    public Message communicate(String msg) {
        String text = msg.toLowerCase();
        String result = null;

        if ("/help".equals(text) || "правила".equals(text)) {
            result = game.getHelp();
        }

        else if ("/start".equals(text)) {
            game = null;
            result = getWelcomeMsg();
        }

        else if ("последняя".equals(text)) {
            IGame gameNow = memory.getLastGame();
            if (gameNow != null) {
                memory.saveLastGame(game);
                game = gameNow;
                result = startPlay();
            }
        }

        else if (game == null) //game didn't start
        {
            numberGame = getGameNumber(text);
            if (numberGame == null) result = "Введите корректное значение";
            if (result == null) {
                IGame g = memory.getLastGame();
                game = numberGame != null ? games[numberGame] : g;
                result = startPlay();
            }
        }

        else if ("сохранить".equals(text)) {
            memory.saveLastGame(game);
            game = null;
            result = "игра сохранена! \n" + getWelcomeMsg();
        }

        else if ("новая".equals(text)) {
            game = null;
            result = getWelcomeMsg();
        }

        else if ("хватит".equals(text)) {
            result = "Чтобы сохранить игру, введи сохранить. Иначе введи не сохранять.";
        }

        else if ("не сохранять".equals(text)) {
            game = null;
            result = getWelcomeMsg();
        }

        if (result != null) {
            return new Message(result);
        }
        return processInput(text);
    }

    private String startPlay() {
        StringBuilder output = new StringBuilder();
        output.append("Если захочешь сменить игру, скажи: новая\n");
        output.append("Если захочешь вернуться к последней игре: последняя\n");
        output.append(game.start());
        String answer = output.toString();
        return answer;
    }

    private Message processInput(String text) {
        var msg = game.answerMessage(text);
        String result = null;
        if (msg.Text == null) {
            result = "Не знаю... Попробуй написать что-то еще";
        }
        else if (game.isFinished()) {
            result = finish();
        }
        if (result != null) {
            return new Message(result);
        }
        else {
            return msg;
        }
    }

    private String finish() {
        if (game instanceof Goroda) {
            games[numberGame] = new Goroda();
        } else {
            games[numberGame] = new MathGame();
        }
        game = null;
        return "Игра окончена \n" + getWelcomeMsg();
        //if (memory.getLastGame().isFinished())
        //{
        //    memory.saveLastGame(null);
        //}
    }
}