package net.egartley.beyondorigins.logic.events;

import net.egartley.beyondorigins.logic.collision.Collision;

/**
 * A custom "event" that can be used for gathering information from a
 * {@link net.egartley.beyondorigins.logic.collision.Collision Collision}
 * 
 * @author Evan Gartley
 * @see EntityEntityCollisionEvent
 */
public abstract class CollisionEvent {
	public Collision invoker;
}
