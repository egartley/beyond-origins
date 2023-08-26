package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private Entity getTestEntity() {
        return new Entity("test", 5, 5) {
            public void tick() {}
        };
    }

    @Test
    void register() {
        Entity e = getTestEntity();

        e.register();

        assertTrue(EntityBank.getEntities().contains(e));
    }

    @Test
    void deregister() {
        Entity e = getTestEntity();
        e.register();

        e.deregister();

        assertFalse(EntityBank.getEntities().contains(e));
    }

    @Test
    void isRightOf() {
        Entity e1 = getTestEntity();
        e1.x = 100;
        e1.y = 100;
        Entity e2 = getTestEntity();
        e2.x = 200;
        e2.y = 200;

        assertTrue(e2.isRightOf(e1));
    }

    @Test
    void isLeftOf() {
        Entity e1 = getTestEntity();
        e1.x = 100;
        e1.y = 100;
        Entity e2 = getTestEntity();
        e2.x = 200;
        e2.y = 200;

        assertTrue(e1.isLeftOf(e2));
    }

    @Test
    void isAbove() {
        Entity e1 = getTestEntity();
        e1.x = 100;
        e1.y = 100;
        Entity e2 = getTestEntity();
        e2.x = 200;
        e2.y = 200;

        assertTrue(e1.isAbove(e2));
    }

    @Test
    void isBelow() {
        Entity e1 = getTestEntity();
        e1.x = 100;
        e1.y = 100;
        Entity e2 = getTestEntity();
        e2.x = 200;
        e2.y = 200;

        assertTrue(e2.isBelow(e1));
    }

}