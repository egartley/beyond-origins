package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.threads.DelayedEvent;
import org.newdawn.slick.*;

public class NotificationBanner extends UIElement {

    public String[] lines;
    public Image icon;

    public boolean done = false;
    private int offset = 0;
    private final int move = 2;
    private boolean startedAnimation = false, readyToMoveAgain = true, reachedTargetY = false, shown = false;

    private static int startY;
    private static final int targetY = 8;
    private static final double SLIDE_DELAY = 0.001D, SHOW_DELAY = 3.5D;
    private static final Font font = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);

    public NotificationBanner(String text) {
        this(text, "ui/notification-icon-default.png");
    }

    public NotificationBanner(String text, String iconFile) {
        super(Images.get(Images.uiPath + "notification-banner.png"));
        icon = Images.get(Images.path + iconFile);
        startY = -8 - image.getHeight();
        lines = Util.toLines(text, font, 260);
        setPosition(Calculate.getCenteredX(width), startY);
    }

    public void drawLine(String line, Graphics graphics) {
        graphics.drawString(line, x() + 64, y() + 24 + (offset++ * 18));
    }

    private void slideDown() {
        y(y() + move);
    }

    private void slideUp() {
        y(y() - move);
    }

    @Override
    public void tick() {
        if (done) {
            InGameState.map.sector.onNotificationFinish(this);
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
        graphics.drawImage(image, x(), y());
        // icon
        graphics.drawImage(icon, x() + 8 + (48 - icon.getWidth()) / 2, y() + 8 + (48 - icon.getHeight()) / 2);
        // text
        graphics.setColor(Color.white);
        graphics.setFont(font);
        for (String line : lines) {
            drawLine(line, graphics);
        }
        offset = 0;
    }

}
