package net.egartley.beyondorigins.engine;

import net.egartley.beyondorigins.Game;

public abstract class Entity extends Renderable implements Tickable {

    private double deltaX, deltaY;
    protected boolean isMovingUpwards;
    protected boolean isMovingDownwards;
    protected boolean isMovingLeftwards;
    protected boolean isMovingRightwards;
    public int id;
    public int health;
    public int maxHealth;
    public double speed;
    public boolean isAllowedToMoveUpwards = true;
    public boolean isAllowedToMoveDownwards = true;
    public boolean isAllowedToMoveLeftwards = true;
    public boolean isAllowedToMoveRightwards = true;
    public String name;

    public Entity(String name, int width, int height) {
        super();
        this.name = name;
        this.width = width;
        this.height = height;
        speed = 1.0;
        health = 100;
        maxHealth = 100;
        id = -1;
    }

    public void register() {
        id = Util.randomInt(100000, 999999, true);
        while (!EntityBank.isIDAvailable(id)) {
            id = Util.randomInt(100000, 999999, true);
        }
        EntityBank.registerEntity(this);
    }

    public void deregister() {
        EntityBank.removeEntity(this);
    }

    protected void move(Direction direction) {
        move(direction, true, false);
    }

    protected void move(Direction direction, boolean resetIsMoving) {
        move(direction, resetIsMoving, false);
    }

    protected void move(Direction direction, boolean resetIsMoving, boolean containToWindow) {
        if (resetIsMoving) {
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
                if (containToWindow && y <= 0) {
                    break; // top of window
                }
                isMovingUpwards = true;
                deltaY -= speed;
                y -= (int) Math.abs(deltaY - y);
            }
            case DOWN -> {
                if (containToWindow && y + height >= Game.WINDOW_HEIGHT) {
                    break; // bottom of window
                }
                isMovingDownwards = true;
                deltaY += speed;
                y += (int) Math.abs(deltaY - y);
            }
            case LEFT -> {
                if (containToWindow && x <= 0) {
                    break; // left side of window
                }
                isMovingLeftwards = true;
                deltaX -= speed;
                x -= (int) Math.abs(deltaX - x);
            }
            case RIGHT -> {
                if (containToWindow && x + width >= Game.WINDOW_WIDTH) {
                    break; // right side of window
                }
                isMovingRightwards = true;
                deltaX += speed;
                x += (int) Math.abs(deltaX - x);
            }
        }
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
        return name + "#" + id;
    }

}
