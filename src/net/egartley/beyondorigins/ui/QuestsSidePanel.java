package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.ingame.Quest;

import java.awt.*;

public class QuestsSidePanel extends UIElement {

    public Quest quest;

    private static Font titleFont = new Font("Bookman Old Style", Font.BOLD, 16);
    private static Color titleColor = new Color(65, 53, 37);

    public QuestsSidePanel() {
        super(412, 277);
        setPosition(446, 186);
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics graphics) {
        if (quest != null) {
            graphics.setFont(titleFont);
            graphics.setColor(titleColor);
            graphics.drawString(quest.title, x() + 8, y() + 18);
        } else {
            // click a quest to view its objectives, something like that
        }
    }

}
