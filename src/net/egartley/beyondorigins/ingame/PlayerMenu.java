package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ui.ClickableArea;
import net.egartley.beyondorigins.ui.PlayerInventory;
import net.egartley.beyondorigins.ui.QuestsPanel;
import net.egartley.beyondorigins.ui.UIElement;
import net.egartley.gamelib.interfaces.Tickable;

import java.awt.*;
import java.util.ArrayList;

public class PlayerMenu implements Tickable {

    static final int ROWS = 5, COLUMNS = 4;

    public boolean isShowing;

    public UIElement panel;
    public PlayerInventory inventoryPanel;
    public QuestsPanel questsPanel;
    public ArrayList<ClickableArea> tabs = new ArrayList<>();
    public ClickableArea inventoryPanelTab, questsPanelTab;

    private Color backgroundColor = new Color(0, 0, 0, 152);

    public PlayerMenu() {
        inventoryPanel = new PlayerInventory();
        questsPanel = new QuestsPanel();

        // show inventory by default
        panel = inventoryPanel;
        isShowing = true;

        inventoryPanelTab = new ClickableArea(panel.x() + 20, panel.y() + 1, 53, 25) {
            @Override
            public void onHover() {

            }

            @Override
            public void onClick() {
                onTabClicked(this);
            }
        };
        questsPanelTab = new ClickableArea(panel.x() + 79, panel.y() + 1, 53, 25) {
            @Override
            public void onHover() {

            }

            @Override
            public void onClick() {
                onTabClicked(this);
            }
        };
        tabs.add(inventoryPanelTab);
        tabs.add(questsPanelTab);
    }

    private void onTabClicked(ClickableArea tab) {
        isShowing = false;
        if (tab.equals(inventoryPanelTab)) {
            panel = inventoryPanel;
            isShowing = true;
        } else if (tab.equals(questsPanelTab)) {
            panel = questsPanel;
        }
    }

    @Override
    public void tick() {
        panel.tick();
        tabs.forEach(ClickableArea::tick);
    }

    public void render(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);
        panel.render(graphics);
    }

}
