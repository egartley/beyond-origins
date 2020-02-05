package net.egartley.gamelib.logic.dialogue;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.ui.DialoguePanel;
import net.egartley.gamelib.interfaces.Character;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class CharacterDialogue {

    private static final short MAX_LINE_LENGTH = 49;

    private int randomAmount;

    public Character character;
    public String path;
    public String[] lines;

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
            lines = Util.toLines(Files.readString(FileSystems.getDefault().getPath("resources", "dialogue", path)), DialoguePanel.lineFont, 376);
        } catch (IOException e) {
            Debug.error(e);
        }
    }

}
