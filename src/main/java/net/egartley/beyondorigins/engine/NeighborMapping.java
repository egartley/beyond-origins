package net.egartley.beyondorigins.engine;

public class NeighborMapping {

    public int sector1ID, sector2ID;
    public Direction direction;

    /**
     * Sector 1 is to the DIRECTION of sector 2
     *
     * @param sector1ID
     * @param direction
     * @param sector2ID
     */
    public NeighborMapping(int sector1ID, Direction direction, int sector2ID) {
        this.sector1ID = sector1ID;
        this.sector2ID = sector2ID;
        this.direction = direction;
    }

}
