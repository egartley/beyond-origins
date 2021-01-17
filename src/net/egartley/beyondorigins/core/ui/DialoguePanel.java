package net.egartley.beyondorigins.core.ui;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.core.abstracts.UIElement;
import net.egartley.beyondorigins.core.controllers.DialogueController;
import net.egartley.beyondorigins.core.logic.Calculate;
import net.egartley.beyondorigins.core.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.core.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.*;

public class DialoguePanel extends UIElement {

    private short lineIndex = -1;
    private DialogueExchange exchange;
    private final Image moreLinesImage;
    private static final double DELAY = 1.225D;
    private static final Font characterNameFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);

    public boolean isShowing;
    public boolean isReadyToAdvance;
    public static Font lineFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);
    public static final short MAX_LINES = 5;

    public DialoguePanel() {
        super(Images.get(Images.DIALOGUE_PANEL), true);
        moreLinesImage = Images.get(Images.MORE_LINES);
        setPosition(Calculate.getCenteredX(image.getWidth()), Game.WINDOW_HEIGHT - image.getHeight() - 8);
    }

    /**
     * Called when the space bar is pressed
     */
    public void advance() {
        if (!isShowing) {
            return;
        }
        exchange.advance();
        if (exchange.isFinished) {
            DialogueController.onFinished(exchange);
            exchange.reset();
            hide();
        }
    }

    /**
     * Disable advancing the dialogue for {@link #DELAY} amount of time
     */
    public void delay() {
        isReadyToAdvance = false;
        new DelayedEvent(DELAY) {
            @Override
            public void onFinish() {
                isReadyToAdvance = true;
            }
        }.start();
    }

    /**
     * Makes the dialogue panel visible
     */
    public void show() {
        isShowing = true;
        InGameState.isDialogueVisible = true;
        delay();
        Entities.PLAYER.freeze();
    }

    /**
     * Makes the dialogue panel no longer visible
     */
    public void hide() {
        isShowing = false;
        isReadyToAdvance = false;
        InGameState.isDialogueVisible = false;
        Entities.PLAYER.thaw();
    }

    /**
     * Sets {@link #exchange} and calls {@link #show()}
     *
     * @param exchange The exchange to start
     */
    public void startExchange(DialogueExchange exchange) {
        this.exchange = exchange;
        if (!isShowing) {
            show();
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (!isShowing) {
            return;
        }
        graphics.drawImage(image, x(), y());
        Image characterImage = exchange.currentDialogue.character.getCharacterImage();
        graphics.drawImage(characterImage, 277 - characterImage.getWidth() / 2, 414 - (characterImage.getHeight() - 44));
        graphics.setColor(Color.white);
        graphics.setFont(characterNameFont);
        graphics.drawString(exchange.currentDialogue.character.getName(), 277 - characterNameFont.getWidth(exchange.currentDialogue.character.getName()) / 2, 466);
        graphics.setFont(lineFont);
        for (String line : exchange.displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
        if (isReadyToAdvance) {
            graphics.drawImage(moreLinesImage, 700, 500);
        }
    }

    private void renderLine(String text, Graphics graphics) {
        // max width 380, or max str length 49
        lineIndex++;
        graphics.drawString(text, x() + 106, y() + 3 + (22 * lineIndex));
    }

    @Override
    public void tick() {

    }

}
