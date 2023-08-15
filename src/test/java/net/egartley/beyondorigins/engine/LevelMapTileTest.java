package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelMapTileTest {

    @Test
    void createEmptyTile() {
        String id = "testid";
        LevelMapTile tile = new LevelMapTile(id, null);
        assertEquals(id, tile.getID());
        assertNull(tile.image);
    }

}