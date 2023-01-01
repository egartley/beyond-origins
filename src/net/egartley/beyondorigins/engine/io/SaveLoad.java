package net.egartley.beyondorigins.engine.io;

import net.egartley.beyondorigins.Debug;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveLoad {

    private static int currentID = -1;
    private static JSONObject currentData;

    public static void setCurrentID(int id) {
        currentID = id;
    }

    public static void save() {
        save(currentID);
    }

    public static void save(int id) {
        try {
            SaveFile file = new SaveFile();
            file.id = currentID;
            file.data = currentData;
            file.write(getSaveFilePath(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        load(currentID);
    }

    public static void load(int id) {
        try {
            SaveFile file = getSaveFile(id);
            currentData = file.data;
        } catch (IOException e) {
            Debug.error(e);
        }
    }

    public static void set(String key, Object value) {
        if (currentData == null) {
            currentData = new JSONObject();
        }
        if (currentData.has(key)) {
            currentData.remove(key);
        }
        currentData.put(key, value);
    }

    public static JSONObject get(String key) {
        if (currentData != null) {
            return currentData.getJSONObject(key);
        }
        Debug.warning("Must load first!");
        return null;
    }

    private static String getSaveFilePath(int id) {
        String base = System.getProperty("user.home") + "\\AppData\\Roaming\\Beyond Origins\\saves";
        try {
            Files.createDirectories(Paths.get(base));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base + "\\save" + id + ".bos";
    }

    private static SaveFile getSaveFile(int id) throws IOException {
        String full = Files.readString(Paths.get(getSaveFilePath(id)));
        SaveFile file = new SaveFile();
        file.id = id;
        file.data = new JSONObject(full);
        return file;
    }

}
