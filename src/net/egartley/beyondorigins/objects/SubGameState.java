package net.egartley.beyondorigins.objects;

import java.awt.*;

/**
 * Represents a "sub", or lower level, state within another game state
 *
 * @see GameState
 */
public abstract class SubGameState extends GameState {

    /**
     * Unique integer used for identification
     */
    public int identificationNumber;

    @Override
    public abstract void render(Graphics graphics);

    @Override
    public abstract void tick();

}
