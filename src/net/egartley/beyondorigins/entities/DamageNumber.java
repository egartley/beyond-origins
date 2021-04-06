package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.Entity;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class DamageNumber extends Entity {

    private final int amount;
    private final int initalY;
    private final int FLOAT_DISTANCE = 26;

    private final Color TEXT_COLOR = Color.yellow;
    private final Font FONT = new TrueTypeFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 10), true);

    public DamageNumber(int amount, Entity damaged) {
        super("Damage");
        setPosition(damaged.x() + Util.randomInt(2, damaged.sprite.width - 2), damaged.y());
        setBoundaries();
        isSectorSpecific = true;
        speed = 0.8;

        this.amount = amount;
        initalY = y();
    }

    private void destroy() {
        InGameState.map.sector.removeEntity(this);
    }

    @Override
    public void tick() {
        move(DIRECTION_UP);
        if (y() <= initalY - FLOAT_DISTANCE) {
            destroy();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(TEXT_COLOR);
        graphics.setFont(FONT);
        graphics.drawString(String.valueOf(amount), x(), y());
    }

    @Override
    protected void setCollisions() {

    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setInteractions() {

    }

}
