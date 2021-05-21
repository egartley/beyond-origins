package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.abstracts.Renderable;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.BuildingChanger;
import net.egartley.beyondorigins.entities.Entities;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;

public class BuildingFloor extends Renderable implements Tickable {

    private final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<BuildingChanger> changers = new ArrayList<>();
    private final ArrayList<EntityEntityCollision> changerCollisions = new ArrayList<>();

    public int number;
    public int leftLimit;
    public int rightLimit;
    public int upperYLimit;
    public int lowerYLimit;
    public Image image;
    public Building parent;

    public BuildingFloor(int number, Building parent) {
        this.number = number;
        this.parent = parent;
        image = Images.get("resources/images/buildings/floors/" + parent.name + "_" + number + ".png");
        setPosition(Calculate.getCenteredX(image.getWidth()), Calculate.getCenteredY(image.getHeight()));
        upperYLimit = y;
        lowerYLimit = y + image.getHeight() - Entities.PLAYER.sprite.height;
        leftLimit = x;
        rightLimit = x + image.getWidth() - Entities.PLAYER.sprite.width;
    }

    public void addChanger(BuildingChanger changer) {
        changers.add(changer);
        BuildingFloor me = this;
        EntityEntityCollision collision = new EntityEntityCollision(Entities.PLAYER.boundary, changer.defaultBoundary) {
            @Override
            public void start(EntityEntityCollisionEvent event) {
                end();
                switch (changer.action) {
                    case BuildingChanger.UPSTAIRS:
                        me.parent.upstairs();
                        break;
                    case BuildingChanger.DOWNSTAIRS:
                        me.parent.downstairs();
                        break;
                    case BuildingChanger.LEAVE:
                        me.parent.onPlayerLeave();
                        break;
                    case BuildingChanger.JUMP:
                        me.parent.changeFloor(me.parent.floors.get(changer.jumpNumber));
                        break;
                    default:
                        break;
                }
            }
        };
        changerCollisions.add(collision);
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void onPlayerLeave() {
        changerCollisions.forEach(Collisions::endRemove);
    }

    public void onPlayerEnter(BuildingFloor from) {
        changerCollisions.forEach(Collisions::add);
    }

    /**
     * Ensures the player doesn't move beyond the limits of this floor (into the background)
     */
    public void checkPlayerLimits() {
        Entities.PLAYER.isAllowedToMoveUpwards = Entities.PLAYER.y > upperYLimit;
        Entities.PLAYER.isAllowedToMoveDownwards = Entities.PLAYER.y < lowerYLimit;
        Entities.PLAYER.isAllowedToMoveLeftwards = Entities.PLAYER.x > leftLimit;
        Entities.PLAYER.isAllowedToMoveRightwards = Entities.PLAYER.x < rightLimit;
    }

    @Override
    public void tick() {
        changerCollisions.forEach(EntityEntityCollision::tick);
        entities.forEach(Entity::tick);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x, y);
        entities.forEach(e -> e.render(graphics));
        if (Game.debug) {
            changers.forEach(c -> c.defaultBoundary.render(graphics));
        }
    }

}
