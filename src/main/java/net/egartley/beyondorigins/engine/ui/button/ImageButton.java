package net.egartley.beyondorigins.engine.ui.button;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ImageButton extends GenericButton {

    public Image hoverImage;
    public Image enabledImage;
    public Image disabledImage;

    public ImageButton(Image image) {
        this(image, 0, 0);
    }

    public ImageButton(Image image, int x, int y) {
        this(image, image, image, x, y);
    }

    public ImageButton(Image enabledImage, Image disabledImage, Image hoverImage, int x, int y) {
        super(enabledImage, x, y);
        this.enabledImage = enabledImage;
        this.disabledImage = disabledImage;
        this.hoverImage = hoverImage;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Graphics graphics) {
        if (!isEnabled) {
            graphics.drawImage(disabledImage, x, y);
        } else if (isHovering) {
            graphics.drawImage(hoverImage, x, y);
        } else {
            graphics.drawImage(enabledImage, x, y);
        }
    }

}
