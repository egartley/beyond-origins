package net.egartley.beyondorigins.engine;

import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public abstract class LevelMapSector implements Tickable {

    public int id;
    public static final short TILE_ROWS = 17, TILE_COLS = 30;
    private int tileRenderX, tileRenderY;
    private final short TILE_SIZE = 32, BOUNDARY_SIZE = 18;
    private final short PLAYER_ENTRANCE_OFFSET = BOUNDARY_SIZE + 4;

    protected LevelMap parent;
    protected ArrayList<ArrayList<LevelMapTile>> tiles = new ArrayList<>();

    public LevelMapSector(LevelMap parent, int id) {
        this.parent = parent;
        this.id = id;
        this.parent.tileBuilder.buildTiles(this);
        this.tiles = this.parent.tileBuilder.getTiles();
    }

    public abstract void init();

    public void render(Graphics graphics) {
        drawTiles(graphics);
    }

    public void onEnter(LevelMapSector from) {
        init();
    }

    public void onLeave(LevelMapSector to) {
        // entities.clear();
        // Collisions.nuke();
    }

    protected void drawTiles(Graphics graphics) {
        tileRenderX = 0;
        tileRenderY = 0;
        for (ArrayList<LevelMapTile> row : tiles) {
            for (LevelMapTile tile : row) {
                graphics.drawImage(tile.image, tileRenderX, tileRenderY);
                tileRenderX += TILE_SIZE;
            }
            tileRenderX = 0;
            tileRenderY += TILE_SIZE;
        }
    }

    protected void updatePlayerPosition(LevelMapSector from) {
        /*switch (getNeighborDirection(from)) {
            case Direction.UP -> Entities.PLAYER.y(PLAYER_ENTRANCE_OFFSET);
            case Direction.LEFT -> Entities.PLAYER.x(PLAYER_ENTRANCE_OFFSET);
            case Direction.DOWN -> Entities.PLAYER.y(Game.WINDOW_HEIGHT - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.height);
            case Direction.RIGHT -> Entities.PLAYER.x(Game.WINDOW_WIDTH - PLAYER_ENTRANCE_OFFSET - Entities.PLAYER.sprite.width);
        }*/
    }

    @Override
    public String toString() {
        return parent.toString() + ", Sector " + this.id;
    }

}
