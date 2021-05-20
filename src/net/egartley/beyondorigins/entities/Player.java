package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.AnimatedEntity;
import net.egartley.beyondorigins.core.abstracts.Cutscene;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.core.abstracts.MapSector;
import net.egartley.beyondorigins.core.controllers.KeyboardController;
import net.egartley.beyondorigins.core.enums.Direction;
import net.egartley.beyondorigins.core.graphics.SpriteSheet;
import net.egartley.beyondorigins.core.input.KeyTyped;
import net.egartley.beyondorigins.core.input.Keyboard;
import net.egartley.beyondorigins.core.interfaces.Attacker;
import net.egartley.beyondorigins.core.interfaces.Character;
import net.egartley.beyondorigins.core.interfaces.Damageable;
import net.egartley.beyondorigins.core.logic.collision.Collisions;
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
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

public class Player extends AnimatedEntity implements Character, Damageable, Attacker {

    private boolean frozen;
    private boolean isMovementInvalidated;
    private final int MAX_LEVEL = 100;
    private final int DEFAULT_DAMAGE = 4;
    private final int DEFAULT_HEALTH = 30;
    private final int DEFAULT_DEFENSE = 1;
    private final byte LEFT_ANIMATION = 0;
    private final byte RIGHT_ANIMATION = 1;
    private final int MAX_EXPERIENCE = 4900601;
    private final int ANIMATION_THRESHOLD = 165;

    public int level = 1;
    public int experience = 0;
    public boolean isInBuilding;
    public KeyTyped attack;
    public EntityBoundary boundary;
    public EntityInventory inventory;
    public EntityBoundary headBoundary;
    public EntityBoundary bodyBoundary;
    public EntityBoundary feetBoundary;
    public EntityBoundary chatBoundary;
    public EntityBoundary attackBoundary;

    public Player() {
        super("Player", new SpriteSheet(Images.get(Images.PLAYER), 30, 44, 2, 4));
        isSectorSpecific = false;
        isDualRendered = false;
        speed = 2;
        health = 30;
        maximumHealth = health;
        inventory = new EntityInventory(this, 20) {
            @Override
            public void onUpdate() {
                PlayerInventory.populate();
            }
        };
        attack = new KeyTyped(Input.KEY_ENTER) {
            @Override
            public void onType() {
                attack();
            }
        };
    }

    private void move(boolean up, boolean down, boolean left, boolean right) {
        if (animation.isStopped() && (up || down || left || right)) {
            // animation was stopped, so restart it because we're moving
            animation.start();
            animation.setCurrentFrame(1);
        } else if (animation.isStopped()) {
            animation.setCurrentFrame(0);
        }
        isMovingUpwards = false;
        isMovingDownwards = false;
        isMovingLeftwards = false;
        isMovingRightwards = false;
        if (up) {
            if (isAllowedToMoveUpwards) {
                move(Direction.UP, defaultBoundary, true, true);
            }
        } else if (down) {
            if (isAllowedToMoveDownwards) {
                move(Direction.DOWN, defaultBoundary, true, true);
            }
        }
        if (left) {
            if (isAllowedToMoveLeftwards) {
                move(Direction.LEFT, defaultBoundary, true, true);
            }
            switchAnimation(LEFT_ANIMATION);
        } else if (right) {
            if (isAllowedToMoveRightwards) {
                move(Direction.RIGHT, defaultBoundary, true, true);
            }
            switchAnimation(RIGHT_ANIMATION);
        }
    }

    private int getExperienceNeededForNextLevel() {
        // 100 needed to get level 2, 205 needed for level 3, 310 needed for 4, and so on
        int n = level - 1;
        return 100 + (100 * n) + (5 * n);
    }

    private void levelUp() {
        if (level < MAX_LEVEL) {
            level++;
        }
        if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }
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
     * Makes the player immovable
     */
    public void freeze() {
        frozen = true;
        isAllowedToMoveUpwards = false;
        isAllowedToMoveDownwards = false;
        isAllowedToMoveLeftwards = false;
        isAllowedToMoveRightwards = false;
    }

    public void enteredBuilding() {
        isInBuilding = true;
        // invalidateAllMovement();
    }

    public void onInGameEnter() {
        KeyboardController.addKeyTyped(attack);
    }

    public void onInGameLeave() {
        KeyboardController.removeKeyTyped(attack);
    }

    public void onSectorEnter(MapSector sector) {
        if (sector instanceof Cutscene) {
            freeze();
        }
    }

    public void onSectorLeave(MapSector sector) {
        if (sector instanceof Cutscene) {
            thaw();
        }
    }

