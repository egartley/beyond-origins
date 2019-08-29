package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
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

    private boolean isAngry;
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
                isAngry = false;
            }
        });
    }

    private void switchAnimation(byte i) {
        if (i >= animations.size()) {
            Debug.warning("Tried to switch to an animation at an invalid index");
            return;
        }
        if (!animation.equals(animations.get(i))) {
            // this prevents the same animation being set again
            animation.stop(false);
            animation = animations.get(i);
        }
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

        if (isAngry) {
            exp.tick();
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (isAngry) {
            exp.render(graphics);
        }
        super.render(graphics);
        if (Game.debug) {
            drawDebug(graphics);
        }
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
        animations.clear();
        animations.add(new Animation(sprites.get(0), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(1), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    public void setCollisions() {
        collisions.clear();
        collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.defaultBoundary) {
            public void onCollide(EntityEntityCollisionEvent event) {
                if (Game.in().dialoguePanel.isShowing) {
                    return;
                }
                Game.in().dialoguePanel.exchange = dialogue_playerCollision;
                Game.in().dialoguePanel.show();
                isAngry = true;

                Game.in().inventory.put(Item.WOJAK);
                Game.in().inventory.put(Item.HONKLER);
                Game.in().inventory.put(Item.BOOMER);
                Game.in().inventory.put(Item.ZOOMER);
                Game.in().inventory.put(Item.BIG_CHUNGUS);
                Game.in().inventory.put(Item.THE_ZUCC);
                Game.in().inventory.put(Item.BITCONNECT);
                Game.in().inventory.put(Item.AINT_CLICKIN_THAT);
                Game.in().inventory.put(Item.CURRENT_YEAR);
                Game.in().inventory.put(Item.TUCKER);
                Game.in().inventory.put(Item.HMM);
            }

            public void onCollisionEnd(EntityEntityCollisionEvent event) {

            }
        });
    }

    @Override
    public String getName() {
        return "Dummy";
    }

    @Override
    public BufferedImage getDialoguePanelImage() {
        return sprite.toBufferedImage(0);
    }
}
