package ru.practicum.kanban.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_TOKEN;
    private URI url;
    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String urlStorage) {
        this.url = URI.create(urlStorage);
    }

    public String register() throws IOException, InterruptedException {
        URI urlReg = URI.create(this.url + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlReg)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return API_TOKEN = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI urlSave = URI.create(this.url+ "/save" + key + "?API_TOKEN=" + API_TOKEN);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlSave)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ключ добавлен! " + response);
        System.out.println("json: " + json);
    }

    public String load(String key) throws IOException, InterruptedException {
        URI urlLoad = URI.create(this.url + "/load" + key + "?API_TOKEN=" + API_TOKEN);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlLoad)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }
}
