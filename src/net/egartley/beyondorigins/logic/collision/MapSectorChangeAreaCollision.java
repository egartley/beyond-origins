package net.egartley.beyondorigins.logic.collision;

import java.awt.Rectangle;

import net.egartley.beyondorigins.logic.interaction.Boundary;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.logic.interaction.MapSectorChangeArea;

public class MapSectorChangeAreaCollision {

	private boolean isCollided;
	private boolean firedEvent;

	public Boundary boundary1;
	public Boundary boundary2;
	public Rectangle rectangle1;
	public Rectangle rectangle2;

	public MapSectorChangeAreaCollision(MapSectorChangeArea changeBoundary, EntityBoundary playerBoundary) {
		boundary1 = changeBoundary;
		boundary2 = playerBoundary;
		rectangle1 = boundary1.asRectangle();
		rectangle2 = boundary2.asRectangle();
		rectangle1.width++;
		rectangle1.height++;
		rectangle2.width++;
		rectangle2.height++;
	}
	
	public void tick() {
		rectangle1.x = boundary1.x - 1;
		rectangle2.x = boundary2.x - 1;
		rectangle1.y = boundary1.y - 1;
		rectangle2.y = boundary2.y - 1;
		isCollided = rectangle1.intersects(rectangle2);

		if (isCollided == true && firedEvent == false) {
			onCollide();
			firedEvent = true;
		}
	}
	
	/**
	 * This is called <b>once</b> after the collision occurs
	 * 
	 * @param event
	 *            The collision's event
	 */
	public void onCollide() {

	}

}
