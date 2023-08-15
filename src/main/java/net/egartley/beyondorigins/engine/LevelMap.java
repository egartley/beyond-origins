package net.egartley.beyondorigins.engine;

import java.util.ArrayList;

public abstract class LevelMap implements Tickable {

    public String name;
    public LevelMapSector currentSector;
    protected TileBuilder tileBuilder;
    private ArrayList<LevelMapSector> sectors;

    public LevelMap(String name) {
        this.name = name;
        sectors = new ArrayList<>();
        tileBuilder = new TileBuilder();
    }

    public abstract void onEnter();

    public abstract void onLeave();

    public abstract void onSectorChange(LevelMapSector from, LevelMapSector to);

    public void setSector(LevelMapSector newSector) {
        if (currentSector != null) {
            currentSector.onLeave(newSector);
        }
        onSectorChange(currentSector, newSector);
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

    @Override
    public void tick() {
        currentSector.tick();
    }

    @Override
    public String toString() {
        return name;
    }

}
