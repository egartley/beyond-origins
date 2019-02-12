package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.definitions.dialogue.DummyDialogue;
import net.egartley.beyondorigins.graphics.Animation;
import net.egartley.beyondorigins.graphics.EntityExpression;
import net.egartley.beyondorigins.graphics.Sprite;
import net.egartley.beyondorigins.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.AnimatedEntity;
import net.egartley.beyondorigins.objects.Character;
import net.egartley.beyondorigins.objects.Entity;
import net.egartley.beyondorigins.objects.MapSector;

import java.awt.*;
import java.util.ArrayList;

public class Dummy extends AnimatedEntity implements Character {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final byte ANIMATION_THRESHOLD = 9;

    private short walktime = 0;
    private byte dir = RIGHT;

    EntityExpression exp;

    public Dummy(ArrayList<Sprite> sprites) {
        super("Dummy");
        this.sprites = sprites;
        sprite = sprites.get(0);
        x = 470.0;
        y = 180.0;
        setAnimations();
        setBoundaries();
        setCollisions();

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 1.3;

        exp = new EntityExpression(EntityExpression.CONFUSION, this);
    }

    private void switchAnimation(byte i) {
        if (i >= animations.size()) {
            Debug.warning("Tried to switch to an animation at an invalid index");
            return;
        }
        if (!animation.equals(animations.get(i))) {
            // this prevents the same animation being set again
            animation = animations.get(i);
        }
    }

    public void onSectorEnter(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                EntityEntityCollision baseCollision = new EntityEntityCollision(boundaries.get(0), e.defaultBoundary) {
                    public void onCollide(EntityEntityCollisionEvent event) {
                        onCollisionWithNonTraversableEntity(event);
                    }

                    public void onCollisionEnd(EntityEntityCollisionEvent event) {
                        if (!Entities.DUMMY.isCollided) {
                            allowAllMovement();
                        } else {
                            annulCollisionEvent(event);
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
        if (walktime >= 90) {
            walktime = 0;
            if (dir == RIGHT) {
                dir = LEFT;
            } else {
                dir = RIGHT;
            }
        } else {
            move(dir);
        }

        if (!isMovingRightwards && !isMovingLeftwards && !isMovingUpwards && !isMovingDownwards)
            animation.stop();

        exp.tick();
    }

    @Override
    public void render(Graphics graphics) {
        exp.render(graphics);
        super.render(graphics);
    }

    @Override
    protected void onMove(byte direction) {
        if (animation.isStopped) {
            // animation was stopped, so restart it because we're moving
            animation.resume();
        }

        if (direction == RIGHT)
            switchAnimation(RIGHT_ANIMATION);
        else if (direction == LEFT)
            switchAnimation(LEFT_ANIMATION);
    }

    @Override
    public void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(4, 4, 0, 4)));
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
    protected void setCollisions() {
        collisions.clear();
        collisions.add(new EntityEntityCollision(defaultBoundary, Entities.PLAYER.defaultBoundary) {
            public void onCollide(EntityEntityCollisionEvent event) {
                if (Entities.DIALOGUE_PANEL.isShowing)
                    return;
                Entities.DIALOGUE_PANEL.setDialogue(DummyDialogue.CAUGHT_UP_WITH_PLAYER);
                Entities.DIALOGUE_PANEL.isShowing = true;
            }

            public void onCollisionEnd(EntityEntityCollisionEvent event) {
            }
        });
    }

}
