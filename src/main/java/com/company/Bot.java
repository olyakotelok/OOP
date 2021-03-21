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
    public String answer;

    public String getWelcomeMsg() {
        StringBuilder output = new StringBuilder();
        output.append("Выберите игру:\n");
        for (int i = 0; i < games.length; i++) {
            output.append(games[i].getName() + "\n");
        }
        sendWelcomeMsg = true;
        answer = output.toString();
        return answer;
    }

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private Integer getGameNumber(String userChoice) {
        if (userChoice.equals("города")) {
            return 0;
        } else if (userChoice.equals("математика")) {
            return 1;
        } else {
            answer = "Введите корректное значение";
            return null;
        }
    }

    public boolean inGame() {
        return game != null;
    }

    public void communicate(String msg) {
        String text = msg.toLowerCase();

        if ("/help".equals(text)) {
            game.getHelp();
            return;
        }

        if ("/start".equals(text)) {
            game = null;
            getWelcomeMsg();
            return;
        }

        if ("последняя".equals(text)) {
            IGame gameNow = memory.getLastGame();
            if (gameNow != null) {
                memory.saveLastGame(game);
                game = gameNow;
                startPlay();
                return;
            }
        }

        if (game == null) //game didn't start
        {
            numberGame = getGameNumber(text);
            IGame g = memory.getLastGame();
            game = numberGame != null ? games[numberGame] : g;
            startPlay();
            return;
        }

        if ("сохранить".equals(text)) {
            memory.saveLastGame(game);
            game = null;
            getWelcomeMsg();
            answer = "игра сохранена! \n" + answer;
            return;
        }

        if ("новая".equals(text)) {
            game = null;
            getWelcomeMsg();
            return;
        }

        if ("хватит".equals(text)) {
            answer = "Чтобы сохранить игру, введи сохранить. Иначе введи не сохранять.";
            return;
        }

        if ("не сохранять".equals(text)) {
            game = null;
            getWelcomeMsg();
            return;
        }
        processInput(text);
    }

    private void startPlay() {
        StringBuilder output = new StringBuilder();
        output.append("Если захочешь сменить игру, скажи: новая\n");
        output.append("Если захочешь вернуться к последней игре: последняя\n");
        System.out.println("hh");
        output.append(game.start());
        answer = output.toString();
        System.out.println(answer);
    }

    private void processInput(String text) {
        String msg = game.answerMessage(text);
        if (msg == null) {
            answer = "Не знаю... Попробуй написать что-то еще";
            return;
        }
        if (game.isFinished()) {
            finish();
            return;
        }
        answer = msg;
    }

    private void finish() {
        if (game instanceof Goroda) {
            games[numberGame] = new Goroda();
        } else {
            games[numberGame] = new MathGame();
        }
        getWelcomeMsg();
        answer = "Игра окончена \n" + answer;
        //if (memory.getLastGame().isFinished())
        //{
        //    memory.saveLastGame(null);
        //}
        game = null;
        return;
    }
}
