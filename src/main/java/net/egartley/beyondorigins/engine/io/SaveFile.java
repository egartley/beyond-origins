package net.egartley.beyondorigins.engine.io;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveFile {

    int id;
    JSONObject data;

    void write(String path) throws IOException {
        Files.writeString(Paths.get(path), data.toString());
    }

}