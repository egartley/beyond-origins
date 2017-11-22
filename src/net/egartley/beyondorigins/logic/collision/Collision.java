package net.egartley.beyondorigins.logic.collision;

import java.awt.Rectangle;

import net.egartley.beyondorigins.logic.interaction.Boundary;

public abstract class Collision {

	public boolean isCollided;
	public Boundary boundary1, boundary2;
	public Rectangle rect1, rect2;

	public Collision(Boundary b1, Boundary b2) {
		boundary1 = b1;
		boundary2 = b2;
		rect1 = new Rectangle((int) boundary1.x, (int) boundary1.y, boundary1.width, boundary1.height);
		rect2 = new Rectangle((int) boundary2.x, (int) boundary2.y, boundary2.width, boundary2.height);
	}

	public void check() {
		rect1.x = (int) boundary1.x;
		rect2.x = (int) boundary2.x;
		rect1.y = (int) boundary1.y;
		rect2.y = (int) boundary2.y;
		isCollided = rect1.intersects(rect2);
	}

}
