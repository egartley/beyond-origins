package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.controllers.MouseController;
import net.egartley.gamelib.input.Mouse;
import net.egartley.gamelib.input.MouseClicked;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuButton extends UIElement {

    /**
     * Whether or not font metrics (string width and location) have been calculated. This is meant to avoid calculating the same thing every tick
     */
    public boolean setFontMetrics;
    private int stringX;
    private int stringY;

    public MouseClicked clicked;

    public static Font font = new Font("Georgia", Font.BOLD, 24);

    /**
     * String that is displayed on the button
     */
    private String text;
    private Color disabledColor = Color.DARK_GRAY;
    private Color enabledColor = Color.RED.darker();
    private Color hoverColor = enabledColor.brighter();
    private Color textColor = new Color(0x0092ff);
    private Color hoverTextColor = new Color(0x00448f);
    /**
     * The color that is currently being used for the button
     */
    private Color currentColor;
    /**
     * The color that is currently being used for the text
     */
    private Color currentTextColor;

    public MenuButton(String text, boolean isEnabledByDefault, int x, int y, int width, int height) {
        super(width, height, isEnabledByDefault);
        this.text = text;
        setPosition(x, y);

        clicked = new MouseClicked() {
            @Override
            public void onClick(MouseEvent e) {
                checkClick(e);
            }
        };
    }

    public void registerClicked() {
        MouseController.addMouseClicked(clicked);
    }

    public void deregisterClicked() {
        MouseController.removeMouseClicked(clicked);
    }

    public void setFontMetrics(FontMetrics fontMetrics) {
        stringX = (x() + (width / 2)) - (fontMetrics.stringWidth(text) / 2);
        stringY = y() + (height / 2) + (font.getSize() / 4);
        setFontMetrics = true;
    }

    /**
     * Should be called whenever the user clicks/releases the mouse
     */
    public void checkClick(MouseEvent e) {
        if (isClickInBounds(e.getX(), e.getY()) && isEnabled) {
            onClick();
        }
    }

    /**
     * Called when the button is clicked and enabled
     */
    public void onClick() {

    }

    @Override
    public void render(Graphics graphics) {
        if (!setFontMetrics) {
            setFontMetrics(graphics.getFontMetrics(font));
        }

        // background
        if (isEnabled) {
            graphics.setColor(currentColor);
        } else {
            graphics.setColor(disabledColor);
        }
        // graphics.fillRect(x, y, width, height);

        // text
        graphics.setFont(font);
        graphics.setColor(currentTextColor);
        graphics.drawString(text, stringX, stringY);
    }

    @Override
    public void tick() {
        // emulate hover effect
        if (isClickInBounds(Mouse.x, Mouse.y) && isEnabled) {
            currentColor = hoverColor;
            currentTextColor = hoverTextColor;
        } else {
            currentColor = enabledColor;
            currentTextColor = textColor;
        }
    }

}
