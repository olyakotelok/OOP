package com.company;

public interface IQuestionGame
{
    public String generateQuestion();
    public boolean checkAnswer(String ans);
    public String getRightAnswer();
}
