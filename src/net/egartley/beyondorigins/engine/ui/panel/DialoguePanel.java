package net.egartley.beyondorigins.engine.ui.panel;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.engine.ui.UIElement;
import net.egartley.beyondorigins.engine.controllers.DialogueController;
import net.egartley.beyondorigins.engine.logic.Calculate;
import net.egartley.beyondorigins.engine.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.engine.threads.DelayedEvent;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.*;

public class DialoguePanel extends UIElement {

    private short lineIndex = -1;
    private static final double ADVANCE_DELAY = 1.225D;
    private DialogueExchange exchange;
    private final Image moreLines;
    private static final Font CHARACTER_NAME_FONT =
            new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);

    public boolean isShowing;
    public boolean isReadyToAdvance;
    public static final short MAX_LINES = 5;
    public static final Font LINE_FONT =
            new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);

    public DialoguePanel() {
        super(Images.getImage(Images.DIALOGUE_PANEL), true);
        moreLines = Images.getImage(Images.MORE_LINES);
        this.x = Calculate.getCenteredX(image.getWidth());
        this.y = Game.WINDOW_HEIGHT - image.getHeight() - 8;
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
     * Disable advancing the dialogue for {@link #ADVANCE_DELAY} amount of time
     */
    public void delay() {
        isReadyToAdvance = false;
        new DelayedEvent(ADVANCE_DELAY) {
            @Override
            public void onFinish() {
                isReadyToAdvance = true;
            }
        }.start();
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
     * Sets {@link #exchange} and calls {@link #show()}
     */
    public void startExchange(DialogueExchange exchange) {
        this.exchange = exchange;
        if (!isShowing) {
            show();
        }
    }

    private void renderLine(String text, Graphics graphics) {
        // max width 380, or max str length 49
        lineIndex++;
        graphics.drawString(text, x + 106, y + 3 + (22 * lineIndex));
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        if (!isShowing) {
            return;
        }
        graphics.drawImage(image, x, y);
        Image characterImage = exchange.currentDialogue.character.getCharacterImage();
        graphics.drawImage(characterImage, 277 - characterImage.getWidth() / 2,
                414 - (characterImage.getHeight() - 44));
        graphics.setColor(Color.white);
        graphics.setFont(CHARACTER_NAME_FONT);
        graphics.drawString(exchange.currentDialogue.character.getName(),
                277 - CHARACTER_NAME_FONT.getWidth(exchange.currentDialogue.character.getName()) / 2, 466);
        graphics.setFont(LINE_FONT);
        for (String line : exchange.displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
        if (isReadyToAdvance) {
            graphics.drawImage(moreLines, 700, 500);
        }
    }

}
