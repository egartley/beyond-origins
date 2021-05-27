package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.core.controllers.MouseController;
import net.egartley.beyondorigins.core.input.Mouse;
import net.egartley.beyondorigins.core.input.MouseClickedEvent;
import org.newdawn.slick.Image;

public class GenericButton extends UIElement {

    public boolean isHovering;
    public MouseClickedEvent clickedEvent;

    public GenericButton(int width, int height) {
        super(width, height);
        clickedEvent = new MouseClickedEvent() {
            @Override
            public void onClick(int x, int y) {
                checkClick(x, y);
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

    public void checkClick(int x, int y) {
        if (isClickInBounds(x, y) && isEnabled) {
            onClick();
        }
    }

    public void registerClicked() {
        MouseController.addMouseClicked(clickedEvent);
    }

    public void deregisterClicked() {
        MouseController.removeMouseClicked(clickedEvent);
    }

    @Override
    public void tick() {
        isHovering = isClickInBounds(Mouse.x, Mouse.y) && isEnabled;
    }

}
