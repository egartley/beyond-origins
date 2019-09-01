package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ingame.Item;
import net.egartley.gamelib.abstracts.AnimatedEntity;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.MapSector;
import net.egartley.gamelib.graphics.Animation;
import net.egartley.gamelib.graphics.EntityExpression;
import net.egartley.gamelib.graphics.SpriteSheet;
import net.egartley.gamelib.interfaces.Character;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.dialogue.CharacterDialogue;
import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.events.DialogueExchangeFinishedEvent;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Dummy extends AnimatedEntity implements Character {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int ANIMATION_THRESHOLD = 150;

    private boolean isTalkingToPlayer;
    private short walktime = 0;
    private byte dir = DIRECTION_RIGHT;
    private DialogueExchange dialogue_playerCollision;

    EntityExpression exp;

    public Dummy() {
        super("Dummy", new SpriteSheet(ImageStore.get(ImageStore.DUMMY), 30, 44, 2, 4));
        setPosition(470, 132);

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 1.1;

        exp = new EntityExpression(EntityExpression.HEART, this);

        dialogue_playerCollision = new DialogueExchange(new CharacterDialogue(this, "dummy/player-collision.def"), new CharacterDialogue(Entities.PLAYER, "player/dummy-collision.def"));

        DialogueController.addFinished(new DialogueExchangeFinishedEvent(dialogue_playerCollision) {
            @Override
            public void onFinish() {
                isTalkingToPlayer = false;
            }
        });
    }

    public void onSectorEnter(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                EntityEntityCollision baseCollision = new EntityEntityCollision(boundaries.get(0), e.defaultBoundary) {
                    public void onCollide(EntityEntityCollisionEvent event) {
                        Util.onCollisionWithNonTraversableEntity(event, Entities.DUMMY);
                    }

                    public void onCollisionEnd(EntityEntityCollisionEvent event) {
                        if (!Entities.DUMMY.isCollided) {
                            allowAllMovement();
                        } else {
                            Util.annulCollisionEvent(event, Entities.DUMMY);
                        }
                    }
                };
                collisions.add(baseCollision);
            }
        }
    }

    public void onSectorLeave(MapSector sector) {
        // remove the generated collisions

        // prevents concurrent modification
        ArrayList<EntityEntityCollision> removeCollisions = new ArrayList<>();

        for (Entity e : sector.entities)
            if (!e.isTraversable && e.isSectorSpecific)
                for (EntityEntityCollision c : collisions)
                    if (c.entities[0].equals(e) || c.entities[1].equals(e))
                        removeCollisions.add(c);

        for (EntityEntityCollision c : removeCollisions)
            collisions.remove(c);

        removeCollisions.clear();
    }

    @Override
    public void tick() {
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        super.tick();

        walktime++;
        if (walktime >= 120) {
            walktime = 0;
            if (dir == DIRECTION_RIGHT) {
                dir = DIRECTION_LEFT;
            } else {
                dir = DIRECTION_RIGHT;
            }
        } else {
            move(dir);
        }

        if (!isMovingRightwards && !isMovingLeftwards && !isMovingUpwards && !isMovingDownwards) {
            animation.stop();
        }

        if (isTalkingToPlayer) {
            exp.tick();
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (isTalkingToPlayer) {
            exp.render(graphics);
        }
        super.render(graphics);
    }

    @Override
    protected void onMove(byte direction) {
        if (!animation.clock.isRunning) {
            // animation was stopped, so restart it because we're moving
            animation.start();
        }

        if (direction == DIRECTION_RIGHT && !animations.get(RIGHT_ANIMATION).clock.isRunning)
            switchAnimation(RIGHT_ANIMATION);
        else if (direction == DIRECTION_LEFT && !animations.get(LEFT_ANIMATION).clock.isRunning)
            switchAnimation(LEFT_ANIMATION);
    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0, 2, 0, 2)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(sprites.get(0), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(1), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    public void setCollisions() {
        collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.defaultBoundary) {
            public void onCollide(EntityEntityCollisionEvent event) {
                Game.in().dialogue.startExchange(dialogue_playerCollision);
                isTalkingToPlayer = true;
                Game.in().inventory.put(Item.CURRENT_YEAR, 99);
            }

            public void onCollisionEnd(EntityEntityCollisionEvent event) {

            }
        });
    }

    @Override
    public String getName() {
        return id;
    }

    @Override
    public BufferedImage getCharacterImage() {
        return image;
    }
}
