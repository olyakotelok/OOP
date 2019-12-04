package com.company;

import java.util.Random;

public class History implements IQuestionGame
{
    private Question[] questions = new Question[2];
    private int lastQuestion;
    private Random random = new Random();

    public History()
    {
        questions[0] = new Question("What is the year of ending II World War ?", "1945");
        questions[1] = new Question("Who attacked Russian Empire in 1812 ?", "Napoleon");
    }

    public boolean checkAnswer(String ans)
    {
        if (questions[lastQuestion].Answer.equals(ans))
            return true;
        return false;
    }

    public String generateQuestion()
    {
        //lastQuestion = random.nextInt(questions.length - 1);
        lastQuestion = 0;
        return questions[lastQuestion].Text;
    }

    public String getRightAnswer()
    {
        return questions[lastQuestion].Answer;
    }
}
