package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.BuildingChanger;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.Renderable;
import net.egartley.gamelib.interfaces.Tickable;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.math.Calculate;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.ArrayList;

public class BuildingFloor extends Renderable implements Tickable {

    private final ArrayList<Entity> entities = new ArrayList<>();
    private final ArrayList<BuildingChanger> changers = new ArrayList<>();
    private final ArrayList<EntityEntityCollision> changerCollisions = new ArrayList<>();

    public int number;
    public int upperYLimit, lowerYLimit, leftLimit, rightLimit;
    public Building parent;
    public Image image;

    public BuildingFloor(int number, Building parent) {
        this.number = number;
        this.parent = parent;
        image = Images.get("resources/images/buildings/floors/" + parent.name + "_" + number + ".png");
        setPosition(Calculate.getCenteredX(image.getWidth()), Calculate.getCenteredY(image.getHeight()));
        upperYLimit = y();
        lowerYLimit = y() + image.getHeight() - Entities.PLAYER.sprite.height;
        leftLimit = x();
        rightLimit = x() + image.getWidth() - Entities.PLAYER.sprite.width;
    }

    public void onPlayerEnter(BuildingFloor from) {

    }

    public void onPlayerLeave() {

    }

    /**
     * Ensures the player doesn't move beyond the limits of this floor (into the black)
     */
    public void checkPlayerLimits() {
        Entities.PLAYER.isAllowedToMoveUpwards = Entities.PLAYER.y() > upperYLimit;
        Entities.PLAYER.isAllowedToMoveDownwards = Entities.PLAYER.y() < lowerYLimit;
        Entities.PLAYER.isAllowedToMoveLeftwards = Entities.PLAYER.x() > leftLimit;
        Entities.PLAYER.isAllowedToMoveRightwards = Entities.PLAYER.x() < rightLimit;
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
                        me.parent.leave();
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

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x(), y());
        entities.forEach(e -> e.render(graphics));

        if (Game.debug) {
            changers.forEach(c -> c.defaultBoundary.draw(graphics));
        }
    }

    @Override
    public void tick() {
        changerCollisions.forEach(EntityEntityCollision::tick);
        entities.forEach(Entity::tick);
    }

}
