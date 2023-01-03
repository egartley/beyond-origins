package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.AnimatedEntity;
import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.map.MapSector;
import net.egartley.beyondorigins.engine.controllers.DialogueController;
import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.interfaces.Character;
import net.egartley.beyondorigins.engine.interfaces.Interactable;
import net.egartley.beyondorigins.engine.logic.collision.Collisions;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.engine.logic.dialogue.CharacterDialogue;
import net.egartley.beyondorigins.engine.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.engine.logic.events.DialogueFinishedEvent;
import net.egartley.beyondorigins.engine.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityInteraction;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Dummy extends AnimatedEntity implements Character, Interactable {

    private short walktime = 0;
    private boolean isTalkingToPlayer;
    private Direction dir = Direction.RIGHT;
    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int ANIMATION_THRESHOLD = 150;
    private final EntityExpression exp;
    private final DialogueExchange dialogue_playerCollision;

    public Dummy() {
        super("Dummy", new SpriteSheet(
                Images.getImage(Images.DUMMY), 30, 44, 2, 4));
        setPosition(470, 132);
        isSectorSpecific = false;
        speed = 1.1;
        exp = new EntityExpression(EntityExpression.HEART, this);
        dialogue_playerCollision = new DialogueExchange(
                new CharacterDialogue(this, "dummy/player-collision.def"),
                new CharacterDialogue(Entities.PLAYER, "player/dummy-collision.def"));
        DialogueController.addFinished(new DialogueFinishedEvent(dialogue_playerCollision) {
            @Override
            public void onFinish() {
                isTalkingToPlayer = false;
            }
        });
    }

    public void onSectorEnter(MapSector entering) {
        entering.addEntity(this);
        for (Entity e : entering.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                EntityEntityCollision baseCollision = new EntityEntityCollision(boundaries.get(0), e.defaultBoundary) {
                    public void start(EntityEntityCollisionEvent event) {
                        Util.onCollisionWithNonTraversableEntity(event, Entities.DUMMY);
                    }

                    public void end(EntityEntityCollisionEvent event) {
                        if (!Entities.DUMMY.isCollided) {
                            allowAllMovement();
                        } else {
                            Util.annulCollisionEvent(event, Entities.DUMMY);
                        }
                    }
                };
                Collisions.add(baseCollision);
            }
        }
    }

    public void onSectorLeave(MapSector leaving) {
        Collisions.removeAllWith(this);
        leaving.removeEntity(this);
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
            if (dir == Direction.RIGHT) {
                dir = Direction.LEFT;
            } else {
                dir = Direction.RIGHT;
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
    protected void onMove(Direction direction) {
        if (animation.isStopped()) {
            animation.start();
        }
        if (direction == Direction.RIGHT && !animations.get(RIGHT_ANIMATION).isStopped()) {
            switchAnimation(RIGHT_ANIMATION);
        } else if (direction == Direction.LEFT && !animations.get(LEFT_ANIMATION).isStopped()) {
            switchAnimation(LEFT_ANIMATION);
        }
    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite.width, sprite.height,
                new BoundaryPadding(0, 2, 0, 2)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(0)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(1)), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    public void setCollisions() {

    }

    @Override
    public void setInteractions() {
        interactions.add(new EntityEntityInteraction(defaultBoundary, Entities.PLAYER.chatBoundary) {
            @Override
            public void interact() {
                InGameState.dialogue.startExchange(dialogue_playerCollision);
                isTalkingToPlayer = true;
            }
        });
        interactions.get(0).activate();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getCharacterImage() {
        return image;
    }

}
