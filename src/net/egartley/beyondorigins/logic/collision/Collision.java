package net.egartley.beyondorigins.logic.collision;

import java.awt.Color;
import java.awt.Rectangle;

import net.egartley.beyondorigins.logic.interaction.Boundary;
import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Entity;

public abstract class Collision {

	public boolean isCollided, firedCollisionEvent, previouslyCollided;
	public Boundary boundary1, boundary2;
	public Rectangle rect1, rect2;

	public Collision(Boundary b1, Boundary b2) {
		boundary1 = b1;
		boundary2 = b2;
		rect1 = new Rectangle(boundary1.x, boundary1.y, boundary1.width, boundary1.height);
		rect2 = new Rectangle(boundary2.x, boundary2.y, boundary2.width, boundary2.height);
	}

	public void check() {
		rect1.x = boundary1.x;
		rect2.x = boundary2.x;
		rect1.y = boundary1.y;
		rect2.y = boundary2.y;
		isCollided = rect1.intersects(rect2);
		if (isCollided && !firedCollisionEvent) {
			onCollision();
			firedCollisionEvent = true;
		}
		if (!isCollided) {
			firedCollisionEvent = false;
		}
		if (previouslyCollided != isCollided) {
			setBoundaryColors();
		}
		previouslyCollided = isCollided;
	}

	public void setBoundaryColors() {
		Entity e1 = ((EntityBoundary) boundary1).entity;
		Entity e2 = ((EntityBoundary) boundary2).entity;
		Color c1 = Color.BLACK, c2 = Color.YELLOW;
		if (isCollided) {
			c1 = Color.YELLOW;
			c2 = Color.RED;
		}

		if (!e1.isCollided) {
			if (e1.isStatic) {
				e1.boundary.drawColor = c1;
			} else {
				e1.boundary.drawColor = c2;
			}
		}
		if (!e2.isCollided) {
			if (e2.isStatic) {
				e2.boundary.drawColor = c1;
			} else {
				e2.boundary.drawColor = c2;
			}
		}
	}

	public abstract void onCollision();

}
