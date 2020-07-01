package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.ui.ClickableArea;
import net.egartley.beyondorigins.core.ui.PlayerInventory;
import net.egartley.beyondorigins.core.ui.QuestsPanel;
import net.egartley.beyondorigins.core.ui.UIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class PlayerMenu implements Tickable {

    static final int ROWS = 5, COLUMNS = 4;

    public UIElement panel;
    public PlayerInventory inventoryPanel;
    public QuestsPanel questsPanel;
    public ArrayList<ClickableArea> tabs = new ArrayList<>();
    public ClickableArea inventoryPanelTab, questsPanelTab;

    private final Color backgroundColor = new Color(0, 0, 0, 152);

    public PlayerMenu() {
        inventoryPanel = new PlayerInventory();
        questsPanel = new QuestsPanel();

        // show inventory by default
        panel = inventoryPanel;

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

    private void onTabClicked(ClickableArea clickedTab) {
        if (clickedTab.equals(questsPanelTab) && !panel.equals(questsPanel)) {
            questsPanel.onShow();
        } else if (!clickedTab.equals(questsPanelTab) && panel.equals(questsPanel)) {
            questsPanel.onHide();
        }
        if (clickedTab.equals(inventoryPanelTab)) {
            panel = inventoryPanel;
        } else if (clickedTab.equals(questsPanelTab)) {
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
