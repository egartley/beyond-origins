package net.egartley.beyondorigins.engine.logic.dialogue;

import net.egartley.beyondorigins.engine.ui.panel.DialoguePanel;
import net.egartley.beyondorigins.gamestates.InGameState;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A collection of dialogues between characters
 */
public class DialogueExchange {

    public boolean isFinished;
    public int characterDialogueIndex = -1;
    public String[] allLines;
    public String[] queuedLines;
    public String[] displayedLines;
    public CharacterDialogue currentDialogue;
    public ArrayList<CharacterDialogue> dialogues = new ArrayList<>();

    public DialogueExchange(CharacterDialogue... dialogues) {
        this.dialogues.addAll(Arrays.asList(dialogues));
        nextDialogue();
    }

    private void setCurrentDialogue(CharacterDialogue currentDialogue) {
        currentDialogue.onStart();
        this.currentDialogue = currentDialogue;
        allLines = this.currentDialogue.lines;
        displayedLines = allLines.length <= DialoguePanel.MAX_LINES ? allLines : Arrays.copyOfRange(allLines, 0, DialoguePanel.MAX_LINES);
        queuedLines = allLines.length <= DialoguePanel.MAX_LINES ? null : Arrays.copyOfRange(allLines, DialoguePanel.MAX_LINES, allLines.length);
    }

    private void nextLine() {
        System.arraycopy(displayedLines, 1, displayedLines, 0, DialoguePanel.MAX_LINES - 1);
        displayedLines[DialoguePanel.MAX_LINES - 1] = queuedLines[0];
        queuedLines = Arrays.copyOfRange(queuedLines, 1, queuedLines.length);
    }

    private void nextDialogue() {
        characterDialogueIndex++;
        if (isFinished) {
            return;
        }
        setCurrentDialogue(dialogues.get(characterDialogueIndex));
    }

    public void advance() {
        boolean isReadyToAdvance = InGameState.dialogue.isReadyToAdvance;
        isFinished = isCurrentDialogueFinished() && characterDialogueIndex + 1 == dialogues.size() && isReadyToAdvance;
        if (isFinished) {
            return;
        }
        if (isCurrentDialogueFinished() && isReadyToAdvance) {
            nextDialogue();
            InGameState.dialogue.delay();
        } else if (isReadyToAdvance) {
            nextLine();
        }
    }

    public void reset() {
        characterDialogueIndex = 0;
        setCurrentDialogue(dialogues.get(0));
        isFinished = false;
    }

    private boolean isCurrentDialogueFinished() {
        return queuedLines == null || queuedLines.length == 0;
    }

}
