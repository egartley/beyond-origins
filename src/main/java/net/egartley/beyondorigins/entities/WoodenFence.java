package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.VisibleEntity;
import net.egartley.beyondorigins.engine.graphics.Sprite;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.BoundaryPadding;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.data.Images;
import org.newdawn.slick.Image;

/**
 * Test for dynamic creation of sprites and their images
 */
public class WoodenFence extends VisibleEntity {

    private final int length;
    private final boolean hasCorners;

    public WoodenFence(int length, boolean corners) {
        super("Wooden-Fence");
        if (length < 1) {
            length = 1;
        }
        this.length = length;
        hasCorners = corners;
        setSprite(buildSprite());
        isTraversable = false;
        isSectorSpecific = true;
    }

    private Sprite buildSprite() {
        Image build;
        Image full = Images.getImageFromPath(Images.entityPath + "wooden-fence-full.png");
        if (hasCorners) {
            build = Images.getImageFromPath(Images.entityPath + "wooden-fence-left-end.png");
        } else {
            build = full;
        }
        if (length > 1) {
            for (int i = 0; i < length - 1; i++) {
                build = Util.stitchImage(build, full);
            }
        }
        if (hasCorners) {
            build = Util.stitchImage(build, Images.getImageFromPath(Images.entityPath + "wooden-fence-right-end.png"));
        }
        return new Sprite(build);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, image.getWidth(), image.getHeight(),
                new BoundaryPadding(-12, 0, -10, 0)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {

    }

}
