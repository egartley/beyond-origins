package net.egartley.beyondorigins.engine.logic.dialogue;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.interfaces.Character;
import net.egartley.beyondorigins.engine.ui.panel.DialoguePanel;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A piece of dialogue for a specific character
 */
public class CharacterDialogue {

    private int amountOfRandoms;

    public String path;
    public String[] lines;
    public Character character;

    public CharacterDialogue(Character character, String path) {
        this(character, path, false, 0);
    }

    public CharacterDialogue(Character character, String path, boolean random, int amountOfRandoms) {
        this.character = character;
        this.path = path;
        if (!random) {
            setLines(path);
        } else {
            this.amountOfRandoms = amountOfRandoms;
            setLinesFromRandom();
        }
    }

    public void onStart() {
        setLinesFromRandom();
    }

    public void setLinesFromRandom() {
        if (amountOfRandoms <= 0) {
            return;
        }
        setLines(path + "-" + Util.randomInt(1, amountOfRandoms, true) + ".def");
    }

    private void setLines(String path) {
        try {
            lines = Util.toLines(Files.readString(Path.of("src", "main", "resources", "dialogue", path)),
                    DialoguePanel.LINE_FONT, 370);
        } catch (IOException e) {
            Debug.error(e);
        }
    }

}
