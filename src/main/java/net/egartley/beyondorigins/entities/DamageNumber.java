package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.Entity;
import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.enums.Direction;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

/**
 * A small number that briefly appears indicating the amount of damage dealt
 */
public class DamageNumber extends VisibleEntity {

    private final int amount;
    private final int initialY;
    private final int FLOAT_DISTANCE = 26;
    private final Color TEXT_COLOR = Color.yellow;
    private final Font FONT =
            new TrueTypeFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 10), true);

    public DamageNumber(int amount, Entity damaged) {
        super("Damage");
        this.amount = amount;
        isSectorSpecific = true;
        speed = 0.8;
        setPosition(damaged.x + Util.randomInt(2, damaged.width - 2), damaged.y);
        initialY = y;
    }

    private void destroy() {
        InGameState.map.sector.removeEntity(this);
    }

    @Override
    public void tick() {
        move(Direction.UP);
        if (y <= initialY - FLOAT_DISTANCE) {
            destroy();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(TEXT_COLOR);
        graphics.setFont(FONT);
        graphics.drawString(String.valueOf(amount), x, y);
    }

    @Override
    protected void setCollisions() {

    }

    @Override
    protected void setBoundaries() {

    }

}
