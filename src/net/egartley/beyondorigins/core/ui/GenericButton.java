package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.core.controllers.MouseController;
import net.egartley.beyondorigins.core.input.Mouse;
import net.egartley.beyondorigins.core.input.MouseClicked;
import org.newdawn.slick.Image;

public class GenericButton extends UIElement {

    public boolean isBeingHovered;
    public MouseClicked clicked;

    public GenericButton(int width, int height) {
        super(width, height);
        clicked = new MouseClicked() {
            @Override
            public void onClick(int button, int x, int y) {
                checkClick(button, x, y);
            }
        };
    }

    public GenericButton(Image image) {
        this(image.getWidth(), image.getHeight());
        this.image = image;
    }

    public GenericButton(Image image, int x, int y) {
        this(image);
        setPosition(x, y);
    }

    public GenericButton(int width, int height, int x, int y) {
        this(width, height);
        setPosition(x, y);
    }

    public void onClick() {

    }

    public void checkClick(int button, int x, int y) {
        if (isClickInBounds(x, y) && isEnabled) {
            onClick();
        }
    }

    public void registerClicked() {
        MouseController.addMouseClicked(clicked);
    }

    public void deregisterClicked() {
        MouseController.removeMouseClicked(clicked);
    }

    @Override
    public void tick() {
        isBeingHovered = isClickInBounds(Mouse.x, Mouse.y) && isEnabled;
    }

}
