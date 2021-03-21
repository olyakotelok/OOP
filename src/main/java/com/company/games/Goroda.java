package com.company.games;

import com.company.interfaces.IGame;

import java.util.HashSet;


public class Goroda implements IGame {
    private String[] cities = new String[]
            {
                    "Москва",
                    "Анадырь",
                    "Ростов",
                    "Волгоград",
                    "Донецк"
            };
    private HashSet<String> usedCities = new HashSet<>();
    private String currentCity = "Амстердам";
    private String lastLetter = "м";
    private Boolean finished = false;
    private String lastMessage;

    public String start() {
        return (currentCity == "Амстердам" ? "Начнем" : "Продолжим") + " играть в города!\n" +
                "Я называю город, ты называешь город на последнюю букву моего и так далее...\n" +
                "Чтобы закончить, введи: хватит\n" +
                currentCity;

    }

    public String getName() {
        return "Города";
    }

    @Override
    public String answerMessage(String str) {
        str = str.toLowerCase();

        if (!str.substring(0, 1).equals(lastLetter))
            return save("Нужен город на букву " + lastLetter);

        if (!inBase(str))
            return save("Я не знаю такого города");

        if (usedCities.contains(str))
            return save("Этот город уже был");

        updateCurrentCity(str);
        String city = find(lastLetter);
        if (city != null) {
            updateCurrentCity(city);
            return save(city);
        }

        finished = true;
        return "Я не знаю подходящего города, игра окончена!";
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    private boolean inBase(String value) {
        for (String s : cities) {
            if (s.toLowerCase().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private String find(String value) {
        for (String city : cities) {
            if (city.toLowerCase().startsWith(value) && !usedCities.contains(city)) {
                return city;
            }
        }
        return null;
    }

    private void updateCurrentCity(String city) {
        usedCities.add(city);
        currentCity = city;
        int lastIndex = currentCity.length();
        lastLetter = currentCity.substring(lastIndex - 1);
        if (lastLetter.equals("ь") || lastLetter.equals("ы")) {
            lastLetter = currentCity.substring(lastIndex - 2, lastIndex - 1);
        }
    }

    private String save(String message) {
        lastMessage = message;
        return message;
    }

    @Override
    public String getHelp() {
        return "Я называю город, ты называешь город на последнюю букву моего и так далее...\n" +
                "Чтобы закончить, введи: хватит\nНапомнить правила можно командной \\help";
    }
}
