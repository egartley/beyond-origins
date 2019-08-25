package net.egartley.gamelib.logic.events;

import net.egartley.gamelib.logic.dialogue.CharacterDialogue;

public class CharacterDialogueFinishedEvent {

    public CharacterDialogue dialogue;

    public CharacterDialogueFinishedEvent(CharacterDialogue dialogue) {
        this.dialogue = dialogue;
    }

    public void onFinish() {

    }

}
