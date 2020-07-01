package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.beyondorigins.gamestates.InGameState;
import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.threads.DelayedEvent;
import org.newdawn.slick.*;

public class DialoguePanel extends UIElement {

    /**
     * How long before the dialogue can be advanced after starting or switching
     */
    private static final double DELAY = 1.225D;

    /**
     * The maximum number of lines that can be displayed at once
     */
    public static final short MAX_LINES = 5;

    /**
     * Whether or not the dialogue panel is showing (visible)
     */
    public boolean isShowing;
    public boolean readyToAdvance;

    /**
     * An index, used as a multipler for the y-coordinate of each line of text
     */
    private short lineIndex = -1;

    /**
     * The font used when rendering the actual dialogue
     */
    public static Font lineFont = new TrueTypeFont(new java.awt.Font("Bookman Old Style", java.awt.Font.PLAIN, 14), true);
    /**
     * The font used when rendering the character's name
     */
    private static final Font characterNameFont = new TrueTypeFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12), true);
    /**
     * The image displayed when there are more lines available
     */
    private final Image moreLinesImage;

    /**
     * The dialogue that is currently being used
     */
    private DialogueExchange exchange;

    public DialoguePanel() {
        super(Images.get(Images.DIALOGUE_PANEL), true);
        moreLinesImage = Images.get(Images.MORE_LINES);
        setPosition(Calculate.getCenter(Game.WINDOW_WIDTH / 2, image.getWidth()), Game.WINDOW_HEIGHT - image.getHeight() - 8);
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
        readyToAdvance = false;
        new DelayedEvent(DELAY) {
            @Override
            public void onFinish() {
                readyToAdvance = true;
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
        readyToAdvance = false;
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
        // render background (panel)
        graphics.drawImage(image, x(), y());
        // render character image and name
        Image characterImage = exchange.currentDialogue.character.getCharacterImage();
        graphics.drawImage(characterImage, 277 - characterImage.getWidth() / 2, 414 - (characterImage.getHeight() - 44));
        graphics.setColor(Color.white);
        graphics.setFont(characterNameFont);
        graphics.drawString(exchange.currentDialogue.character.getName(), 277 - characterNameFont.getWidth(exchange.currentDialogue.character.getName()) / 2, 466);
        // render text
        graphics.setFont(lineFont);
        for (String line : exchange.displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
        // render more lines indiciator
        if (readyToAdvance) {
            graphics.drawImage(moreLinesImage, 700, 500);
        }
    }

    private void renderLine(String text, Graphics graphics) {
        // max width 380, or max str length 49
        lineIndex++;
        graphics.drawString(text, x() + 106, y() + 3 + (22 * lineIndex));
    }

}
