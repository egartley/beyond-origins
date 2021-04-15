package net.egartley.beyondorigins.core.abstracts;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.interfaces.Damageable;
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

public abstract class Entity extends Renderable implements Tickable {

    private final int healthBarWidth = 64;
    private static final Color healthBarColor = new Color(0, 179, 0);
    private static final Color nameTagBackgroundColor = new Color(0, 0, 0, 128);
    private static final Font debugFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11), true);

    protected boolean isMovingUpwards;
    protected boolean isMovingDownwards;
    protected boolean isMovingLeftwards;
    protected boolean isMovingRightwards;

    public int uuid;
    public int width;
    public int health;
    public int height;
    public int maximumHealth;
    public double speed;
    public double deltaX;
    public double deltaY;
    public boolean isCollided;
    public boolean isTraversable;
    public boolean isSectorSpecific;
    public boolean canCollide = true;
    public boolean isAllowedToMoveUpwards = true;
    public boolean isAllowedToMoveDownwards = true;
    public boolean isAllowedToMoveLeftwards = true;
    public boolean isAllowedToMoveRightwards = true;
    public static final byte DIRECTION_UP = 1;
    public static final byte DIRECTION_DOWN = 2;
    public static final byte DIRECTION_LEFT = 3;
    public static final byte DIRECTION_RIGHT = 4;
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
        maximumHealth = health;
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
        int width = debugFont.getWidth(toString()) + 8;
        int nameX = (x() + (this.width / 2)) - width / 2;
        int nameY = y() - 18;
        graphics.setColor(nameTagBackgroundColor);
        graphics.setFont(debugFont);
        graphics.fillRect(nameX, nameY, width, 18);
        graphics.setColor(Color.white);
        graphics.drawString(toString(), nameX + 5, nameY + 16 - debugFont.getLineHeight());
    }

    private void drawHealthBar(Graphics graphics) {
        int baseX = (x() + (width / 2)) - healthBarWidth / 2;
        int baseY = y() - 30;
        graphics.setColor(Color.black);
        graphics.fillRect(baseX - 1, baseY - 1, healthBarWidth + 2, 8);
        graphics.setColor(Color.red);
        graphics.fillRect(baseX, baseY, healthBarWidth, 6);
        graphics.setColor(healthBarColor);
        graphics.fillRect(baseX, baseY, health * (healthBarWidth) / (maximumHealth), 6);
    }

    private void drawBoundaries(Graphics graphics) {
        boundaries.forEach(boundary -> boundary.render(graphics));
    }

    private void x(int x, boolean setDelta) {
        super.x(x);
        if (setDelta) {
            deltaX = x;
        }
    }

    private void y(int y, boolean setDelta) {
        super.y(y);
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

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        deltaX = x;
        deltaY = y;
    }

    @Override
    public void x(int x) {
        x(x, true);
    }

    @Override
    public void y(int y) {
        y(y, true);
    }

    protected void onMove(byte direction) {

    }

    protected void move(byte direction) {
        move(direction, defaultBoundary, true, false);
    }

    protected void move(byte direction, boolean reset) {
        move(direction, defaultBoundary, reset, false);
    }

    protected void move(byte direction, EntityBoundary boundary, boolean reset) {
        move(direction, boundary, reset, false);
    }

    protected void move(byte direction, EntityBoundary boundary, boolean reset, boolean contain) {
        if (reset) {
            isMovingUpwards = false;
            isMovingDownwards = false;
            isMovingLeftwards = false;
            isMovingRightwards = false;
        }
        if (direction == DIRECTION_UP && !isAllowedToMoveUpwards)
            return;
        if (direction == DIRECTION_DOWN && !isAllowedToMoveDownwards)
            return;
        if (direction == DIRECTION_LEFT && !isAllowedToMoveLeftwards)
            return;
        if (direction == DIRECTION_RIGHT && !isAllowedToMoveRightwards)
            return;
        if (direction == -1)
            return;
        switch (direction) {
            case DIRECTION_UP:
                if (contain && boundary.top <= 0) {
                    break; // top of window
                }
                isMovingUpwards = true;
                deltaY -= speed;
                y(y() - (int) Math.abs(deltaY - y()), false);
                onMove(DIRECTION_UP);
                break;
            case DIRECTION_DOWN:
                if (contain && boundary.bottom >= Game.WINDOW_HEIGHT) {
                    break; // bottom of window
                }
                isMovingDownwards = true;
                deltaY += speed;
                y(y() + (int) Math.abs(deltaY - y()), false);
                onMove(DIRECTION_DOWN);
                break;
            case DIRECTION_LEFT:
                if (contain && boundary.left <= 0) {
                    break; // left side of window
                }
                isMovingLeftwards = true;
                deltaX -= speed;
                x(x() - (int) Math.abs(deltaX - x()), false);
                onMove(DIRECTION_LEFT);
                break;
            case DIRECTION_RIGHT:
                if (contain && boundary.right >= Game.WINDOW_WIDTH) {
                    break; // right side of window
                }
                isMovingRightwards = true;
                deltaX += speed;
                x(x() + (int) Math.abs(deltaX - x()), false);
                onMove(DIRECTION_RIGHT);
                break;
            default:
                break;
        }
    }

    public void follow(Entity toFollow) {
        follow(toFollow, defaultBoundary);
    }

    public void follow(Entity toFollow, EntityBoundary boundary) {
        follow(toFollow, boundary, 1);
    }

    public void follow(Entity toFollow, EntityBoundary boundary, int tolerance) {
        if (!Calculate.isWithinToleranceOf(this.x(), toFollow.x(), tolerance)) {
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
        if (!Calculate.isWithinToleranceOf(this.y(), toFollow.y(), tolerance)) {
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
            move(DIRECTION_RIGHT, boundary, false);
        } else if (isMovingLeftwards) {
            move(DIRECTION_LEFT, boundary, false);
        }
        if (isMovingDownwards) {
            move(DIRECTION_DOWN, boundary, false);
        } else if (isMovingUpwards) {
            move(DIRECTION_UP, boundary, false);
        }
    }

    protected void allowAllMovement() {
        isAllowedToMoveUpwards = true;
        isAllowedToMoveDownwards = true;
        isAllowedToMoveLeftwards = true;
        isAllowedToMoveRightwards = true;
    }

    public boolean isRightOf(Entity e) {
        return x() > e.x();
    }

    public boolean isLeftOf(Entity e) {
        return !isRightOf(e);
    }

    public boolean isAbove(Entity e) {
        return y() < e.y();
    }

    public boolean isBelow(Entity e) {
        return !isAbove(e);
    }

    public String toString() {
        return name + "#" + uuid;
    }

}
