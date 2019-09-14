package net.egartley.beyondorigins.ui;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.threads.DelayedEvent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DialoguePanel extends UIElement {

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
     * The width of the character's name when rendered
     *
     * @see #setFontMetrics
     */
    private int characterNameStringWidth = 0;
    /**
     * An index, used as a multipler for the y-coordinate of each line of text
     */
    private short lineIndex = -1;

    /**
     * Whether or not font metrics have been set
     *
     * @see #characterNameStringWidth
     */
    public boolean setFontMetrics;
    /**
     * The font used when rendering the actual dialogue
     */
    public static Font lineFont = new Font("Bookman Old Style", Font.PLAIN, 14);
    /**
     * The font used when rendering the character's name
     */
    private static Font characterNameFont = new Font("Arial", Font.PLAIN, 12);
    /**
     * The image displayed when there are more lines available
     */
    private BufferedImage moreLinesImage;

    /**
     * The dialogue that is currently being used
     */
    private DialogueExchange exchange;

    public DialoguePanel() {
        super(ImageStore.get(ImageStore.DIALOGUE_PANEL), true);
        moreLinesImage = ImageStore.get(ImageStore.MORE_LINES);
        setPosition(Calculate.getCenter(Game.WINDOW_WIDTH / 2, image.getWidth()), Game.WINDOW_HEIGHT - image.getHeight() - 8);
    }

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
        Game.in().isDialogueVisible = true;
        delay();
        Entities.PLAYER.freeze();
    }

    /**
     * Makes the dialogue panel no longer visible, and sets {@link #setFontMetrics} to <code>false</code>
     */
    public void hide() {
        isShowing = false;
        setFontMetrics = false;
        readyToAdvance = false;
        Game.in().isDialogueVisible = false;
        Entities.PLAYER.thaw();
    }

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
        if (!setFontMetrics) {
            characterNameStringWidth = graphics.getFontMetrics(characterNameFont).stringWidth(exchange.currentDialogue.character.getName());
            setFontMetrics = true;
        }
        // render background (panel)
        graphics.drawImage(image, x(), y(), null);
        // render character image and name
        BufferedImage characterImage = exchange.currentDialogue.character.getCharacterImage();
        graphics.drawImage(characterImage, 277 - characterImage.getWidth() / 2, 414 - (characterImage.getHeight() - 44), null);
        graphics.setColor(Color.WHITE);
        graphics.setFont(characterNameFont);
        graphics.drawString(exchange.currentDialogue.character.getName(), 277 - characterNameStringWidth / 2, 476);
        // render text
        graphics.setFont(lineFont);
        for (String line : exchange.displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
        // render more lines indiciator
        if (readyToAdvance) {
            graphics.drawImage(moreLinesImage, 700, 500, null);
        }
    }

    private void renderLine(String text, Graphics graphics) {
        // max width 380, or max str length 49
        lineIndex++;
        graphics.drawString(text, x() + 106, y() + 18 + (22 * lineIndex));
    }

}
