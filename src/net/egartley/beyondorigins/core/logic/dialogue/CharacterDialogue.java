package net.egartley.beyondorigins.core.logic.dialogue;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.interfaces.Character;
import net.egartley.beyondorigins.core.ui.DialoguePanel;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * A piece of dialogue for a specific character
 */
public class CharacterDialogue {

    private int randomAmount;

    public String path;
    public String[] lines;
    public Character character;

    public CharacterDialogue(Character character, String path) {
        this(character, path, false, 0);
    }

    public CharacterDialogue(Character character, String path, boolean random, int randomAmount) {
        this.character = character;
        this.path = path;
        if (!random) {
            setLines(path);
        } else {
            this.randomAmount = randomAmount;
            random();
        }
    }

    public void onStart() {
        random();
    }

    public void random() {
        if (randomAmount <= 0) {
            return;
        }
        setLines(path + "-" + Util.randomInt(randomAmount, 1, true) + ".def");
    }

    private void setLines(String path) {
        try {
            lines = Util.toLines(Files.readString(FileSystems.getDefault().getPath("resources", "dialogue", path)), DialoguePanel.lineFont, 370);
        } catch (IOException e) {
            Debug.error(e);
        }
    }

}
