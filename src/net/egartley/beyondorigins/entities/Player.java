package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ingame.Building;
import net.egartley.gamelib.abstracts.AnimatedEntity;
import net.egartley.gamelib.abstracts.Entity;
import net.egartley.gamelib.abstracts.MapSector;
import net.egartley.gamelib.graphics.Animation;
import net.egartley.gamelib.graphics.SpriteSheet;
import net.egartley.gamelib.input.Keyboard;
import net.egartley.gamelib.interfaces.Character;
import net.egartley.gamelib.logic.collision.EntityEntityCollision;
import net.egartley.gamelib.logic.events.EntityEntityCollisionEvent;
import net.egartley.gamelib.logic.interaction.BoundaryOffset;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends AnimatedEntity implements Character {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int ANIMATION_THRESHOLD = 165;

    private boolean frozen;
    private boolean isMovementInvalidated;

    public EntityBoundary boundary;
    public EntityBoundary headBoundary;
    public EntityBoundary bodyBoundary;
    public EntityBoundary feetBoundary;

    public Player() {
        super("Player", new SpriteSheet(ImageStore.get(ImageStore.PLAYER), 30, 44, 2, 4));

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 1.1;
        if (Game.debug) {
            speed = 1.8;
        }
    }

    public void generateSectorSpecificCollisions(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                generateMovementRestrictionCollisions(e.defaultBoundary, boundary, headBoundary);
            }
        }
    }

    public void removeSectorSpecificCollisions(MapSector sector) {
        ArrayList<EntityEntityCollision> removeCollisions = new ArrayList<>();

        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                for (EntityEntityCollision c : collisions) {
                    if (c.entities[0].equals(e) || c.entities[1].equals(e)) {
                        removeCollisions.add(c);
                    }
                }
            }
        }

        for (EntityEntityCollision c : removeCollisions) {
            collisions.remove(c);
        }
    }

    public void generateMovementRestrictionCollisions(EntityBoundary otherBoundary, EntityBoundary... exclusions) {
        EntityEntityCollision baseCollision = new EntityEntityCollision(headBoundary, otherBoundary) {
            public void onCollide(EntityEntityCollisionEvent event) {
                Util.onCollisionWithNonTraversableEntity(event, Entities.PLAYER);
            }

            public void onCollisionEnd(EntityEntityCollisionEvent event) {
                boolean noMovementRestrictions = true;
                for (EntityEntityCollision c : Entities.PLAYER.concurrentCollisions) {
                    if (c.isMovementRestricting) {
                        noMovementRestrictions = false;
                        break;
                    }
                }
                if (!Entities.PLAYER.isCollided || noMovementRestrictions) {
                    allowAllMovement();
                } else {
                    Util.annulCollisionEvent(event, Entities.PLAYER);
                }
            }
        };
        baseCollision.isMovementRestricting = true;
        collisions.add(baseCollision);

        for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(baseCollision, this, otherBoundary)) {
            boolean add = true;
            for (EntityBoundary exclude : exclusions) {
                if (collision.boundaries[0] == exclude) {
                    add = false;
                    break;
                }
            }
            if (add) {
                collisions.add(collision);
            }
        }
    }

    public void deactivateBuildingCollisions() {
        for (EntityEntityCollision c : collisions) {
            if (c.boundaries[0].parent instanceof Building ||
                    c.boundaries[1].parent instanceof Building) {
                c.deactivate();
            }
        }
    }

    public void reactivateBuildingCollisions() {
        for (EntityEntityCollision c : collisions) {
            if (c.boundaries[0].parent instanceof Building ||
                    c.boundaries[1].parent instanceof Building) {
                c.activate();
            }
        }
    }

    public void invalidateAllMovement() {
        isMovementInvalidated = true;
    }

    public void freeze() {
        frozen = true;
        isAllowedToMoveUpwards = false;
        isAllowedToMoveDownwards = false;
        isAllowedToMoveLeftwards = false;
        isAllowedToMoveRightwards = false;
    }

    public void thaw() {
        frozen = false;
        isAllowedToMoveUpwards = true;
        isAllowedToMoveDownwards = true;
        isAllowedToMoveLeftwards = true;
        isAllowedToMoveRightwards = true;
    }

    public void enteredBuilding() {
        removeSectorSpecificCollisions(Game.in().map.sector);
        deactivateBuildingCollisions();
        // invalidateAllMovement();
    }

    public void leftBuilding(Building building) {
        generateSectorSpecificCollisions(Game.in().map.sector);
        setPosition(building.playerLeaveX, building.playerLeaveY);
        reactivateBuildingCollisions();
        // invalidateAllMovement();
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(sprites.get(0), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(1), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    public void setBoundaries() {
        boundary = new EntityBoundary(this, sprite, new BoundaryPadding(4, 3, 2, 3));
        boundary.name = "Base";
        defaultBoundary = boundary;
        headBoundary = new EntityBoundary(this, 19, 18, new BoundaryPadding(0), new BoundaryOffset(0, 0, 0, 5));
        headBoundary.name = "Head";
        bodyBoundary = new EntityBoundary(this, 30, 17, new BoundaryPadding(0), new BoundaryOffset(0, 18, 0, 0));
        bodyBoundary.name = "Body";
        feetBoundary = new EntityBoundary(this, 17, 16, new BoundaryPadding(0), new BoundaryOffset(0, 29, 0, 6));
        feetBoundary.name = "Feet";

        boundaries.add(boundary);
        boundaries.add(headBoundary);
        boundaries.add(bodyBoundary);
        boundaries.add(feetBoundary);
    }

    @Override
    public void setCollisions() {

    }

    @Override
    public void tick() {
        // get keyboard input (typical WASD for now, make configurable in future)
        boolean up = Keyboard.isKeyPressed(KeyEvent.VK_W) && !isMovementInvalidated;
        boolean left = Keyboard.isKeyPressed(KeyEvent.VK_A) && !isMovementInvalidated;
        boolean down = Keyboard.isKeyPressed(KeyEvent.VK_S) && !isMovementInvalidated;
        boolean right = Keyboard.isKeyPressed(KeyEvent.VK_D) && !isMovementInvalidated;

        if (!frozen) {
            move(up, down, left, right);
        }

        if (!left && !right && !down && !up) {
            // not moving, so stop the animation if it's not already stopped
            animation.stop();
        }

        // this makes it so that the user has to re-press any keys already being pressed in order to move the player again
        if (isMovementInvalidated) {
            int[] codes = new int[]{KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D};
            for (int keyCode : codes) {
                // invalidate only the keys that are currently pressed down
                if (Keyboard.isKeyPressed(keyCode)) {
                    Keyboard.invalidateKey(keyCode);
                }
            }
            isMovementInvalidated = false;
        }

        super.tick();
    }

    private void move(boolean up, boolean down, boolean left, boolean right) {
        if (!animation.clock.isRunning && (up || down || left || right)) {
            // animation was stopped, so restart it because we're moving
            animation.start();
            animation.setFrame(1);
        }

        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;

        if (up) {
            if (isAllowedToMoveUpwards) {
                move(DIRECTION_UP);
            }
        } else if (down) {
            if (isAllowedToMoveDownwards) {
                move(DIRECTION_DOWN);
            }
        }

        if (left) {
            if (isAllowedToMoveLeftwards) {
                move(DIRECTION_LEFT);
            }
            switchAnimation(LEFT_ANIMATION);
        } else if (right) {
            if (isAllowedToMoveRightwards) {
                move(DIRECTION_RIGHT);
            }
            switchAnimation(RIGHT_ANIMATION);
        }
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
