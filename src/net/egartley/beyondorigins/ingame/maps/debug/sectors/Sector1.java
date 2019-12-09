package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.beyondorigins.data.ItemStore;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.WoodenFence;
import net.egartley.beyondorigins.ingame.buildings.House1;
import net.egartley.beyondorigins.ui.NotificationBanner;
import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.abstracts.MapSector;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;

public class Sector1 extends MapSector {

    public House1 house;

    public Sector1(Map parent) {
        super(parent, 1);
    }

    @Override
    public void initialize() {
        if (!didInitialize) {
            // sector-specific entities
            Sprite s = Entities.getSpriteTemplate(Entities.TEMPLATE_TREE);
            addEntity(new DefaultTree(s, 36, 200));
            DefaultTree tree = new DefaultTree(s, 100, 200);
            tree.collisions.add(new EntityEntityCollision(Entities.PLAYER.boundary, tree.defaultBoundary) {
                public void start(EntityEntityCollisionEvent e) {
                    if (!Entities.PLAYER.inventory.contains(ItemStore.WIZARD_HAT) && Entities.WIZARD.metPlayer && !Entities.WIZARD.foundHat) {
                        Entities.PLAYER.inventory.put(ItemStore.WIZARD_HAT);
                        pushNotification(new NotificationBanner("You have found the Wizard's hat!", "items/wizard-hat.png"));
                    }
                }
            });
            addEntity(tree);
            s = Entities.getSpriteTemplate(Entities.TEMPLATE_ROCK);
            int off = 0;
            for (byte i = 0; i < 14; i++) {
                // addEntity(new DefaultRock(s, (s.width * 2) * off++ + 48, 400));
            }
            WoodenFence fence = new WoodenFence(8, true);
            fence.setPosition(534, 268);
            addEntity(fence);

            // buildings
            house = new House1(280, 200, 334, 313);
            addEntity(house);

            didInitialize = true;
            Entities.PLAYER.inventory.put(ItemStore.WIZARD_HAT, 2);
            Entities.PLAYER.inventory.put(ItemStore.HMM, 1);
            Entities.PLAYER.inventory.put(ItemStore.CURRENT_YEAR, 3);
            // Debug.out(Entities.PLAYER.inventory);
        }
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.setPosition(700, 140);
        } else {
            updatePlayerPosition(from);
        }
        // Entities.DUMMY.onSectorEnter(this);
        Entities.PLAYER.generateSectorSpecificCollisions(this);
    }

    @Override
    public void onPlayerLeave(MapSector to) {
        // Entities.DUMMY.onSectorLeave(this);
        Entities.PLAYER.removeSectorSpecificCollisions(this);
    }

}
