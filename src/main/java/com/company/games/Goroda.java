package com.company.games;

import com.company.interfaces.IGame;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


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
    private String err;
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
    public void readMessage(String str) {
        str = str.toLowerCase();

        if (!str.substring(0, 1).equals(lastLetter)) {
            err = "Нужен город на букву " + lastLetter;
            return;
        }

        if (!inBase(str)) {
            err = "Я не знаю такого города";
            return;
        }
        if (usedCities.contains(str)) {
            err = "Этот город уже был";
            return;
        }
        updateCurrentCity(str);
        err = null;
    }

    @Override
    public String getMessage() {
        if (err != null) {
            lastMessage = err;
            return err;
        }

        String city = find(lastLetter);
        if (city != null) {
            updateCurrentCity(city);
            lastMessage = city;
            return city;
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

    @Override
    public String getHelp() {
        return "Я называю город, ты называешь город на последнюю букву моего и так далее...\n" +
                "Чтобы закончить, введи: хватит\nНапомнить правила можно командной \\help";
    }

    public Map<String, String> getWikiInfo(String city) throws Exception {
        Map<String, String> wikiInfo = new HashMap<String, String>();
        String url = "https://ru.wikipedia.org/w/api.php?format=json&action=query&prop=pageimages%7Cextracts&exintro&explaintext&images&pithumbsize=300&titles=" + city;
        URL wikiApi = new URL(url);

        BufferedReader in = new BufferedReader(new InputStreamReader(wikiApi.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine); //Можно   накапливать в StringBuilder а потом присвоить перемной String результат накопления
        in.close();

        return wikiInfo;
    }
}
