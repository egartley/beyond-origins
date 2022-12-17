package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.*;

public class NotificationBanner extends UIElement {

    private int offset = 0;
    private boolean didShow = false;
    private boolean didStart = false;
    private boolean didReachTargetY = false;
    private boolean isReadyToMoveAgain = true;
    private final int SPEED = 2;
    private static int startY;
    private static final int TARGET_Y = 8;
    private static final double SLIDE_DELAY = 0.001D, SHOW_DELAY = 3.5D;
    private static final Font FONT = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);

    public boolean isDone;
    public Image icon;
    public String[] lines;

    public NotificationBanner(String text) {
        this(text, "ui/notification-icon-default.png");
    }

    public NotificationBanner(String text, String iconFile) {
        super(Images.getImageFromPath(Images.uiPath + "notification-banner.png"));
        icon = Images.getImageFromPath(Images.path + iconFile);
        startY = -8 - image.getHeight();
        lines = Util.toLines(text, FONT, 260);
        this.x = Calculate.getCenteredX(width);
        this.y = startY;
    }

    private void showBanner() {
        isReadyToMoveAgain = false;
        new DelayedEvent(SHOW_DELAY) {
            @Override
            public void onFinish() {
                didShow = true;
                isReadyToMoveAgain = true;
            }
        }.start();
    }

    private void slideUp() {
        y -= SPEED;
    }

    private void slideDown() {
        y += SPEED;
    }

    private void drawLine(String line, Graphics graphics) {
        graphics.drawString(line, x + 64, y + 24 + (offset * 18));
        offset++;
    }

    private void start() {
        isReadyToMoveAgain = false;
        didStart = true;
        new DelayedEvent(SLIDE_DELAY) {
            @Override
            public void onFinish() {
                slideDown();
                isReadyToMoveAgain = true;
            }
        }.start();
    }

    private void moveUp() {
        isReadyToMoveAgain = false;
        new DelayedEvent(SLIDE_DELAY) {
            @Override
            public void onFinish() {
                slideUp();
                isReadyToMoveAgain = true;
                isDone = y <= startY;
            }
        }.start();
    }

    private void moveDown() {
        isReadyToMoveAgain = false;
        new DelayedEvent(SLIDE_DELAY) {
            @Override
            public void onFinish() {
                slideDown();
                isReadyToMoveAgain = true;
            }
        }.start();
    }

    @Override
    public void tick() {
        if (isDone) {
            InGameState.onNotificationFinish(this);
            return;
        }
        didReachTargetY = y >= TARGET_Y || didReachTargetY;
        if (!didStart) {
            start();
        } else if (isReadyToMoveAgain && !didReachTargetY && !didShow) {
            moveDown();
        } else if (isReadyToMoveAgain && !didShow) {
            showBanner();
        } else if (isReadyToMoveAgain && y >= startY) {
            moveUp();
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, x, y);
        graphics.drawImage(icon, x + 8 + (48 - icon.getWidth()) / 2, y + 8 + (48 - icon.getHeight()) / 2);
        graphics.setColor(Color.white);
        graphics.setFont(FONT);
        for (String line : lines) {
            drawLine(line, graphics);
        }
        offset = 0;
    }

}
