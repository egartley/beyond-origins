package net.egartley.beyondorigins.maps.testmap.sectors;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.DefaultRock;
import net.egartley.beyondorigins.entities.DefaultTree;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.objects.Map;
import net.egartley.beyondorigins.objects.MapSector;
import net.egartley.beyondorigins.objects.MapSectorDefinition;
import net.egartley.beyondorigins.objects.Sprite;

import java.awt.*;
import java.util.ArrayList;

public class Sector1 extends MapSector {

    private ArrayList<DefaultTree> trees;
    private ArrayList<DefaultRock> rocks;

    public Sector1(Map parent, MapSectorDefinition def) {
        super(parent, def);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);

        for (DefaultTree tree : trees) {
            tree.drawFirstLayer(graphics);
        }
        for (DefaultRock rock : rocks) {
            rock.drawFirstLayer(graphics);
        }

        Entities.DUMMY.render(graphics);
        Entities.PLAYER.render(graphics);

        for (DefaultTree tree : trees) {
            tree.drawSecondLayer(graphics);
        }
        for (DefaultRock rock : rocks) {
            rock.drawSecondLayer(graphics);
        }
    }

    @Override
    public void tick() {
        super.tick();

        for (DefaultTree tree : trees) {
            tree.tick();
        }
        for (DefaultRock rock : rocks) {
            rock.tick();
        }
    }

    @Override
    public void onPlayerEnter(MapSector from) {
        if (from == null) {
            Entities.PLAYER.x = 200;
            Entities.PLAYER.y = 200;
        } else {
            if (from.equals(parent.sectors.get(1))) {
                // from sector 2
                Entities.PLAYER.y = 50;
            } else if (from.equals(parent.sectors.get(3))) {
                // from sector 4
                Entities.PLAYER.x = Game.WINDOW_WIDTH - 84;
            }
        }

        // sector-specific entities
        trees = new ArrayList<>();
        Sprite s = Entities.getSpriteTemplate(Entities.TREE);
        trees.add(new DefaultTree(s, 100, 200));
        trees.add(new DefaultTree(s, 36, 200));

        rocks = new ArrayList<>();
        // re-use same sprite variable, no use in creating a new one if there is already
        // one in memory
        s = Entities.getSpriteTemplate(Entities.ROCK);
        rocks.add(new DefaultRock(s, 300, 160));
        rocks.add(new DefaultRock(s, 270, 310));
        rocks.add(new DefaultRock(s, 150, 370));
        rocks.add(new DefaultRock(s, 460, 350));
        rocks.add(new DefaultRock(s, 200, 115));
    }

    @Override
    public void onPlayerLeave(MapSector to) {
        for (DefaultTree tree : trees) {
            tree.kill();
        }
        for (DefaultRock rock : rocks) {
            rock.kill();
        }
    }

}
