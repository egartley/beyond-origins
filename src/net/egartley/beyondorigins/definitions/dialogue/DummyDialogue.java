package net.egartley.beyondorigins.definitions.dialogue;

import net.egartley.beyondorigins.ingame.CharacterDialogue;
import net.egartley.beyondorigins.objects.Character;

public class DummyDialogue extends CharacterDialogue {

    public static DummyDialogue CAUGHT_UP_WITH_PLAYER;

    DummyDialogue(Character character, String dialogue) {
        super(character, dialogue);
    }

    public static void initialize() {
        CAUGHT_UP_WITH_PLAYER = new DummyDialogue(null, "The rot enforces the bicycle. An interview refines the ranging angel under the choral. Every civil calculator rots behind the secondary computer. A stake rephrases the crazy verb. A tactic chops the earned sore. The celebrated steer bicycles within the nominate voter. The explosive interferes without an urge. The enforced water remarks a photo opposite the closing dilemma. How will the cotton stamp? An orbit apologizes into the bounce. How will the chemistry spin outside the powerful cable? The secondary name gasps. Why can't the syndrome cooperate outside his recognized receiver? A paper decline surnames the coffee. Past the cynic hardship stretches an admirable lawyer.");
    }

}
