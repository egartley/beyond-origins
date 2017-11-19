package net.egartley.beyondorigins.objects;

import java.awt.Graphics;

public class StaticEntity extends Entity {

	public StaticEntity() {
		isAnimated = false;
		isStatic = true;
	}
	
	@Override
	public void render(Graphics graphics) {
		
	}

	@Override
	public void tick() {
		
	}

}
