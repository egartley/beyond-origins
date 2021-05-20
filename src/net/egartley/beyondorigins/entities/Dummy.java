package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.core.controllers.DialogueController;
import net.egartley.beyondorigins.core.enums.Direction;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.interfaces.Character;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.dialogue.CharacterDialogue;
import net.egartley.beyondorigins.core.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.core.logic.events.DialogueFinishedEvent;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.core.logic.interaction.EntityEntityInteraction;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Dummy extends AnimatedEntity implements Character {

    private short walktime = 0;
    private boolean isTalkingToPlayer;
    private Direction dir = Direction.RIGHT;
    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int ANIMATION_THRESHOLD = 150;
    private final EntityExpression exp;
    private final DialogueExchange dialogue_playerCollision;

    public Dummy() {
        super("Dummy", new SpriteSheet(Images.get(Images.DUMMY), 30, 44, 2, 4));
        setPosition(470, 132);
        isSectorSpecific = false;
        isDualRendered = false;
        speed = 1.1;
        exp = new EntityExpression(EntityExpression.HEART, this);
        dialogue_playerCollision = new DialogueExchange(new CharacterDialogue(this, "dummy/player-collision.def"), new CharacterDialogue(Entities.PLAYER, "player/dummy-collision.def"));
        DialogueController.addFinished(new DialogueFinishedEvent(dialogue_playerCollision) {
            @Override
            public void onFinish() {
                isTalkingToPlayer = false;
            }
        });
    }

    public void onSectorEnter(MapSector entering) {
        entering.addEntity(this, true);
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
        Collisions.removeWith(this);
        leaving.removeEntity(this, true);
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
        boundaries.add(new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(0, 2, 0, 2)));
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
