package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelMapTest {

    private LevelMap getTestMap() {
        return new LevelMap("test") {
            public void onEnter() {}
            public void onLeave() {}
            public void onSectorChange(LevelMapSector from, LevelMapSector to) {}
        };
    }

    private LevelMapSector getTestSector(LevelMap parent, int id) {
        return new LevelMapSector(parent, id) {
            public void init() {}
            public void tick() {}
        };
    }

    @Test
    void addSingleSector() {
        int id = 1;
        LevelMap testMap = getTestMap();
        LevelMapSector testSector = getTestSector(testMap, id);

        testMap.addSector(testSector);

        assertEquals();
    }

    @Test
    void setCurrentSector() {
        int id = 1;
        LevelMap testMap = getTestMap();
        LevelMapSector testSector = getTestSector(testMap, id);
        LevelMapSector testSector2 = getTestSector(testMap, id + 1);
        testMap.addSector(testSector);
        testMap.addSector(testSector2);

        testMap.setSector(id);

        assertEquals(testSector, testMap.currentSector, "currentSector set correctly");
        assertNotEquals(testSector2, testMap.currentSector, "currentSector set to wrong sector");
    }

    @Test
    void getSectorByID() {
        int id = 1;
        LevelMap testMap = getTestMap();
        LevelMapSector testSector = getTestSector(testMap, id);
        testMap.addSector(testSector);

        LevelMapSector sector = testMap.getSector(id);

        assertEquals(testSector, sector);
    }

    @Test
    void getNeighborID() {
        int id = 1;
        LevelMap testMap = getTestMap();
        LevelMapSector testSector = getTestSector(testMap, id);
        LevelMapSector testSector2 = getTestSector(testMap, id + 1);

        testMap.addNeighborMapping(new NeighborMapping(testSector.id, Direction.DOWN, testSector2.id));

        assertEquals(testSector2.id, testMap.getNeighborID(Direction.UP, testSector));
    }

    @Test
    void getNonExistentNeighborID() {
        int id = 1;
        LevelMap testMap = getTestMap();
        LevelMapSector testSector = getTestSector(testMap, id);
        LevelMapSector testSector2 = getTestSector(testMap, id + 1);

        testMap.addNeighborMapping(new NeighborMapping(testSector.id, Direction.DOWN, testSector2.id));

        assertEquals(-1, testMap.getNeighborID(Direction.LEFT, testSector2));
    }

}