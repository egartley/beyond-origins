package net.egartley.beyondorigins.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageButton extends GenericButton {

    public BufferedImage enabledImage;
    public BufferedImage disabledImage;
    public BufferedImage hoverImage;

    public ImageButton(BufferedImage image) {
        this(image, 0, 0);
    }

    public ImageButton(BufferedImage image, int x, int y) {
        this(image, image, image, x, y);
    }

    public ImageButton(BufferedImage enabledImage, BufferedImage disabledImage, BufferedImage hoverImage, int x, int y) {
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
            graphics.drawImage(disabledImage, x(), y(), null);
        } else if (isBeingHovered) {
            graphics.drawImage(hoverImage, x(), y(), null);
        } else {
            graphics.drawImage(enabledImage, x(), y(), null);
        }
    }

}
