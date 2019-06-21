package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.entities.Entities;
import net.egartley.gamelib.graphics.Sprite;
import net.egartley.gamelib.objects.StaticEntity;

import java.awt.*;
import java.util.Arrays;

public class DialoguePanel extends StaticEntity {

    public boolean isShowing;
    private int characterNameStringWidth = 0;
    private short lineIndex = -1;

    private static boolean setFontMetrics;
    private static Font lineFont = new Font("Bookman Old Style", Font.BOLD, 14);
    private static Font characterNameFont = new Font("Arial", Font.PLAIN, 12);
    private static FontMetrics characterNameFontMetrics;

    public String[] allLines;
    public String[] displayedLines;
    public String[] queuedLines;

    public DialoguePanel(Sprite sprite) {
        super("DialogPanel", sprite);
        x = (Game.WINDOW_WIDTH / 2.0) - (sprite.width / 2.0);
        y = Game.WINDOW_HEIGHT - sprite.height - 4;
    }

    public void setDialogue(CharacterDialogue dialogue) {
        allLines = dialogue.lines;
        if (allLines.length <= 6) {
            // six or less lines, will always display all of them
            displayedLines = allLines;
            queuedLines = null;
        } else {
            // seven or more lines, queue up remaining after first six
            displayedLines = Arrays.copyOfRange(allLines, 0, 6);
            queuedLines = Arrays.copyOfRange(allLines, 6, allLines.length);
        }
    }

    public void advance() {
        if (queuedLines == null || queuedLines.length == 0) {
            isShowing = false;
        } else {
            nextLine();
        }
    }

    private void nextLine() {
        System.arraycopy(displayedLines, 1, displayedLines, 0, 5);
        displayedLines[5] = queuedLines[0];
        queuedLines = Arrays.copyOfRange(queuedLines, 1, queuedLines.length);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Graphics graphics) {
        // TODO: no hardcode for character stuff
        if (!isShowing) {
            return;
        }
        if (!setFontMetrics) {
            characterNameFontMetrics = graphics.getFontMetrics(characterNameFont);
            characterNameStringWidth = characterNameFontMetrics.stringWidth("Dummy");
            setFontMetrics = true;
        }
        // render background (the "panel" (image))
        super.render(graphics);
        // render character thing
        Sprite s = Entities.DUMMY.sprite;
        graphics.drawImage(s.toBufferedImage(), 288 - s.width / 2, 401, null);
        graphics.setFont(characterNameFont);
        graphics.drawString("Dummy", 288 - characterNameStringWidth / 2, 468);
        // render text
        graphics.setFont(lineFont);
        graphics.setColor(Color.WHITE);
        for (String line : displayedLines) {
            renderLine(line, graphics);
        }
        lineIndex = 0;
    }

    private void renderLine(String text, Graphics graphics) {
        // max width 380, or max str length 55, max 6 displayed at once
        lineIndex++;
        graphics.drawString(text, (int) x + 96, (int) y + 22 + (18 * lineIndex));
    }

    @Override
    protected void setBoundaries() {

    }

}
