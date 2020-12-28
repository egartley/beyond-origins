package net.egartley.beyondorigins.ingame.quests;

import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.QuestObjective;

public class WizardHatQuest extends Quest {

    public WizardHatQuest() {
        super((byte) 0, "Missing hat", "The wizard's hat has gone missing! You must find it and ensure its safe return.");
        objectives.add(new QuestObjective("Locate the Wizard's hat", "It's in one of the trees, dummy!"));
        objectives.add(new QuestObjective("Return the hat", "Go back to the Wizard and give him back his magical hat."));
    }

}
