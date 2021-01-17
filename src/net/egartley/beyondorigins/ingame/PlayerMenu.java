package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.ui.ClickableArea;
import net.egartley.beyondorigins.core.ui.PlayerInventory;
import net.egartley.beyondorigins.core.ui.QuestsPanel;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * What shows up when the user presses E in game
 */
public class PlayerMenu implements Tickable {

    private final Color backgroundColor = new Color(0, 0, 0, 152);

    public UIElement panel;
    public QuestsPanel questsPanel;
    public ClickableArea questsPanelTab;
    public PlayerInventory inventoryPanel;
    public ClickableArea inventoryPanelTab;
    public ArrayList<ClickableArea> tabs = new ArrayList<>();

    public PlayerMenu() {
        // initialize panels
        inventoryPanel = new PlayerInventory();
        questsPanel = new QuestsPanel();
        // show inventory by default
        panel = inventoryPanel;
        // set and add tabs
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

    /**
     * Called when a tab (rather its clickable area) is clicked by the user
     *
     * @param clickedTab The tab that was clicked
     */
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
        // dim the game to make the menu easier to see
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        // actually show whatever panel is the current one
        panel.render(graphics);
    }

}
