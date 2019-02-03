package net.egartley.beyondorigins.ingame;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.definitions.dialogue.DummyDialogue;
import net.egartley.beyondorigins.objects.Sprite;
import net.egartley.beyondorigins.objects.StaticEntity;

import java.awt.*;
import java.util.Arrays;

public class DialoguePanel extends StaticEntity {

    private short lineIndex = -1;

    public boolean isShowing;

    private Font lineFont = new Font("Bookman Old Style", Font.BOLD, 14);

    public String[] allLines;
    public String[] displayedLines;
    public String[] queuedLines;

    public DialoguePanel(Sprite sprite) {
        super("DialogPanel", sprite);
        x = (Game.WINDOW_WIDTH / 2.0) - (sprite.width / 2.0);
        y = Game.WINDOW_HEIGHT - sprite.height - 4;

        // debug
        isShowing = true;
        setDialogue(new DummyDialogue(null, "The rot enforces the bicycle. An interview refines the ranging angel under the choral. Every civil calculator rots behind the secondary computer. A stake rephrases the crazy verb. A tactic chops the earned sore. The celebrated steer bicycles within the nominate voter. The explosive interferes without an urge. The enforced water remarks a photo opposite the closing dilemma. How will the cotton stamp? An orbit apologizes into the bounce. How will the chemistry spin outside the powerful cable? The secondary name gasps. Why can't the syndrome cooperate outside his recognized receiver? A paper decline surnames the coffee. Past the cynic hardship stretches an admirable lawyer."));
        // http://watchout4snakes.com/wo4snakes/Random/RandomParagraph used to generate the above filler dialogue
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

    public void nextLine() {
        if (queuedLines.equals(null) || queuedLines.length == 0) {
            return;
        }
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
        if (!isShowing) {
            return;
        }
        // render background (the "panel")
        super.render(graphics);
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
        // N/A
    }

    @Override
    protected void setCollisions() {
        // N/A
    }

}
