package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.Quest;
import org.newdawn.slick.*;

public class QuestSlot extends ClickableArea {

    private static boolean didLoadImages;
    private Image slotImage;
    private static Image slotHoverImage, slotNormalImage;
    private static final Color SLOT_SELECTED_TITLE_COLOR = Color.white,
            SLOT_TITLE_COLOR = new Color(65, 53, 37);
    private static final Font SLOT_TITLE_FONT =
            new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);

    public boolean isSelected;
    public Quest quest;

    public QuestSlot(Quest quest, int x, int y) {
        super(x, y, 0, 0);
        this.quest = quest;
        if (!didLoadImages) {
            // might not be the best way to do this, but don't want to reload the same
            // images each time the constructor is called (hence they're static)
            slotNormalImage = Images.getImage(Images.QUEST_SLOT);
            slotHoverImage = Images.getImage(Images.QUEST_SLOT_HOVER);
            didLoadImages = true;
        }
        slotImage = slotNormalImage;
        width = slotImage.getWidth();
        height = slotImage.getHeight();
    }

    @Override
    public void onClick() {
        InGameState.playerMenu.questsPanel.onSlotClicked(this);
    }

    @Override
    public void tick() {
        super.tick();
        slotImage = isCursorInBounds ? slotHoverImage : slotNormalImage;
    }

    public void render(Graphics graphics) {
        graphics.drawImage(slotImage, x, y);
        graphics.setFont(SLOT_TITLE_FONT);
        graphics.setColor(isSelected ? SLOT_SELECTED_TITLE_COLOR : SLOT_TITLE_COLOR);
        graphics.drawString(quest.title, x + 8, y + 6);
    }

}
