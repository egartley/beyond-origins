package net.egartley.beyondorigins.objects;

import java.util.ArrayList;

public abstract class AnimatedEntity extends Entity {

	public Animation animation;
	public ArrayList<Animation> animationCollection = new ArrayList<Animation>();
	
	public abstract void setAnimationCollection();
	
	public AnimatedEntity() {
		isAnimated = true;
		isStatic = false;
	}

}
