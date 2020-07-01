package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.core.graphics.Animation;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.input.Keyboard;
import net.egartley.beyondorigins.core.interfaces.Character;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryOffset;
import net.egartley.beyondorigins.core.logic.interaction.BoundaryPadding;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.core.logic.inventory.EntityInventory;
import net.egartley.beyondorigins.core.ui.PlayerInventory;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.beyondorigins.ingame.Building;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import java.util.ArrayList;

public class Player extends AnimatedEntity implements Character {

    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int ANIMATION_THRESHOLD = 165;
    private final int MAX_LEVEL = 100;
    private final int MAX_EXPERIENCE = 10390; // 100 + (100 * 98) + (5 * 98)

    private boolean frozen;
    private boolean isMovementInvalidated;

    public int level = 1;
    public int experience = 0;
    public boolean isInBuilding;

    public EntityBoundary boundary;
    public EntityBoundary headBoundary;
    public EntityBoundary bodyBoundary;
    public EntityBoundary feetBoundary;
    public EntityBoundary chatBoundary;

    public Player() {
        super("Player", new SpriteSheet(Images.get(Images.PLAYER), 30, 44, 2, 4));

        isSectorSpecific = false;
        isDualRendered = false;
        speed = 1.1;
        if (Game.debug) {
            speed = 1.8;
        }

        inventory = new EntityInventory(this, 20) {
            @Override
            public void onUpdate() {
                PlayerInventory.populate();
            }
        };
    }

    public void generateSectorSpecificCollisions(MapSector sector) {
        // generate collisions with sector entities that aren't traversable
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                generateMovementRestrictionCollisions(e.defaultBoundary, boundary, headBoundary, chatBoundary);
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
            public void start(EntityEntityCollisionEvent event) {
                Util.onCollisionWithNonTraversableEntity(event, Entities.PLAYER);
            }

            public void end(EntityEntityCollisionEvent event) {
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
            if (c.boundaries[0].parent instanceof Building || c.boundaries[1].parent instanceof Building) {
                c.deactivate();
            }
        }
    }

    public void reactivateBuildingCollisions() {
        for (EntityEntityCollision c : collisions) {
            if (c.boundaries[0].parent instanceof Building || c.boundaries[1].parent instanceof Building) {
                c.activate();
            }
        }
    }

    public void invalidateAllMovement() {
        isMovementInvalidated = true;
    }

    /**
     * Makes the player immovable
     */
    public void freeze() {
        frozen = true;
        isAllowedToMoveUpwards = false;
        isAllowedToMoveDownwards = false;
        isAllowedToMoveLeftwards = false;
        isAllowedToMoveRightwards = false;
    }

    /**
     * Restores the player's ability to move
     */
    public void thaw() {
        frozen = false;
        isAllowedToMoveUpwards = true;
        isAllowedToMoveDownwards = true;
        isAllowedToMoveLeftwards = true;
        isAllowedToMoveRightwards = true;
    }

    /**
     * Called when the player enters a building
     */
    public void enteredBuilding() {
        isInBuilding = true;
        removeSectorSpecificCollisions(InGameState.map.sector);
        deactivateBuildingCollisions();
        // invalidateAllMovement();
    }

    /**
     * Called when a player leaves a building
     *
     * @param building The building that was just left
     */
    public void leftBuilding(Building building) {
        generateSectorSpecificCollisions(InGameState.map.sector);
        setPosition(building.playerLeaveX, building.playerLeaveY);
        reactivateBuildingCollisions();
        isInBuilding = false;
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
        chatBoundary = new EntityBoundary(this, sprite, new BoundaryPadding(36));
        chatBoundary.name = "Chat";
        chatBoundary.isVisible = false;

        boundaries.add(boundary);
        boundaries.add(headBoundary);
        boundaries.add(bodyBoundary);
        boundaries.add(feetBoundary);
        boundaries.add(chatBoundary);
    }

    @Override
    public void setCollisions() {

    }

    @Override
    public void setInteractions() {

    }

    @Override
    public void tick() {
        // get keyboard input (typical WASD for now, make configurable in future)
        boolean up = Game.input.isKeyDown(Input.KEY_W) && !isMovementInvalidated;
        boolean left = Game.input.isKeyDown(Input.KEY_A) && !isMovementInvalidated;
        boolean down = Game.input.isKeyDown(Input.KEY_S) && !isMovementInvalidated;
        boolean right = Game.input.isKeyDown(Input.KEY_D) && !isMovementInvalidated;

        if (!frozen) {
            move(up, down, left, right);
        }

        if (!left && !right && !down && !up) {
            // not moving, so stop the animation if it's not already stopped
            animation.stop();
        }

        // this makes it so that the user has to re-press any keys already being pressed in order to move the player again
        if (isMovementInvalidated) {
            for (int keyCode : new int[]{Input.KEY_W, Input.KEY_A, Input.KEY_S, Input.KEY_D}) {
                // invalidate only the keys that are currently pressed down
                if (Keyboard.isPressed(keyCode)) {
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

    private int getExperienceNeededForNextLevel() {
        // 100 needed to get level 2, 205 needed for level 3, 310 needed for 4, and so on
        int n = level - 1;
        return 100 + (100 * n) + (5 * n);
    }

    private void nextLevel() {
        if (level < MAX_LEVEL) {
            level++;
        }
        if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }
    }

    public void giveExperience(int amount) {
        if (experience < MAX_EXPERIENCE) {
            experience += amount;
        }
        if (experience > MAX_EXPERIENCE) {
            experience = MAX_EXPERIENCE;
        }
        if (experience >= getExperienceNeededForNextLevel()) {
            nextLevel();
        }
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
