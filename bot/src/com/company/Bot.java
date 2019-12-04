package com.company;

public class Bot
{
    private String answer;
    private boolean isStart = true;
    private boolean waitNumber = false;
    private boolean waitText = false;
    private IQuestionGame game;

    public Bot()
    {
        start();
    }


    public String getAnswer()
    {
        return answer;
    }

    public void communicate(String message)
    {
        var input = message.toLowerCase();
        if (isStart)
        {
            start();
        }

        else if (waitNumber)
        {
            waitNumber = false;
            if (input.equals("1"))
            {
                var s1 = new StringBuilder();
                s1.append("You chose History! \n");
                var h = new History();
                s1.append(h.generateQuestion()); // любой вопрос из набора
                answer = s1.toString();
            }
            else if (input.equals("2"))
            {
                answer = "Not exist now";
                /*
                var s1 = new StringBuilder();
                s1.append("You chose Physics! \n");
                s1.append(Physics.questions[0].text); // любой вопрос из набора
                answer = s1.toString();

                 */
            }
            else
            {
                waitNumber = true;
                answer = "Input number from 1 to 2";
            }
        }

        else
        {
            var check = game.checkAnswer(input);
            if (check)
            {
                var s1 = game.generateQuestion();
                answer = "Right! "+s1;
            }
            else
            {
                answer = "Not right!! Right answer is "+game.getRightAnswer();
            }

        }
    }

    private void start()
    {
        StringBuilder str = new StringBuilder();
        String s1 = "Hello! Let's play! I can tell you some facts about some topics. Then I ask you about them and check your memory.";
        String s2 = "\n Choose interesting topic, input number: \n 1.History \n 2.Physics";
        str.append(s1);
        str.append(s2);
        isStart = false;
        waitNumber = true;
        answer = str.toString();
    }
}
