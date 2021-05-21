package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.QuestObjective;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class QuestsSidePanel extends UIElement {

    private Quest quest = null;
    private String[] descriptionLines;
    private final ArrayList<String[]> objectiveLines = new ArrayList<>();
    private static Image checkboxImage;
    private static Image checkboxCheckedImage;
    private static final Color titleColor = new Color(65, 53, 37);
    private static final Color descriptionColor = titleColor, objectiveTitleColor = titleColor, objectiveDescriptionColor = titleColor;
    private static final Font descriptionFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11), true);
    private static final Font objectiveDescriptionFont = descriptionFont;
    private static final Font objectiveTitleFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11), true);
    private static final Font titleFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.BOLD, 16), true);

    public QuestsSidePanel() {
        super(412, 277);
        setPosition(446, 186);
        checkboxImage = Images.get(Images.QUEST_CHECKBOX);
        checkboxCheckedImage = Images.get(Images.QUEST_CHECKBOX_CHECKED);
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
        if (quest != null) {
            descriptionLines = Util.toLines(quest.description, descriptionFont, 216);
            for (QuestObjective objective : quest.objectives) {
                objectiveLines.add(Util.toLines(objective.description, objectiveDescriptionFont, 216));
            }
        } else {
            descriptionLines = new String[]{};
        }
    }

    public Quest getQuest() {
        return quest;
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics graphics) {
        if (quest != null) {
            // title
            graphics.setFont(titleFont);
            graphics.setColor(titleColor);
            graphics.drawString(quest.title, x + 8, y + 2);
            // description
            graphics.setFont(descriptionFont);
            graphics.setColor(descriptionColor);
            int offset = 0;
            for (String line : descriptionLines) {
                graphics.drawString(line, x + 8, y + 24 + (offset * 14));
                offset++;
            }
            // objective(s)
            int baseY = y + 36 + (offset * 14);
            for (int i = 0; i < quest.objectives.size(); i++) {
                QuestObjective objective = quest.objectives.get(i);
                // checkbox
                if (!objective.isComplete) {
                    graphics.drawImage(checkboxImage, x + 8, baseY);
                } else {
                    graphics.drawImage(checkboxCheckedImage, x + 6, baseY - 2);
                }
                // title
                graphics.setFont(objectiveTitleFont);
                graphics.setColor(objectiveTitleColor);
                graphics.drawString(objective.title, x + 30, baseY);
                // description
                graphics.setFont(objectiveDescriptionFont);
                graphics.setColor(objectiveDescriptionColor);
                baseY += 32;
                for (String line : objectiveLines.get(i)) {
                    graphics.drawString(line, x + 8, baseY - 11);
                    baseY += 14;
                }
            }
        }
    }

}
