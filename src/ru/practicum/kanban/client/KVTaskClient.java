package ru.practicum.kanban.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String API_TOKEN;
    private String urlStorage;

    public KVTaskClient(String urlStorage) throws IOException, InterruptedException {
        this.urlStorage = urlStorage;
        URI url = URI.create(urlStorage + "register");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI url = URI.create(urlStorage + key + "?API_TOKEN=" + API_TOKEN);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ключ добавлен! " + response);
        System.out.println("json " + json);
    }

    public String load(String key) throws IOException, InterruptedException {
        URI url = URI.create(urlStorage + key + "?API_TOKEN=" + API_TOKEN);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }
}
