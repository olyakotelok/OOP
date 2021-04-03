package com.company;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WikiApi {
    private static final String API_ENDPOINT = "https://ru.wikipedia.org/w/api.php?format=json&action=query&prop=pageimages%7Cextracts&indexpageids&exintro&explaintext&images&pithumbsize=300&titles=";

    public CityInfo getCityInfo(String cityName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + cityName))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }

        JSONObject data = new JSONObject(response.body());
        String id = data
                .getJSONObject("query")
                .getJSONArray("pageids")
                .getString(0);
        JSONObject answer = data
                .getJSONObject("query")
                .getJSONObject("pages")
                .getJSONObject(id);

        return new CityInfo(
                answer.getString("title"),
                answer.getString("extract"),
                answer.getJSONObject("thumbnail").getString("source")
        );
    }
}
