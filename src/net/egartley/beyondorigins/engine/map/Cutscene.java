package net.egartley.beyondorigins.engine.map;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.interfaces.Tickable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class Cutscene extends MapSector {

    private double transitionAlpha = 255;
    private final int BAR_HEIGHT = 64;
    private final double TRANSITION_SPEED = 1.5;
    private MapSector returnSector;
    private Color transitionColor = Color.black;

    public boolean isShowingTransition;

    public Cutscene(Map parent, int sector) {
        super(parent, sector);
    }

    public abstract void init();

    public void start() {
        returnSector = parent.sector;
        parent.changeSector(this, returnSector);
        isShowingTransition = true;
    }

    public void end() {
        parent.changeSector(returnSector, this);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        if (isShowingTransition) {
            graphics.setColor(transitionColor);
            graphics.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
            transitionColor = new Color(0, 0, 0, (int) (transitionAlpha -= TRANSITION_SPEED));
            if (transitionAlpha <= 0) {
                isShowingTransition = false;
            }
        }
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH, BAR_HEIGHT);
        graphics.fillRect(0, Game.WINDOW_HEIGHT - BAR_HEIGHT, Game.WINDOW_WIDTH, BAR_HEIGHT);
    }

    @Override
    public void tick() {
        tickables.forEach(Tickable::tick);
    }

}
