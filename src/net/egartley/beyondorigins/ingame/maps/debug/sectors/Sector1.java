package net.egartley.beyondorigins.ingame.maps.debug.sectors;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.DefaultRock;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.entities.WoodenFence;
import net.egartley.beyondorigins.ingame.Inventory;
import net.egartley.beyondorigins.ingame.Item;
import net.egartley.beyondorigins.ingame.buildings.House1;
import net.egartley.beyondorigins.ui.NotificationBanner;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.Map;
import net.egartley.gamelib.abstracts.MapSector;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;

import java.awt.*;

public class Sector1 extends MapSector {

    public House1 house;

    private NotificationBanner testBanner;

    public Sector1(Map parent) {
        super(parent, 1);
    }

    @Override
    public void render(Graphics graphics) {
        drawTiles(graphics);
        for (Entity e : entities) {
            if (e.isDualRendered) {
                e.drawFirstLayer(graphics);
            } else {
                e.render(graphics);
            }
        }
        Entities.DUMMY.render(graphics);
        Entities.PLAYER.render(graphics);
        for (Entity e : entities) {
            if (e.isDualRendered) {
                e.drawSecondLayer(graphics);
            }
        }

        if (Game.debug) {
            changeBoundaries.forEach(boundary -> boundary.draw(graphics));
        }

        notifications.forEach(notification -> notification.render(graphics));
    }

    @Override
    public void tick() {
        super.tick();

        Entities.DUMMY.tick();
    }

    @Override
    public void initialize() {
        Game.in().inventory.put(Item.CURRENT_YEAR);
        if (!didInitialize) {
            // sector-specific entities
            Sprite s = Entities.getSpriteTemplate(Entities.TEMPLATE_TREE);
            entities.add(new DefaultTree(s, 36, 200));
            DefaultTree tree = new DefaultTree(s, 100, 200);
            tree.collisions.add(new EntityEntityCollision(Entities.PLAYER.boundary, tree.defaultBoundary) {
                public void start(EntityEntityCollisionEvent e) {
                    Inventory inventory = Game.in().inventory;
                    if (!inventory.has(Item.WIZARD_HAT) && Entities.WIZARD.metPlayer && !Entities.WIZARD.foundHat) {
                        inventory.put(Item.WIZARD_HAT);
                        pushNotification(new NotificationBanner("You have found the Wizard's hat!", "items/wizard-hat.png"));
                    }
                }
            });
            entities.add(tree);
            s = Entities.getSpriteTemplate(Entities.TEMPLATE_ROCK);
            int off = 0;
            for (byte i = 0; i < 14; i++) {
                entities.add(new DefaultRock(s, (s.width * 2) * off++ + 48, 400));
            }
            WoodenFence fence = new WoodenFence(8, true);
            fence.setPosition(534, 268);
            entities.add(fence);

            // buildings
            house = new House1(280, 200, 334, 313);
            entities.add(house);

            didInitialize = true;
        }
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.setPosition(700, 140);
        } else {
            updatePlayerPosition(from);
        }
        initialize();
        Entities.DUMMY.onSectorEnter(this);
        Entities.PLAYER.generateSectorSpecificCollisions(this);
    }

    @Override
    public void onPlayerLeave(MapSector to) {
        Entities.DUMMY.onSectorLeave(this);
        Entities.PLAYER.removeSectorSpecificCollisions(this);
    }

}
