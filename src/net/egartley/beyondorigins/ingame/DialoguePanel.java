package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.objects.CharacterDialogue;
import net.egartley.gamelib.objects.StaticEntity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DialoguePanel extends StaticEntity {

    public boolean isShowing;

    private int characterNameStringWidth = 0;
    private short lineIndex = -1;
    private short maxLines = 6;

    private static boolean setFontMetrics;
    private static Font lineFont = new Font("Bookman Old Style", Font.BOLD, 14);
    private static Font characterNameFont = new Font("Arial", Font.PLAIN, 12);
    private BufferedImage moreLinesImage;
    public CharacterDialogue currentDialogue;

    public String[] allLines;
    public String[] displayedLines;
    public String[] queuedLines;

    public DialoguePanel(Sprite sprite) {
        super("DialogPanel", sprite);
        setPosition(Calculate.getCenter(Game.WINDOW_WIDTH / 2, sprite.width), Game.WINDOW_HEIGHT - sprite.height - 8);
        moreLinesImage = ImageStore.get(ImageStore.MORE_LINES);
    }

    public void setDialogue(CharacterDialogue dialogue) {
        currentDialogue = dialogue;
        allLines = currentDialogue.lines;
        if (allLines.length <= maxLines) {
            // max number or less lines, will always display all of them
            displayedLines = allLines;
            queuedLines = null;
        } else {
            // (max number + 1) or more lines, queue up remaining
            displayedLines = Arrays.copyOfRange(allLines, 0, 6);
            queuedLines = Arrays.copyOfRange(allLines, 6, allLines.length);
        }
    }

    public void advance() {
        if (queuedLines == null || queuedLines.length == 0) {
            DialogueController.onFinished(currentDialogue);
            hide();
        } else if (isShowing) {
            nextLine();
        }
    }

    public void show() {
        isShowing = true;
        Game.in().isDialogueVisible = true;
    }

    public void hide() {
        isShowing = false;
        Game.in().isDialogueVisible = false;
    }

    private void nextLine() {
        System.arraycopy(displayedLines, 1, displayedLines, 0, maxLines - 1);
        displayedLines[maxLines - 1] = queuedLines[0];
        queuedLines = Arrays.copyOfRange(queuedLines, 1, queuedLines.length);
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
            characterNameStringWidth = graphics.getFontMetrics(characterNameFont).stringWidth("Dummy");
            setFontMetrics = true;
        }
        // render background (the "panel" (image))
        super.render(graphics);
        // render character thing
        Sprite s = Entities.DUMMY.sprite;
        graphics.drawImage(s.toBufferedImage(), 247 + 26 - (s.width / 2), 414 /* + 26 - (s.height / 2) */, null);
        graphics.setColor(Color.WHITE);
        graphics.setFont(characterNameFont);
        graphics.drawString("Dummy", 272 - characterNameStringWidth / 2, 476);
        // render text
        graphics.setFont(lineFont);
        for (String line : displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
        // render more lines thing
        if (queuedLines != null && queuedLines.length > 0) {
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
