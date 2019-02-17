package net.egartley.beyondorigins.definitions.dialogue;

import net.egartley.beyondorigins.ingame.CharacterDialogue;
import net.egartley.beyondorigins.objects.Character;

public class DummyDialogue extends CharacterDialogue {

    public static DummyDialogue CAUGHT_UP_WITH_PLAYER;

    DummyDialogue(Character character, String dialogue) {
        super(character, dialogue);
    }

    public static void initialize() {
        CAUGHT_UP_WITH_PLAYER = new DummyDialogue(null, "Stop bumping into me, dude! I may be dumb, but I am not stupid!");
    }

}
