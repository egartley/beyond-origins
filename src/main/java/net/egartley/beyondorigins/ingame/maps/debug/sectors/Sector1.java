package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.beyondorigins.engine.map.Map;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.engine.graphics.Sprite;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.engine.ui.NotificationBanner;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.data.Quests;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.WarpPad;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.buildings.House1;
import org.newdawn.slick.Input;

public class Sector1 extends MapSector {

    private WarpPad pad;
    private DefaultTree hatTree;

    public House1 house;

    public Sector1(Map parent) {
        super(parent, 1);
    }

    @Override
    public void init() {
        addEntityDirect(new DefaultTree(36, 200));
        hatTree = new DefaultTree(100, 200);
        addEntityDirect(hatTree);
        house = new House1(280, 200, 334, 313);
        addEntityDirect(house);
        pad = new WarpPad(Entities.getSpriteTemplate(Entities.TEMPLATE_WP), 500, 100);
        addEntityDirect(pad);
        // test items
        Entities.PLAYER.inventory.putItem(Items.HMM, 1);
        Entities.PLAYER.inventory.putItem(Items.CURRENT_YEAR, 3);
        // test quest
        /*Quest quest = new Quest(Quests.TEST_QUEST_2, "Test quest 2", "Here's another quest for testing, bud!");
        quest.objectives.add(new QuestObjective("Do this thing", "Because you HAVE TO. That's why."));
        quest.objectives.add(new QuestObjective("Do this other thing", "Just do it, already."));
        quest.objectives.add(new QuestObjective("Final task", "Get it over with."));
        InGameState.quests.add(quest);*/
        setSpecialCollisions();
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.setPosition(700, 140);
        } else {
            updatePlayerPosition(from);
        }
        Entities.PLAYER.generateSectorSpecificCollisions(this);
    }

    @Override
    public void onPlayerLeave(MapSector to) {

    }

    @Override
    public void setSpecialCollisions() {
        Collisions.add(new EntityEntityCollision(Entities.PLAYER.boundary, hatTree.defaultBoundary) {
            public void start(EntityEntityCollisionEvent e) {
                InGameState.showActionUI("Test", Input.KEY_ENTER);
                if (!Entities.PLAYER.inventory.containsItem(Items.WIZARD_HAT) &&
                        Entities.WIZARD.metPlayer && !Entities.WIZARD.foundHat) {
                    Entities.PLAYER.inventory.putItem(Items.WIZARD_HAT);
                    InGameState.pushNotification(
                            new NotificationBanner("You have found the Wizard's hat!",
                                    "items/wizard-hat.png"));
                    Quests.WIZARD_HAT.objectives.get(0).complete();
                }
            }

            public void end(EntityEntityCollisionEvent e) {
                InGameState.hideActionUI();
            }
        });
        Collisions.add(new EntityEntityCollision(Entities.PLAYER.boundary, pad.defaultBoundary) {
            public void start(EntityEntityCollisionEvent e) {
                end();
                InGameState.changeMap(1);
            }
        });
    }

}
