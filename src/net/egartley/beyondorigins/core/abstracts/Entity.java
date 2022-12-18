package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.enums.Direction;
import net.egartley.beyondorigins.core.interfaces.Damageable;
import net.egartley.beyondorigins.core.interfaces.Renderable;
import net.egartley.beyondorigins.core.interfaces.Tickable;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.collision.EntityEntityCollision;
import net.egartley.beyondorigins.core.logic.events.EntityEntityCollisionEvent;
import net.egartley.beyondorigins.core.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.core.logic.interaction.EntityEntityInteraction;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.util.ArrayList;

public abstract class Entity implements Tickable, Renderable {

    private final int HEALTH_BAR_WIDTH = 64;
    private static final Color HEALTH_BAR_COLOR = new Color(0, 179, 0);
    private static final Color NAME_TAG_BACKGROUND_COLOR = new Color(0, 0, 0, 128);
    private static final Font DEBUG_FONT =
            new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11), true);

    protected boolean isMovingUpwards;
    protected boolean isMovingDownwards;
    protected boolean isMovingLeftwards;
    protected boolean isMovingRightwards;

    public int x, y;
    public int uuid;
    public int width;
    public int height;
    public int health;
    public int maxHealth;
    public double speed;
    public double deltaX;
    public double deltaY;
    public boolean isCollided;
    public boolean isTraversable;
    public boolean isSectorSpecific;
    public boolean isAbleToCollide = true;
    public boolean isAllowedToMoveUpwards = true;
    public boolean isAllowedToMoveDownwards = true;
    public boolean isAllowedToMoveLeftwards = true;
    public boolean isAllowedToMoveRightwards = true;
    public String name;
    public EntityBoundary defaultBoundary = null;
    public EntityEntityCollision lastCollision = null;
    public EntityEntityCollisionEvent lastCollisionEvent = null;
    public ArrayList<EntityBoundary> boundaries = new ArrayList<>();
    public ArrayList<EntityEntityInteraction> interactions = new ArrayList<>();

    public Entity(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        speed = 1.0;
        health = 50;
        maxHealth = health;
        uuid = Util.randomInt(999999, 100000, true);
    }

    protected abstract void setCollisions();

    protected abstract void setBoundaries();

    protected abstract void setInteractions();

    public void drawDebug(Graphics graphics) {
        drawBoundaries(graphics);
        drawNameTag(graphics);
        if (this instanceof Damageable) {
            drawHealthBar(graphics);
        }
    }

    private void drawNameTag(Graphics graphics) {
        int width = DEBUG_FONT.getWidth(toString()) + 8;
        int nameX = (x + (this.width / 2)) - width / 2;
        int nameY = y - 18;
        graphics.setColor(NAME_TAG_BACKGROUND_COLOR);
        graphics.setFont(DEBUG_FONT);
        graphics.fillRect(nameX, nameY, width, 18);
        graphics.setColor(Color.white);
        graphics.drawString(toString(), nameX + 5, nameY + 16 - DEBUG_FONT.getLineHeight());
    }

    private void drawHealthBar(Graphics graphics) {
        int baseX = (x + (width / 2)) - HEALTH_BAR_WIDTH / 2;
        int baseY = y - 30;
        graphics.setColor(Color.black);
        graphics.fillRect(baseX - 1, baseY - 1, HEALTH_BAR_WIDTH + 2, 8);
        graphics.setColor(Color.red);
        graphics.fillRect(baseX, baseY, HEALTH_BAR_WIDTH, 6);
        graphics.setColor(HEALTH_BAR_COLOR);
        graphics.fillRect(baseX, baseY, health * (HEALTH_BAR_WIDTH) / (maxHealth), 6);
    }

    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.render(graphics));
    }

    private void x(int x, boolean setDelta) {
        this.x = x;
        if (setDelta) {
            deltaX = x;
        }
    }

    private void y(int y, boolean setDelta) {
        this.y = y;
        if (setDelta) {
            deltaY = y;
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (Game.debug) {
            drawDebug(graphics);
        }
    }

    @Override
    public void tick() {
        boundaries.forEach(EntityBoundary::tick);
        interactions.forEach(EntityEntityInteraction::tick);
    }

    public void setPosition(int x, int y) {
        x(x);
        y(y);
    }

    public void x(int x) {
        x(x, true);
    }

    public void y(int y) {
        y(y, true);
    }

    protected void onMove(Direction direction) {

    }

    protected void move(Direction direction) {
        move(direction, defaultBoundary, true, false);
    }

    protected void move(Direction direction, boolean resetIsMoving) {
        move(direction, defaultBoundary, resetIsMoving, false);
    }

    protected void move(Direction direction, EntityBoundary boundary, boolean resetIsMoving) {
        move(direction, boundary, resetIsMoving, false);
    }

    protected void move(Direction direction, EntityBoundary boundary, boolean reserIsMoving, boolean containToWindow) {
        if (reserIsMoving) {
            isMovingUpwards = false;
            isMovingDownwards = false;
            isMovingLeftwards = false;
            isMovingRightwards = false;
        }
        if (direction == Direction.UP && !isAllowedToMoveUpwards)
            return;
        if (direction == Direction.DOWN && !isAllowedToMoveDownwards)
            return;
        if (direction == Direction.LEFT && !isAllowedToMoveLeftwards)
            return;
        if (direction == Direction.RIGHT && !isAllowedToMoveRightwards)
            return;
        switch (direction) {
            case UP -> {
                if (containToWindow && boundary.top <= 0) {
                    break; // top of window
                }
                isMovingUpwards = true;
                deltaY -= speed;
                y -= (int) Math.abs(deltaY - y);
                onMove(Direction.UP);
            }
            case DOWN -> {
                if (containToWindow && boundary.bottom >= Game.WINDOW_HEIGHT) {
                    break; // bottom of window
                }
                isMovingDownwards = true;
                deltaY += speed;
                y += (int) Math.abs(deltaY - y);
                onMove(Direction.DOWN);
            }
            case LEFT -> {
                if (containToWindow && boundary.left <= 0) {
                    break; // left side of window
                }
                isMovingLeftwards = true;
                deltaX -= speed;
                x -= (int) Math.abs(deltaX - x);
                onMove(Direction.LEFT);
            }
            case RIGHT -> {
                if (containToWindow && boundary.right >= Game.WINDOW_WIDTH) {
                    break; // right side of window
                }
                isMovingRightwards = true;
                deltaX += speed;
                x += (int) Math.abs(deltaX - x);
                onMove(Direction.RIGHT);
            }
        }
    }

    public void follow(Entity toFollow) {
        follow(toFollow, defaultBoundary);
    }

    public void follow(Entity toFollow, EntityBoundary boundary) {
        follow(toFollow, boundary, 1);
    }

    public void follow(Entity toFollow, EntityBoundary boundary, int tolerance) {
        if (!Calculate.isPointWithinTolerance(x, toFollow.x, tolerance)) {
            if (isMovingRightwards && this.isRightOf(toFollow)) {
                isMovingRightwards = false;
                isMovingLeftwards = true;
            } else if (isMovingLeftwards && this.isLeftOf(toFollow)) {
                isMovingLeftwards = false;
                isMovingRightwards = true;
            }
        } else {
            isMovingLeftwards = false;
            isMovingRightwards = false;
        }
        if (!Calculate.isPointWithinTolerance(y, toFollow.y, tolerance)) {
            if (this.isBelow(toFollow)) {
                isMovingDownwards = false;
                isMovingUpwards = true;
            } else if (this.isAbove(toFollow)) {
                isMovingDownwards = true;
                isMovingUpwards = false;
            }
        } else {
            isMovingUpwards = false;
            isMovingDownwards = false;
        }
        if (isMovingRightwards) {
            move(Direction.RIGHT, boundary, false);
        } else if (isMovingLeftwards) {
            move(Direction.LEFT, boundary, false);
        }
        if (isMovingDownwards) {
            move(Direction.DOWN, boundary, false);
        } else if (isMovingUpwards) {
            move(Direction.UP, boundary, false);
        }
    }

    protected void allowAllMovement() {
        isAllowedToMoveUpwards = true;
        isAllowedToMoveDownwards = true;
        isAllowedToMoveLeftwards = true;
        isAllowedToMoveRightwards = true;
    }

    public boolean isRightOf(Entity e) {
        return x > e.x + e.width;
    }

    public boolean isLeftOf(Entity e) {
        return x < e.x;
    }

    public boolean isAbove(Entity e) {
        return y < e.y;
    }

    public boolean isBelow(Entity e) {
        return y > e.y + e.height;
    }

    public String toString() {
        return name + "#" + uuid;
    }

}