    public void leftBuilding(Building building) {
        generateSectorSpecificCollisions(InGameState.map.sector);
        building.setCollisions();
        InGameState.map.sector.setSpecialCollisions();
        setPosition(building.playerLeaveX, building.playerLeaveY);
        isInBuilding = false;
        // invalidateAllMovement();
    }

    public void invalidateAllMovement() {
        isMovementInvalidated = true;
    }

    public void generateMovementRestrictionCollisions(EntityBoundary otherBoundary) {
        EntityBoundary[] exclusions = new EntityBoundary[]{boundary, headBoundary, chatBoundary, attackBoundary};
        EntityEntityCollision baseCollision = new EntityEntityCollision(headBoundary, otherBoundary) {
            public void start(EntityEntityCollisionEvent event) {
                Util.onCollisionWithNonTraversableEntity(event, Entities.PLAYER);
            }

            public void end(EntityEntityCollisionEvent event) {
                boolean noMovementRestrictions = true;
                for (EntityEntityCollision c : Collisions.concurrent(Entities.PLAYER)) {
                    if (c.isMovementRestricting) {
                        noMovementRestrictions = false;
                        break;
                    }
                }
                if (noMovementRestrictions) {
                    allowAllMovement();
                } else {
                    Util.annulCollisionEvent(event, Entities.PLAYER);
                }
            }
        };
        baseCollision.isMovementRestricting = true;
        Collisions.add(baseCollision);
        for (EntityEntityCollision collision : Util.getAllBoundaryCollisions(baseCollision, this, otherBoundary)) {
            boolean add = true;
            for (EntityBoundary exclude : exclusions) {
                if (collision.boundaries[0] == exclude || collision.boundaries[1] == exclude) {
                    add = false;
                    break;
                }
            }
            if (add) {
                Collisions.add(collision);
            }
        }
    }

    public void generateMovementRestrictionCollisions(EntityBoundary... otherBoundaries) {
        for (EntityBoundary b : otherBoundaries) {
            generateMovementRestrictionCollisions(b);
        }
    }

    /**
     * Generate collisions with sector entities that aren't traversable
     *
     * @param sector The sector to generate collisions for its non-traversable entities
     */
    public void generateSectorSpecificCollisions(MapSector sector) {
        generateSectorSpecificCollisions(sector, false);
    }

    /**
     * Generate collisions with sector entities that aren't traversable
     *
     * @param sector The sector to generate collisions for its non-traversable entities
     */
    public void generateSectorSpecificCollisions(MapSector sector, boolean allBoundaries) {
        for (Entity e : sector.entities) {
            if (!e.isTraversable && e.isSectorSpecific) {
                if (!allBoundaries) {
                    generateMovementRestrictionCollisions(e.defaultBoundary);
                } else {
                    e.boundaries.forEach(this::generateMovementRestrictionCollisions);
                }
            }
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
            levelUp();
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(0)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(1)), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    public void setBoundaries() {
        boundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(4, 3, 2, 3));
        boundary.name = "Base";
        defaultBoundary = boundary;
        headBoundary = new EntityBoundary(this, 19, 18, new BoundaryPadding(0), new BoundaryOffset(0, 0, 0, 5));
        headBoundary.name = "Head";
        bodyBoundary = new EntityBoundary(this, 30, 17, new BoundaryPadding(0), new BoundaryOffset(0, 18, 0, 0));
        bodyBoundary.name = "Body";
        feetBoundary = new EntityBoundary(this, 17, 16, new BoundaryPadding(0), new BoundaryOffset(0, 29, 0, 6));
        feetBoundary.name = "Feet";
        chatBoundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(36));
        chatBoundary.name = "Chat";
        chatBoundary.isVisible = false;
        attackBoundary = new EntityBoundary(this, sprite.width, sprite.height, new BoundaryPadding(18));
        attackBoundary.name = "Attack";
        boundaries.add(boundary);
        boundaries.add(headBoundary);
        boundaries.add(bodyBoundary);
        boundaries.add(feetBoundary);
        boundaries.add(chatBoundary);
        boundaries.add(attackBoundary);
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
        if (!left && !right && !down && !up && !animation.isStopped()) {
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

    @Override
    public void attack() {
        for (EntityEntityCollision c : Collisions.concurrent(this)) {
            Entity e = c.entities[0] != this ? c.entities[0] : c.entities[1];
            if (e instanceof Damageable) {
                ((Damageable) e).takeDamage(DEFAULT_DAMAGE);
                InGameState.map.sector.addEntity(new DamageNumber(DEFAULT_DAMAGE, e));
            }
        }
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    @Override
    public void heal(int amount) {
        health += amount;
        if (health > maximumHealth) {
            health = maximumHealth;
        }
    }

    @Override
    public void onDeath() {
    }

    @Override
    public void onColdDeath() {
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
