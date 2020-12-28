package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.ingame.Quest;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;

public class QuestsPanel extends UIElement {

    private final QuestsSidePanel sidePanel;
    private final ArrayList<QuestSlot> slots;
    private final ImageButton pageLeftButton;
    private final ImageButton pageRightButton;

    public QuestsPanel() {
        super(Images.get(Images.QUESTS_PANEL));
        setPosition(Calculate.getCenteredX(width), Calculate.getCenteredY(height));

        slots = new ArrayList<>();
        sidePanel = new QuestsSidePanel();
        Image e = Images.get(Images.PAGE_BUTTON_ENABLED);
        Image d = Images.get(Images.PAGE_BUTTON_DISABLED);
        Image h = Images.get(Images.PAGE_BUTTON_HOVER);
        pageLeftButton = new ImageButton(e, d, h, 324, 359) {
            public void onClick() {
                pageLeftButtonClick();
            }
        };
        e = e.copy();
        d = d.copy();
        h = h.copy();
        e.rotate(180);
        d.rotate(180);
        h.rotate(180);
        pageRightButton = new ImageButton(e, d, h, 362, 359) {
            public void onClick() {
                pageRightButtonClick();
            }
        };
    }

    public void add(Quest quest) {
        add(quest, false);
    }

    public void add(Quest quest, boolean start) {
        boolean contains = false;
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).quest.equals(quest)) {
                contains = true;
                break;
            }
        }
        // contains = false;
        if (!contains) {
            slots.add(new QuestSlot(quest, getSlotX(slots.size()), getSlotY(slots.size())));
            if (start) {
                quest.start();
            }
        }
    }

    public void remove(Quest quest) {
        int index = -1;
        boolean contains = false;
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).quest.equals(quest)) {
                contains = true;
                index = i;
                break;
            }
        }
        if (contains) {
            slots.get(index).removeClicked();
            slots.remove(index);
        }
    }

    public void slotClicked(QuestSlot clickedSlot) {
        if (!slots.contains(clickedSlot)) {
            Debug.warning("Tried to click a quest slot that isn't showing in the panel! (\"" + clickedSlot.quest + "\")");
            return;
        }
        if (sidePanel.getQuest() != null && sidePanel.getQuest().equals(clickedSlot.quest)) {
            // clicked the already selected slot, so remove it from the side panel
            sidePanel.setQuest(null);
            clickedSlot.isSelected = false;
        } else {
            // clicked a slot not already selected, display its details in the side panel
            for (QuestSlot slot : slots) {
                // make sure any previously selected slot is de-selected
                slot.isSelected = false;
            }
            sidePanel.setQuest(clickedSlot.quest);
            clickedSlot.isSelected = true;
        }
    }

    public void onShow() {
        pageLeftButton.registerClicked();
        pageRightButton.registerClicked();
    }

    public void onHide() {
        pageLeftButton.deregisterClicked();
        pageRightButton.deregisterClicked();
    }

    private void pageLeftButtonClick() {
        // Debug.out("left");
    }

    private void pageRightButtonClick() {
        // Debug.out("right");
    }

    public Quest get(byte id) {
        for (QuestSlot slot : slots) {
            if (slot.quest.id == id) {
                return slot.quest;
            }
        }
        return null;
    }

    private int getSlotX(int i) {
        // keep the index argument in case of future use with resizable window
        return x() + 13;
    }

    private int getSlotY(int i) {
        return y() + (53 + (i) * 34);
    }

    @Override
    public void tick() {
        slots.forEach(QuestSlot::tick);
        sidePanel.tick();
        pageLeftButton.tick();
        pageRightButton.tick();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y());

        // assume, at least for now, that there's no need for scrolling (more than 5 at a time)
        for (QuestSlot slot : slots) {
            slot.render(graphics);
        }

        sidePanel.render(graphics);

        pageLeftButton.render(graphics);
        pageRightButton.render(graphics);
    }

}
