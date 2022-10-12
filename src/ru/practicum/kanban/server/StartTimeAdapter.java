package ru.practicum.kanban.server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;

public class StartTimeAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(final JsonWriter jsonWriter, final Instant instant) throws IOException {
        jsonWriter.value(instant.toEpochMilli());
    }

    @Override
    public Instant read(final JsonReader jsonReader) throws IOException {
        return Instant.ofEpochMilli(Long.parseLong(jsonReader.nextString()));
    }
}