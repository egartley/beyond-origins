package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ingame.Quest;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QuestSlot extends ClickableArea {

    public boolean isSelected;
    public Quest quest;
    private BufferedImage slotImage;

    private static boolean loadedImages;
    private static BufferedImage slotNormalImage;
    private static BufferedImage slotHoverImage;
    private static Font slotTitleFont = new Font("Bookman Old Style", Font.BOLD, 12);
    private static Color slotTitleColor = new Color(65, 53, 37);
    private static Color slotSelectedTitleColor = Color.WHITE;

    public QuestSlot(Quest quest, int x, int y) {
        super(x, y, 0, 0);
        this.quest = quest;
        if (!loadedImages) {
            // might not be the best way to do this, but don't want to reload the same images each time the constructor is called
            slotNormalImage = ImageStore.get(ImageStore.QUEST_SLOT);
            slotHoverImage = ImageStore.get(ImageStore.QUEST_SLOT_HOVER);
            loadedImages = true;
        }
        slotImage = slotNormalImage;
        width = slotImage.getWidth();
        height = slotImage.getHeight();
    }

    @Override
    public void onClick() {
        Game.in().playerMenu.questsPanel.slotClicked(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (mouseWithinBounds) {
            slotImage = slotHoverImage;
        } else {
            slotImage = slotNormalImage;
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(slotImage, x, y, null);
        graphics.setFont(slotTitleFont);
        if (isSelected) {
            graphics.setColor(slotSelectedTitleColor);
        } else {
            graphics.setColor(slotTitleColor);
        }
        graphics.drawString(quest.title, x + 8, y + 20);
    }

}
