package net.egartley.beyondorigins.maps.testmap.sectors;

import net.egartley.beyondorigins.entities.DefaultRock;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.objects.*;

import java.awt.*;

public class Sector1 extends MapSector {

    public Sector1(Map parent, MapSectorDefinition def) {
        super(parent, def);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        entities.forEach(entity -> entity.drawFirstLayer(graphics));

        Entities.DUMMY.render(graphics);
        Entities.PLAYER.render(graphics);

        entities.forEach(entity -> entity.drawSecondLayer(graphics));
    }

    @Override
    public void tick() {
        super.tick();

        entities.forEach(Entity::tick);
    }

    @Override
    public void initialize() {
        // sector-specific entities
        Sprite s = Entities.getSpriteTemplate(Entities.TREE);
        entities.add(new DefaultTree(s, 100, 200));
        entities.add(new DefaultTree(s, 36, 200));

        // re-use same sprite variable, no use in creating a new one if there is already
        // one in memory
        s = Entities.getSpriteTemplate(Entities.ROCK);
        int off = 0;
        for (byte i = 0; i < 16; i++)
            entities.add(new DefaultRock(s, (s.width * 2) * off++ + 48, 400));
        /*entities.add(new DefaultRock(s, 300, 160));
        entities.add(new DefaultRock(s, 270, 310));
        entities.add(new DefaultRock(s, 150, 370));
        entities.add(new DefaultRock(s, 460, 350));*/
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.x = 200;
            Entities.PLAYER.y = 200;
        } else {
            updatePlayerPosition(from);
        }
        initialize();
    }

    @Override
    public void onPlayerLeave(MapSector to) {
        entities.forEach(Entity::kill);
    }

}
