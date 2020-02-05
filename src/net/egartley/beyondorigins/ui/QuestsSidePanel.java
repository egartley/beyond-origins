package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.QuestObjective;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class QuestsSidePanel extends UIElement {

    private Quest quest = null;
    private String[] descriptionLines;
    private ArrayList<String[]> objectiveLines;

    private static Font titleFont = new Font("Bookman Old Style", Font.BOLD, 16);
    private static Font descriptionFont = new Font("Arial", Font.PLAIN, 11);
    private static Font objectiveTitleFont = new Font("Arial", Font.BOLD, 11);
    private static Font objectiveDescriptionFont = descriptionFont;
    private static Color titleColor = new Color(65, 53, 37);
    private static Color descriptionColor = titleColor;
    private static Color objectiveTitleColor = titleColor;
    private static Color objectiveDescriptionColor = titleColor;
    private static BufferedImage checkboxImage;
    private static BufferedImage checkboxCheckedImage;

    public QuestsSidePanel() {
        super(412, 277);
        setPosition(446, 186);
        checkboxImage = ImageStore.get(ImageStore.QUEST_CHECKBOX);
        checkboxCheckedImage = ImageStore.get(ImageStore.QUEST_CHECKBOX_CHECKED);
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
        if (quest != null) {
            descriptionLines = Util.toLines(quest.description, descriptionFont, 216);
            objectiveLines = new ArrayList<>();
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
            graphics.drawString(quest.title, x() + 8, y() + 18);

            // description
            graphics.setFont(descriptionFont);
            graphics.setColor(descriptionColor);
            int offset = 0;
            for (String line : descriptionLines) {
                graphics.drawString(line, x() + 8, y() + 36 + (offset * 14));
                offset++;
            }

            // objective(s)
            int baseY = y() + 36 + (offset * 14);
            for (int i = 0; i < quest.objectives.size(); i++) {
                QuestObjective objective = quest.objectives.get(i);
                // checkbox
                if (!objective.isComplete) {
                    graphics.drawImage(checkboxImage, x() + 8, baseY, null);
                } else {
                    graphics.drawImage(checkboxCheckedImage, x() + 6, baseY - 2, null);
                }
                // title
                graphics.setFont(objectiveTitleFont);
                graphics.setColor(objectiveTitleColor);
                graphics.drawString(objective.title, x() + 30, baseY + 12);
                // description
                graphics.setFont(objectiveDescriptionFont);
                graphics.setColor(objectiveDescriptionColor);
                baseY += 32;
                for (String line : objectiveLines.get(i)) {
                    graphics.drawString(line, x() + 8, baseY);
                    baseY += 14;
                }
            }
        }
    }

}
