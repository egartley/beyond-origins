package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.logic.math.Calculate;

public class QuestsPanel extends UIElement {

    public QuestsPanel() {
        super(ImageStore.get(ImageStore.QUESTS_PANEL));
        setPosition(Calculate.getCenteredX(width), Calculate.getCenteredY(height));
    }

}
