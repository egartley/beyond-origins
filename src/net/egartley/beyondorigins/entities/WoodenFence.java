package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.Images;
import net.egartley.gamelib.abstracts.StaticEntity;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;

import java.awt.image.BufferedImage;

public class WoodenFence extends StaticEntity {

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
        setBoundaries();

        isTraversable = false;
        isDualRendered = true;
        isSectorSpecific = true;
        firstLayer = image.getSubimage(0, 18, image.getWidth(), 9);
        secondLayer = image.getSubimage(0, 0, image.getWidth(), 18);
    }

    private Sprite buildSprite() {
        BufferedImage build = null;
        BufferedImage full = Images.get(Images.entityPath + "wooden-fence-full.png");
        if (hasCorners) {
            build = Images.get(Images.entityPath + "wooden-fence-left-end.png");
        } else {
            build = full;
        }

        if (build == null) {
            Debug.warning("There was a problem while buiilding the sprite for a wooden fence");
            return null;
        }

        if (length > 1) {
            for (int i = 0; i < length - 1; i++) {
                build = Util.stitchImage(build, full);
            }
        }

        if (hasCorners) {
            build = Util.stitchImage(build, Images.get(Images.entityPath + "wooden-fence-right-end.png"));
        }

        return new Sprite(build);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, image.getWidth(), image.getHeight(), new BoundaryPadding(-12, 0, -10, 0)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setCollisions() {

    }

    @Override
    protected void setInteractions() {

    }
}
