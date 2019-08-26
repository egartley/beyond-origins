package net.egartley.gamelib.logic.dialogue;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.ingame.DialoguePanel;

import java.util.ArrayList;
import java.util.Arrays;

public class DialogueExchange {

    public int index = -1;
    public boolean isFinished;

    public String[] allLines;
    public String[] displayedLines;
    public String[] queuedLines;

    public CharacterDialogue dialogue;
    public ArrayList<CharacterDialogue> dialogues;

    public DialogueExchange(CharacterDialogue... dialogues) {
        this.dialogues = new ArrayList<>();
        this.dialogues.addAll(Arrays.asList(dialogues));
        nextDialogue();
    }

    public void setDialogue(CharacterDialogue dialogue) {
        this.dialogue = dialogue;
        allLines = this.dialogue.lines;
        if (allLines.length <= DialoguePanel.MAX_LINES) {
            // max number or less lines, will always display all of them
            displayedLines = allLines;
            queuedLines = null;
        } else {
            // (max number + 1) or more lines, queue up remaining
            displayedLines = Arrays.copyOfRange(allLines, 0, 6);
            queuedLines = Arrays.copyOfRange(allLines, 6, allLines.length);
        }
    }

    private void nextLine() {
        System.arraycopy(displayedLines, 1, displayedLines, 0, DialoguePanel.MAX_LINES - 1);
        displayedLines[DialoguePanel.MAX_LINES - 1] = queuedLines[0];
        queuedLines = Arrays.copyOfRange(queuedLines, 1, queuedLines.length);
    }

    public void nextDialogue() {
        index++;
        if (isFinished) {
            return;
        }
        setDialogue(dialogues.get(index));
    }

    public void advance() {
        if (currentDialogueFinished()) {
            DialogueController.onFinished(dialogue);
            nextDialogue();
            Game.in().dialoguePanel.setFontMetrics = false;
        } else {
            nextLine();
            isFinished = currentDialogueFinished() && index + 1 == dialogues.size();
        }
    }

    public void reset() {
        index = 0;
        setDialogue(dialogues.get(0));
        isFinished = false;
    }

    public boolean currentDialogueFinished() {
        return queuedLines == null || queuedLines.length == 0;
    }

}
