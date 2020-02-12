package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.controllers.MouseController;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.input.MouseClicked;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GenericButton extends UIElement {

    public boolean isBeingHovered;
    public MouseClicked clicked;

    public GenericButton(int width, int height) {
        super(width, height);

        clicked = new MouseClicked() {
            @Override
            public void onClick(MouseEvent e) {
                checkClick(e);
            }
        };
    }

    public GenericButton(BufferedImage image) {
        this(image.getWidth(), image.getHeight());
        this.image = image;
    }

    public GenericButton(BufferedImage image, int x, int y) {
        this(image);
        setPosition(x, y);
    }

    public GenericButton(int width, int height, int x, int y) {
        this(width, height);
        setPosition(x, y);
    }

    public void onClick() {
    }

    public void checkClick(MouseEvent e) {
        if (isClickInBounds(e.getX(), e.getY()) && isEnabled) {
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
