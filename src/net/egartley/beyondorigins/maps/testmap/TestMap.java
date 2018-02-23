package net.egartley.beyondorigins.maps.testmap;

import java.awt.Graphics;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.logic.events.MapSectorChangeEvent;
import net.egartley.beyondorigins.maps.testmap.sectors.Sector1;
import net.egartley.beyondorigins.maps.testmap.sectors.Sector2;
import net.egartley.beyondorigins.objects.Map;

public class TestMap extends Map {

	public TestMap() {
		super();
		sectors.add(new Sector1(this, net.egartley.beyondorigins.definitions.maps.testmap.Sectors.sector1));
		sectors.add(new Sector2(this, net.egartley.beyondorigins.definitions.maps.testmap.Sectors.sector2));
		// go to the first sector by default
		changeSector(sectors.get(0));
		// stupid fix for sector changing, should be implemented in the map sector
		// change boundary constructor somehow
		sectors.get(0).changeBoundaries.get(0).to = sectors.get(1);
		sectors.get(1).changeBoundaries.get(0).to = sectors.get(0);
	}

	@Override
	public void tick() {
		currentSector.tick();
	}

	@Override
	public void render(Graphics graphics) {
		currentSector.render(graphics);
	}

	@Override
	public void onSectorChange(MapSectorChangeEvent event) {
		Debug.info("Moved from sector \"" + event.from + "\" to \"" + event.to + "\"");
	}

}
