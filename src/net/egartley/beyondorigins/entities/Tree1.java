package net.egartley.beyondorigins.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.egartley.beyondorigins.logic.interaction.EntityBoundary;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

public class Tree1 extends StaticEntity {

	/**
	 * Creates a new instance of {@code Tree1} with {@code Sprite} s
	 * 
	 * @param s
	 *            {@code Sprite} object to use while rendering
	 */
	public Tree1(Sprite s) {
		currentSprite = s;
		setBoundary();
	}

	/**
	 * Creates a new instance of {@code Tree1} with {@code Sprite} s, at the given
	 * coordinate
	 * 
	 * @param s
	 *            {@code Sprite} object to use while rendering
	 */
	public Tree1(Sprite s, int x, int y) {
		currentSprite = s;
		this.x = x;
		this.y = y;
		setBoundary();
	}

	/**
	 * <p>
	 * Disables the direction of movement opposite of where the player collided with
	 * the tree
	 * </p>
	 * <p>
	 * <b>Example:</b> player collides with top of the tree, so downward movement is
	 * disabled
	 * </p>
	 * 
	 * @param treeBoundary
	 *            EntityBoundary of the instance of Tree1
	 */
	public void playerCollision(EntityBoundary treeBoundary) {
		EntityBoundary playerBoundary = Entities.PLAYER.boundary;
		boolean down = Entities.PLAYER.movingDown;
		boolean up = Entities.PLAYER.movingUp;
		boolean right = Entities.PLAYER.movingRight;
		boolean left = Entities.PLAYER.movingLeft;
		if (playerBoundary.north <= treeBoundary.south && up) {
			// bottom of tree
			System.out.println("disable up");
			Entities.PLAYER.canMoveUp = false;
		} else if (playerBoundary.east >= treeBoundary.west && right) {
			// right of tree
			System.out.println("disable right");
			Entities.PLAYER.canMoveRight = false;
		}
		if (playerBoundary.west <= treeBoundary.east && left) {
			// left of tree
			System.out.println("disable left");
			Entities.PLAYER.canMoveLeft = false;
		} else if (playerBoundary.south >= treeBoundary.north && down) {
			// top of tree
			System.out.println("disable down");
			Entities.PLAYER.canMoveDown = false;
		}
		
		if (!Entities.PLAYER.canMoveLeft) {
			Entities.PLAYER.canMoveUp = true;
		}
		if (!Entities.PLAYER.canMoveRight) {
			Entities.PLAYER.canMoveDown = true;
		}
		
		System.out.println("\n");
	}

	@Override
	public void setBoundary() {
		BufferedImage image = currentSprite.getCurrentFrameAsBufferedImage();
		boundary = new EntityBoundary(this, image.getWidth(), image.getHeight(), 2, x, y);
	}

	@Override
	public void render(Graphics graphics) {
		graphics.drawImage(currentSprite.getCurrentFrameAsBufferedImage(), x, y, null);
		boundary.draw(graphics);
	}

	@Override
	public void tick() {

	}

}
