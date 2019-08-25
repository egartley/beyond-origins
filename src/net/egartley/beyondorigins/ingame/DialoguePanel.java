package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.objects.StaticEntity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DialoguePanel extends StaticEntity {

    /**
     * The maximum number of lines that can be displayed at once
     */
    public static final short MAX_LINES = 6;

    /**
     * Whether or not the dialogue panel is showing (visible)
     */
    public boolean isShowing;

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
    private static Font lineFont = new Font("Bookman Old Style", Font.BOLD, 14);
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
    public DialogueExchange exchange;

    public DialoguePanel(Sprite sprite) {
        super("DialogPanel", sprite);
        setPosition(Calculate.getCenter(Game.WINDOW_WIDTH / 2, sprite.width), Game.WINDOW_HEIGHT - sprite.height - 8);
        moreLinesImage = ImageStore.get(ImageStore.MORE_LINES);
    }

    public void advance() {
        if (exchange.isFinished) {
            DialogueController.onFinished(exchange);
            exchange.reset();
            hide();
        } else if (isShowing) {
            exchange.advance();
        }
    }

    /**
     * Makes the dialogue panel visible
     */
    public void show() {
        isShowing = true;
        Game.in().isDialogueVisible = true;
    }

    public void hide() {
        isShowing = false;
        Game.in().isDialogueVisible = false;
        setFontMetrics = false;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        if (!isShowing) {
            return;
        }
        if (!setFontMetrics) {
            characterNameStringWidth = graphics.getFontMetrics(characterNameFont).stringWidth(exchange.dialogue.character.getName());
            setFontMetrics = true;
        }
        // render background (panel)
        super.render(graphics);
        // render character image and name
        BufferedImage characterImage = exchange.dialogue.character.getDialoguePanelImage();
        graphics.drawImage(characterImage, 247 + 26 - (characterImage.getWidth() / 2), 414, null);
        graphics.setColor(Color.WHITE);
        graphics.setFont(characterNameFont);
        graphics.drawString(exchange.dialogue.character.getName(), 272 - characterNameStringWidth / 2, 476);
        // render text
        graphics.setFont(lineFont);
        for (String line : exchange.displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
        // render more lines thing
        if (exchange.queuedLines != null && exchange.queuedLines.length > 0) {
            graphics.drawImage(moreLinesImage, 700, 500, null);
        }
    }

    private void renderLine(String text, Graphics graphics) {
        // max width 380, or max str length 55
        lineIndex++;
        graphics.drawString(text, x() + 96, y() + 16 + (18 * lineIndex));
    }

    @Override
    protected void setBoundaries() {

    }

    @Override
    protected void setCollisions() {

    }

}
