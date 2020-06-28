package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.ingame.InGameState;
import net.egartley.beyondorigins.ingame.Quest;
import org.newdawn.slick.*;

public class QuestSlot extends ClickableArea {

    public boolean isSelected;
    public Quest quest;
    private Image slotImage;

    private static boolean loadedImages;
    private static Image slotNormalImage;
    private static Image slotHoverImage;
    private static final Font slotTitleFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 12), true);
    private static final Color slotTitleColor = new Color(65, 53, 37);
    private static final Color slotSelectedTitleColor = Color.white;

    public QuestSlot(Quest quest, int x, int y) {
        super(x, y, 0, 0);
        this.quest = quest;
        if (!loadedImages) {
            // might not be the best way to do this, but don't want to reload the same images each time the constructor is called
            slotNormalImage = Images.get(Images.QUEST_SLOT);
            slotHoverImage = Images.get(Images.QUEST_SLOT_HOVER);
            loadedImages = true;
        }
        slotImage = slotNormalImage;
        width = slotImage.getWidth();
        height = slotImage.getHeight();
    }

    @Override
    public void onClick() {
        InGameState.playerMenu.questsPanel.slotClicked(this);
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
