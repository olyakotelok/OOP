package com.company.games;

import com.company.CityInfo;
import com.company.Message;
import com.company.WikiApi;
import com.company.interfaces.IGame;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;


public class Goroda implements IGame {
    private final String[] cities = getCities();
    private final HashSet<String> usedCities = new HashSet<>();
    private String currentCity = "Амстердам";
    private String lastLetter = "м";
    private Boolean finished = false;
    private String lastMessage;
    private final WikiApi wikiApi = new WikiApi();

    public String[] getCities() {
        String url = "https://raw.githubusercontent.com/pensnarik/russian-cities/master/russian-cities.json";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new String[]{"Москва", "Амстердам", "Анадырь", "Ростов", "Волгоград", "Донецк"};
        }

        var resp = new JSONObject("{\"cities\": [" + response.body().substring(1, response.body().length() - 1) + "}");
 //       var resp_array = resp.getJSONArray("cities");
        System.out.println(resp.getJSONArray("cities"));
        return null;
//        String[] result = new String[]{};
//        for (var i = 0; i < resp.length(); i++){
//            var name = resp[i].getString("name");
//            result[i] = name;
//        }

//        return result;
    }

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
    public Message answerMessage(String str) {
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
            CityInfo info = wikiApi.getCityInfo(city);
            updateCurrentCity(city);
            return save(info == null
                    ? city
                    : String.join("\n", "<b>" + info.Name + "</b>", info.Info), info.Image
            );
        }

        finished = true;
        return save("Я не знаю подходящего города, игра окончена!");
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

    private Message save(String message) {
        lastMessage = message;
        return new Message(message);
    }

    private Message save(String message, String image){
        lastMessage = message;
        return new Message(message, image);
    }

    @Override
    public String getHelp() {
        return "Я называю город, ты называешь город на последнюю букву моего и так далее...\n" +
                "Чтобы закончить, введи: хватит\nНапомнить правила можно командной \\help";
    }
}
