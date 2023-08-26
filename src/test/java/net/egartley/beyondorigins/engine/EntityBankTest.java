package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityBankTest {

    private Entity getTestEntity() {
        return new Entity("test", 16, 16) {
            public void tick() {}
        };
    }

    @Test
    void registerSingleEntity() {
        Entity e = getTestEntity();
        e.id = 999;

        EntityBank.registerEntity(e);

        assertTrue(EntityBank.getEntities().contains(e));
    }

    @Test
    void registerMultipleEntities() {
        Entity e = getTestEntity();
        Entity e2 = getTestEntity();
        Entity e3 = getTestEntity();
        e.id = 999;
        e2.id = 998;
        e3.id = 997;

        EntityBank.registerEntity(e);
        EntityBank.registerEntity(e2);
        EntityBank.registerEntity(e3);

        assertTrue(EntityBank.getEntities().contains(e));
        assertTrue(EntityBank.getEntities().contains(e2));
        assertTrue(EntityBank.getEntities().contains(e3));
    }

    @Test
    void removeSingleEntity() {
        Entity e = getTestEntity();
        Entity e2 = getTestEntity();
        e.id = 999;
        e2.id = 998;
        EntityBank.getEntities().add(e);
        EntityBank.getEntities().add(e2);

        EntityBank.removeEntity(e2);

        assertFalse(EntityBank.getEntities().contains(e2));
    }

    @Test
    void removeMultipleEntities() {
        Entity e = getTestEntity();
        Entity e2 = getTestEntity();
        e.id = 999;
        e2.id = 998;
        EntityBank.getEntities().add(e);
        EntityBank.getEntities().add(e2);

        EntityBank.removeEntity(e);
        EntityBank.removeEntity(e2);

        assertFalse(EntityBank.getEntities().contains(e));
        assertFalse(EntityBank.getEntities().contains(e2));
    }

    @Test
    void isIDAvailableTrue() {
        Entity e = getTestEntity();
        Entity e2 = getTestEntity();
        e.id = 999;
        e2.id = 998;
        EntityBank.getEntities().add(e);
        EntityBank.getEntities().add(e2);

        assertTrue(EntityBank.isIDAvailable(500));
    }

    @Test
    void isIDAvailableFalse() {
        Entity e = getTestEntity();
        Entity e2 = getTestEntity();
        e.id = 999;
        e2.id = 998;
        EntityBank.getEntities().add(e);
        EntityBank.getEntities().add(e2);

        assertFalse(EntityBank.isIDAvailable(999));
    }

    @Test
    void getEntityByID() {
        Entity e = getTestEntity();
        e.id = 999;
        EntityBank.getEntities().add(e);

        assertEquals(e, EntityBank.getEntityByID(e.id));
    }

}