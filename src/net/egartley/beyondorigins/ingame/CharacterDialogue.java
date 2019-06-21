package net.egartley.beyondorigins.ingame;

import net.egartley.gamelib.interfaces.Character;

import java.util.ArrayList;

public abstract class CharacterDialogue {

    private static final short MAX_LINE_LENGTH = 54;

    public Character character;
    public String[] lines;

    public CharacterDialogue(Character character, String dialogue) {
        this.character = character;
        lines = toLines(dialogue);
    }

    /**
     * Returns an array containing the given dialogue split into seperate lines, wrapped at words
     */
    private String[] toLines(String dialogue) {
        ArrayList<String> splits = new ArrayList<>();

        if (!(dialogue.length() <= MAX_LINE_LENGTH)) {
            // longer than max length, so split it into lines
            int cap = dialogue.length() / MAX_LINE_LENGTH;
            for (int i = 0; i < cap + 1; i++) {
                if (dialogue.length() <= MAX_LINE_LENGTH) {
                    continue;
                }
                String l = dialogue.substring(0, MAX_LINE_LENGTH);
                if (l.startsWith(" ")) {
                    // starts with space, probably from a previous split from within a word
                    l = l.substring(1);
                } else if (l.endsWith(" ")) {
                    // ends with space at max length
                    l = l.substring(0, l.length() - 1);
                }
                if (!dialogue.substring(dialogue.indexOf(l) + l.length(), dialogue.indexOf(l) + l.length() + 1).equals(" ")) {
                    // max length within a word, so split from preceding
                    l = l.substring(0, l.lastIndexOf(" "));
                }
                splits.add(l);
                dialogue = dialogue.substring(l.length() + 1);
            }
            if (!dialogue.isEmpty() && !dialogue.equals(" ")) {
                // add any remaining dialogue that isn't just a space
                if (dialogue.startsWith(" ")) {
                    // check for extra space at start
                    dialogue = dialogue.substring(1);
                }
                splits.add(dialogue);
            }
        } else {
            return new String[]{dialogue};
        }

        return splits.toArray(new String[]{});
    }

}
