package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.Quest;
import org.newdawn.slick.*;

public class QuestSlot extends ClickableArea {

    private Image slotImage;
    private static boolean loadedImages;
    private static Image slotHoverImage, slotNormalImage;
    private static final Color slotSelectedTitleColor = Color.white, slotTitleColor = new Color(65, 53, 37);
    private static final Font slotTitleFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);

    public boolean isSelected;
    public Quest quest;

    public QuestSlot(Quest quest, int x, int y) {
        super(x, y, 0, 0);
        this.quest = quest;
        if (!loadedImages) {
            // might not be the best way to do this, but don't want to reload the same
            // images each time the constructor is called (hence they're static)
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
        slotImage = isCursorInBounds ? slotHoverImage : slotNormalImage;
    }

    public void render(Graphics graphics) {
        graphics.drawImage(slotImage, x, y);
        graphics.setFont(slotTitleFont);
        graphics.setColor(isSelected ? slotSelectedTitleColor : slotTitleColor);
        graphics.drawString(quest.title, x + 8, y + 6);
    }

}
