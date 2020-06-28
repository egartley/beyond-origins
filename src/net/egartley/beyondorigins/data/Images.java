package net.egartley.beyondorigins.data;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Images {

    public final static byte PLAYER = 0;
    public final static byte DUMMY = 1;
    public final static byte WIZARD_DEFAULT = 11;
    public final static byte WIZARD_WITH_HAT = 12;

    public final static byte TILE_GRASS = 2;
    public final static byte TILE_SAND = 3;

    public final static byte TREE_DEFAULT = 4;
    public final static byte ROCK_DEFAULT = 5;
    public final static byte WIZARD_HAT = 13;

    public final static byte DIALOGUE_PANEL = 7;
    public final static byte INVENTORY_PANEL = 8;
    public final static byte MORE_LINES = 9;
    public final static byte INVENTORY_SLOT = 10;
    public final static byte QUESTS_PANEL = 14;
    public final static byte QUEST_SLOT = 15;
    public final static byte QUEST_SLOT_HOVER = 16;
    public final static byte QUEST_CHECKBOX = 17;
    public final static byte QUEST_CHECKBOX_CHECKED = 18;
    public final static byte PAGE_BUTTON_ENABLED = 19;
    public final static byte PAGE_BUTTON_DISABLED = 20;
    public final static byte PAGE_BUTTON_HOVER = 21;

    public static String path = "resources/images/";
    public static String entityPath = path + "entities/";
    public static String expressionPath = entityPath + "expressions/";
    public static String mapTilePath = path + "map-tiles/";
    public static String uiPath = path + "ui/";

    /**
     * Returns the specified image
     */
    public static Image get(byte image) {
        switch (image) {
            case PLAYER:
                return get(entityPath + "player-default.png");
            case DUMMY:
                return get(entityPath + "dummy.png");
            case WIZARD_DEFAULT:
                return get(entityPath + "wizard-default.png");
            case WIZARD_WITH_HAT:
                return get(entityPath + "wizard-with-hat.png");
            case TREE_DEFAULT:
                return get(entityPath + "tree-default.png");
            case ROCK_DEFAULT:
                return get(entityPath + "rock-default.png");
            case WIZARD_HAT:
                return get(entityPath + "wizard-hat.png");
            case DIALOGUE_PANEL:
                return get(uiPath + "dialogue-panel.png");
            case INVENTORY_PANEL:
                return get(uiPath + "inventory-panel.png");
            case QUESTS_PANEL:
                return get(uiPath + "quests-panel.png");
            case MORE_LINES:
                return get(uiPath + "more-lines.png");
            case INVENTORY_SLOT:
                return get(uiPath + "inventory-slot.png");
            case QUEST_SLOT:
                return get(uiPath + "quest-slot.png");
            case QUEST_SLOT_HOVER:
                return get(uiPath + "quest-slot_hover.png");
            case QUEST_CHECKBOX:
                return get(uiPath + "quest-checkbox.png");
            case QUEST_CHECKBOX_CHECKED:
                return get(uiPath + "quest-checkbox_checked.png");
            case PAGE_BUTTON_ENABLED:
                return get(uiPath + "page-button.png");
            case PAGE_BUTTON_DISABLED:
                return get(uiPath + "page-button_disabled.png");
            case PAGE_BUTTON_HOVER:
                return get(uiPath + "page-button_hover.png");
            default:
                return get(path + "unknown.png");
        }
    }

    /**
     * Returns an image at the specified path
     */
    public static Image get(String path) {
        try {
            return new Image(path);
        } catch (SlickException e) {
            e.printStackTrace();
            return null;
        }
    }

}
