package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.logic.collision.Collision;

/**
 * A custom "event" that can be used for gathering information from a collision
 * that has occured
 * 
 * @author Evan Gartley
 * @see EntityEntityCollisionEvent
 */
public abstract class CollisionEvent {
	public Collision invoker;
}
