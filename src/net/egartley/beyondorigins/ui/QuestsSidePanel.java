package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.ingame.Quest;

import java.awt.*;

public class QuestsSidePanel extends UIElement {

    private Quest quest = null;
    private String[] descriptionLines;

    private static Font titleFont = new Font("Bookman Old Style", Font.BOLD, 16);
    private static Font descriptionFont = new Font("Arial", Font.PLAIN, 11);
    private static Color titleColor = new Color(65, 53, 37);
    private static Color descriptionColor = titleColor;

    public QuestsSidePanel() {
        super(412, 277);
        setPosition(446, 186);
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
        if (quest != null) {
            descriptionLines = Util.toLines(quest.description, descriptionFont, 216);
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
        }
    }

}
