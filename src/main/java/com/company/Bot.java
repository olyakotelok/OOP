package com.company;

import com.company.games.Goroda;
import com.company.games.MathGame;
import com.company.interfaces.IGame;


public class Bot
{
    private IGame game;
    private IGame[] games = new IGame[]{new Goroda(), new MathGame()};
    private MemoryGame memory = new MemoryGame();
    public String answer;
    private boolean sendWelcomeMsg = false;
    private Integer numberGame;

    public String getWelcomeMsg()
    {
        StringBuilder output = new StringBuilder();
        output.append("Выберите игру:\n");
        for (int i=0; i<games.length; i++)
        {
            output.append(games[i].getName() + "-" + i + "\n");
        }
        sendWelcomeMsg = true;
        return output.toString();
    }

    private boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }


    private Integer getGameNumber(String userChoice)
    {
        if (!(tryParseInt(userChoice) && (Integer.parseInt(userChoice) < games.length)))
        {
            answer = "Введите корректное значение";
            return null;
        }
        return Integer.parseInt(userChoice);
    }

    public void communicate(String msg)
    {
        String text = msg.toLowerCase();

        if (sendWelcomeMsg) //after sending welcome msg
        {
            numberGame = getGameNumber(text);
            if (numberGame != null)
            {
                IGame g = memory.getLastGame();

                if (g != null)
                {
                    if ((numberGame == 0 && g instanceof Goroda) || (numberGame == 1 && g instanceof MathGame))
                    {
                        game = g;
                        continiuePlay();
                    }
                }
                else {
                    game = games[numberGame];
                    startPlay();
                }

            }
            sendWelcomeMsg = false;
            return;
        }

        if ("/start".equals(text))
        {
            getWelcomeMsg();
            return;
        }

        if ("help".equals(text))
        {
            game.getHelp();
            return;
        }

        if ("новая".equals(text) || "сохранить".equals(text))
        {
            memory.saveLastGame(game);
            game = null;
            getWelcomeMsg();
            answer = "игра сохранена! \n" + answer;
            return;
        }
        if ("последняя".equals(text))
        {
            IGame gameNow = memory.getLastGame();
            memory.saveLastGame(game);
            game = gameNow;
            return;
        }
        if ("хватит".equals(text))
        {
            answer = "Чтобы сохранить игру, введи сохранить. Иначе введи не сохранять.";
            return;
        }

        if ("не сохранять".equals(text))
        {
            game = null;
            getWelcomeMsg();
            return;
        }
        progressInput(text);
    }

    private void startPlay()
    {
        StringBuilder output = new StringBuilder();
        output.append("Если захочешь сменить игру, скажи: новая\n");
        output.append("Если захочешь вернуться к последней игре: последняя\n");
        output.append(game.start());
        answer = output.toString();
    }

    private void progressInput(String text)
    {
        game.readMessage(text);
        String mess = game.getMessage();
        if (mess == null)
        {
            answer = "Не знаю... Попробуй написать что-то еще";
            return;
        }
        if (game.isFinished())
        {
            finish();
            return;
        }
        answer = mess;
    }

    private void finish()
    {
        if (game instanceof Goroda)
        {
            games[numberGame] = new Goroda();
        }
        else
        {
            games[numberGame] = new MathGame();
        }
        getWelcomeMsg();
        answer = "Игра окончена \n" + answer;
        if (memory.getLastGame().isFinished())
        {
            memory.saveLastGame(null);
        }
        game = null;
        return;
    }

    private void continiuePlay()
    {
        answer = game.getLastMessage();
    }
}
