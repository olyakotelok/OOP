package com.company.games;

import com.company.Message;
import com.company.Task;
import com.company.interfaces.IGame;

import java.util.Random;


public class MathGame implements IGame {
    private Task task;
    private String lastMessage;


    @Override
    public boolean isFinished() {
        return false;
    }

    public String start() {
        task = GenerateTask();
        return "Привет, я пишу тебе задачку, а ты пишешь мне ответ.\n" +
                "Как надоест скажи: хватит, а напомить правила можно командой Помощь \n" + task.Question;
    }

    public String getName() {
        return "Математика";
    }

    private Task GenerateTask() {
        Random random = new Random();
        String[] operations = new String[4];
        operations[0] = "+";
        operations[1] = "-";
        operations[2] = "*";
        operations[3] = "/";
        int a = random.nextInt(50);
        int b = random.nextInt(50);
        int operation = random.nextInt(5);
        int answer = 0;
        String questText = "";
        switch (operation) {
            case (0): {
                answer = a + b;
                questText = a + operations[0] + b;
                break;
            }
            case (1): {
                answer = a - b;
                questText = a + operations[1] + b;
                break;
            }
            case (2): {
                answer = a * b;
                questText = a + operations[2] + b;
                break;
            }
            case (3): {
                answer = b;
                if (a == 0) a = 1;
                questText = a * b + operations[3] + a;
                break;
            }
            case (4): {
                answer = a + a * b - b * b;
                questText = a + operations[0] + a + operations[2] + b + operations[1] + b + operations[2] + b;
            }
        }

        return new Task(answer, questText);
    }

    @Override
    public Message answerMessage(String str) {
        if (str.equals(Integer.toString(task.Answer))) {
            task = GenerateTask();
            return save("Правильно! \n" + task.Question);
        }

        String s = "Нет, правильный ответ " + task.Answer + "\n";
        task = GenerateTask();
        String a = task.Question;
        return save(s + a);
    }

    private Message save(String message) {
        lastMessage = message;
        return new Message(message);
    }

    @Override
    public String getHelp() {
        return "Привет, я пишу тебе задачку, а ты пишешь мне ответ." +
                "Как надоест скажи: хватит, а напомить правила можно командой Помощь";
    }
}
