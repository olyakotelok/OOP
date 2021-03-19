package com.company;

import com.company.interfaces.IGame;

public class MemoryGame {
    private IGame lastGame;

    public void saveLastGame(IGame game) {
        lastGame = game;
    }

    public IGame getLastGame() {
        return lastGame;
    }
}
