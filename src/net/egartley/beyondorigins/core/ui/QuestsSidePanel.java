package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.QuestObjective;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class QuestsSidePanel extends UIElement {

    private Quest quest;
    private String[] descriptionLines;
    private final ArrayList<String[]> objectiveLines = new ArrayList<>();
    private static Image checkboxImage;
    private static Image checkboxCheckedImage;
    private static final Color TITLE_COLOR = new Color(65, 53, 37);
    private static final Color DESCRIPTION_COLOR = TITLE_COLOR,
            OBJECTIVE_TITLE_COLOR = TITLE_COLOR,
            OBJECTIVE_DESCRIPTION_COLOR = TITLE_COLOR;
    private static final Font DESCRIPTION_FONT =
            new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11), true);
    private static final Font OBJECTIVE_DESCRIPTION_FONT = DESCRIPTION_FONT;
    private static final Font OBJECTIVE_TITLE_FONT =
            new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11), true);
    private static final Font TITLE_FONT =
            new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.BOLD, 16), true);

    public QuestsSidePanel() {
        super(412, 277);
        this.x = 446;
        this.y = 186;
        checkboxImage = Images.getImage(Images.QUEST_CHECKBOX);
        checkboxCheckedImage = Images.getImage(Images.QUEST_CHECKBOX_CHECKED);
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
        if (quest != null) {
            descriptionLines = Util.toLines(quest.description, DESCRIPTION_FONT, 216);
            for (QuestObjective o : quest.objectives) {
                objectiveLines.add(Util.toLines(o.description, OBJECTIVE_DESCRIPTION_FONT, 216));
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
            graphics.setFont(TITLE_FONT);
            graphics.setColor(TITLE_COLOR);
            graphics.drawString(quest.title, x + 8, y + 2);
            // description
            graphics.setFont(DESCRIPTION_FONT);
            graphics.setColor(DESCRIPTION_COLOR);
            int offset = 0;
            for (String l : descriptionLines) {
                graphics.drawString(l, x + 8, y + 24 + (offset * 14));
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
                graphics.setFont(OBJECTIVE_TITLE_FONT);
                graphics.setColor(OBJECTIVE_TITLE_COLOR);
                graphics.drawString(objective.title, x + 30, baseY);
                // description
                graphics.setFont(OBJECTIVE_DESCRIPTION_FONT);
                graphics.setColor(OBJECTIVE_DESCRIPTION_COLOR);
                baseY += 32;
                for (String l : objectiveLines.get(i)) {
                    graphics.drawString(l, x + 8, baseY - 11);
                    baseY += 14;
                }
            }
        }
    }

}
