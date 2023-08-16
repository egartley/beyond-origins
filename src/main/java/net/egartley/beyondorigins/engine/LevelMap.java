package net.egartley.beyondorigins.engine;

import java.util.ArrayList;

public abstract class LevelMap implements Tickable {

    public String name;
    public LevelMapSector currentSector;
    protected TileBuilder tileBuilder;
    private ArrayList<LevelMapSector> sectors;
    private ArrayList<NeighborMapping> neighborMap;

    public LevelMap(String name) {
        this.name = name;
        sectors = new ArrayList<>();
        tileBuilder = new TileBuilder();
        neighborMap = new ArrayList<>();
    }

    public abstract void onEnter();

    public abstract void onLeave();

    public void setSector(int id) {
        LevelMapSector newSector = getSector(id);
        if (currentSector != null) {
            currentSector.onLeave(newSector);
        }
        LevelMapSector previous = currentSector;
        currentSector = newSector;
        currentSector.onEnter(previous);
    }

    public LevelMapSector getSector(int id) {
        for (LevelMapSector s : sectors) {
            if (s.id == id) {
                return s;
            }
        }
        System.out.println("WARNING: Tried to get unknown sector with id of " + id);
        return null;
    }

    public void addSector(LevelMapSector sector) {
        if (!sectors.contains(sector)) {
            sectors.add(sector);
        }
    }

    public void addNeighborMapping(NeighborMapping mapping) {
        if (!neighborMap.contains(mapping)) {
            neighborMap.add(mapping);
        }
    }

    public int getNeighborID(Direction direction, LevelMapSector sector) {
        for (NeighborMapping mapping : neighborMap) {
            if (mapping.sector2ID == sector.id && mapping.direction == direction) {
                return mapping.sector1ID;
            } else if (mapping.sector1ID == sector.id && mapping.direction == getOppositeDirection(direction)) {
                return mapping.sector2ID;
            }
        }
        return -1;
    }

    private Direction getOppositeDirection(Direction d) {
        return switch (d) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
        };
    }

    @Override
    public void tick() {
        currentSector.tick();
    }

    @Override
    public String toString() {
        return name;
    }

}
