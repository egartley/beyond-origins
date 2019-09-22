package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.threads.DelayedEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NotificationBanner extends UIElement {

    public String[] lines;
    public BufferedImage icon;

    public boolean done = false;
    private int offset = 0;
    private boolean startedAnimation = false, readyToMoveAgain = true, reachedTargetY = false, shown = false;

    private static int MAX_LINE_LENGTH = 31;
    private static int startY, targetY = 8;
    private static final double SLIDE_DELAY = 0.0001D, SHOW_DELAY = 3.5D;
    private static Font font = new Font("Bookman Old Style", Font.BOLD, 14);

    public NotificationBanner(String text) {
        this(text, "ui/notification-icon-default.png");
    }

    public NotificationBanner(String text, String iconFile) {
        super(ImageStore.get(ImageStore.uiPath + "notification-banner.png"));
        icon = ImageStore.get(ImageStore.path + iconFile);
        startY = -8 - image.getHeight();
        lines = Util.toLines(text, 260, MAX_LINE_LENGTH);
        setPosition(Calculate.getCenteredX(width), startY);
    }

    public void drawLine(String line, Graphics graphics) {
        graphics.drawString(line, x() + 64, y() + 24 + (offset++ * 18));
    }

    private void slideDown() {
        y(y() + 1);
    }

    private void slideUp() {
        y(y() - 1);
    }

    @Override
    public void tick() {
        if (done) {
            Game.in().map.sector.killNotification(this);
            return;
        }

        reachedTargetY = y() >= targetY || reachedTargetY;
        if (!startedAnimation) {
            readyToMoveAgain = false;
            startedAnimation = true;
            new DelayedEvent(SLIDE_DELAY) {
                @Override
                public void onFinish() {
                    slideDown();
                    readyToMoveAgain = true;
                }
            }.start();
        } else if (readyToMoveAgain && !reachedTargetY && !shown) {
            readyToMoveAgain = false;
            new DelayedEvent(SLIDE_DELAY) {
                @Override
                public void onFinish() {
                    slideDown();
                    readyToMoveAgain = true;
                }
            }.start();
        } else if (readyToMoveAgain && !shown) {
            readyToMoveAgain = false;
            new DelayedEvent(SHOW_DELAY) {
                @Override
                public void onFinish() {
                    shown = true;
                    readyToMoveAgain = true;
                }
            }.start();
        } else if (readyToMoveAgain && y() >= startY) {
            readyToMoveAgain = false;
            new DelayedEvent(SLIDE_DELAY) {
                @Override
                public void onFinish() {
                    slideUp();
                    readyToMoveAgain = true;
                    done = y() <= startY;
                }
            }.start();
        }
    }

    @Override
    public void render(Graphics graphics) {
        // background
        graphics.drawImage(image, x(), y(), null);
        // icon
        graphics.drawImage(icon, x() + 8 + (48 - icon.getWidth()) / 2, y() + 8 + (48 - icon.getHeight()) / 2, null);
        // text
        graphics.setColor(Color.WHITE);
        graphics.setFont(font);
        for (String line : lines) {
            drawLine(line, graphics);
        }
        offset = 0;
    }

}
