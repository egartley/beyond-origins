package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.logic.math.Calculate;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QuestsPanel extends UIElement {

    // 13, 53

    private BufferedImage slotImage;

    public QuestsPanel() {
        super(ImageStore.get(ImageStore.QUESTS_PANEL));
        setPosition(Calculate.getCenteredX(width), Calculate.getCenteredY(height));

        slotImage = ImageStore.get(ImageStore.QUEST_SLOT);
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics graphics) {

    }

}
