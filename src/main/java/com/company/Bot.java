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
            return null;
        }
    }

    public boolean inGame() {
        return game != null;
    }

    public String communicate(String msg) {
        String text = msg.toLowerCase();

        if ("/help".equals(text)) {
            return game.getHelp();
        }

        if ("/start".equals(text)) {
            game = null;
            return getWelcomeMsg();
        }

        if ("последняя".equals(text)) {
            IGame gameNow = memory.getLastGame();
            if (gameNow != null) {
                memory.saveLastGame(game);
                game = gameNow;
                return startPlay();
            }
        }

        if (game == null) //game didn't start
        {
            numberGame = getGameNumber(text);
            if (numberGame == null) {
                return "Введите корректное значение";
            }
            IGame g = memory.getLastGame();
            game = numberGame != null ? games[numberGame] : g;
            return startPlay();
        }

        if ("сохранить".equals(text)) {
            memory.saveLastGame(game);
            game = null;
            return "игра сохранена! \n" + getWelcomeMsg();
        }

        if ("новая".equals(text)) {
            game = null;
            return getWelcomeMsg();
        }

        if ("хватит".equals(text)) {
            return "Чтобы сохранить игру, введи сохранить. Иначе введи не сохранять.";
        }

        if ("не сохранять".equals(text)) {
            game = null;
            return getWelcomeMsg();
        }
        return processInput(text);
    }

    private String startPlay() {
        StringBuilder output = new StringBuilder();
        output.append("Если захочешь сменить игру, скажи: новая\n");
        output.append("Если захочешь вернуться к последней игре: последняя\n");
        System.out.println("hh");
        output.append(game.start());
        String answer = output.toString();
        System.out.println(answer);
        return answer;
    }

    private String processInput(String text) {
        String msg = game.answerMessage(text);
        if (msg == null) {
            return "Не знаю... Попробуй написать что-то еще";
        }
        if (game.isFinished()) {
            return finish();
        }
        return msg;
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
