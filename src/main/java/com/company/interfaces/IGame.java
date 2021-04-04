package com.company.interfaces;

import com.company.Message;

public interface IGame {
    String start();

    Message answerMessage(String str);

    String getName();

    boolean isFinished();

    String getHelp();
}
