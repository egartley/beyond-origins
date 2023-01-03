package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Util;
import net.egartley.beyondorigins.engine.entities.AnimatedEntity;
import net.egartley.beyondorigins.engine.controllers.DialogueController;
import net.egartley.beyondorigins.engine.graphics.SpriteSheet;
import net.egartley.beyondorigins.engine.interfaces.Character;
import net.egartley.beyondorigins.engine.interfaces.Interactable;
import net.egartley.beyondorigins.engine.logic.dialogue.CharacterDialogue;
import net.egartley.beyondorigins.engine.logic.dialogue.DialogueExchange;
import net.egartley.beyondorigins.engine.logic.events.DialogueFinishedEvent;
import net.egartley.beyondorigins.engine.logic.collision.boundaries.EntityBoundary;
import net.egartley.beyondorigins.engine.logic.collision.EntityEntityInteraction;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.data.Quests;
import net.egartley.beyondorigins.gamestates.InGameState;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * An NPC for testing dialogue and quests
 */
public class Wizard extends AnimatedEntity implements Character, Interactable {

    private final int ANIMATION_THRESHOLD = 210;
    private final DialogueExchange dialogue_gotHat;
    private final EntityExpression foundHatExpression;
    private final DialogueExchange dialogue_meetPlayer;
    private final EntityExpression meetPlayerExpression;
    private final DialogueExchange dialogue_playerGeneric;

    public boolean foundHat = false;
    public boolean metPlayer = false;
    public boolean wearingHat = false;

    public Wizard() {
        super("Wizard", new SpriteSheet(
                Images.getImage(Images.WIZARD_DEFAULT), 30, 44, 2, 4));
        speed = 0.8;
        image = sprites.get(0).asImage();
        sheets.add(new SpriteSheet(Images.getImage(Images.WIZARD_WITH_HAT), 30, 56, 2, 4));
        meetPlayerExpression = new EntityExpression(EntityExpression.ATTENTION, this);
        foundHatExpression = new EntityExpression(EntityExpression.HEART, this);
        dialogue_meetPlayer = new DialogueExchange(
                new CharacterDialogue(this, "wizard/meet-player-1.def"),
                new CharacterDialogue(this, "wizard/meet-player-2.def"),
                new CharacterDialogue(Entities.PLAYER, "player/meet-wizard-1.def"),
                new CharacterDialogue(this, "wizard/meet-player-3.def"));
        dialogue_gotHat = new DialogueExchange(
                new CharacterDialogue(this, "wizard/player-found-hat-1.def"),
                new CharacterDialogue(this, "wizard/player-found-hat-2.def"));
        dialogue_playerGeneric = new DialogueExchange(new CharacterDialogue(this, "wizard/generic", true, 3));
        DialogueController.addFinished(new DialogueFinishedEvent(dialogue_meetPlayer) {
            @Override
            public void onFinish() {
                metPlayer = true;
                InGameState.giveQuest(Quests.WIZARD_HAT, true);
            }
        });
        DialogueController.addFinished(new DialogueFinishedEvent(dialogue_gotHat) {
            @Override
            public void onFinish() {
                Entities.PLAYER.inventory.removeItem(Items.WIZARD_HAT);
                setSpritesFromSheet(1);
                wearingHat = true;
                // update position so that it looks like he doesn't move (hat makes him "taller")
                y -= 12;
                interactions.get(0).collision.end();
                Quests.WIZARD_HAT.objectives.get(1).complete();
                Quests.WIZARD_HAT.complete();
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (!metPlayer) {
            meetPlayerExpression.tick();
        } else if (foundHat && !wearingHat) {
            foundHatExpression.tick();
        }
    }

    @Override
    public void render(Graphics graphics) {
        if (!metPlayer) {
            meetPlayerExpression.render(graphics);
        } else if (foundHat && !wearingHat) {
            foundHatExpression.render(graphics);
        }
        super.render(graphics);
    }

    private void onPlayerInteraction() {
        boolean playerHasHat = Entities.PLAYER.inventory.containsItem(Items.WIZARD_HAT);
        if (!metPlayer) {
            InGameState.dialogue.startExchange(dialogue_meetPlayer);
        } else if (playerHasHat && !foundHat) {
            foundHat = true;
            InGameState.dialogue.startExchange(dialogue_gotHat);
        } else {
            InGameState.dialogue.startExchange(dialogue_playerGeneric);
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(0)), ANIMATION_THRESHOLD));
        animations.add(new Animation(Util.getAnimationFrames(sprites.get(1)), ANIMATION_THRESHOLD));
        animation = animations.get(0);
        animation.stop();
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    public void setCollisions() {

    }

    @Override
    public void setInteractions() {
        interactions.add(new EntityEntityInteraction(defaultBoundary, Entities.PLAYER.chatBoundary) {
            @Override
            public void interact() {
                onPlayerInteraction();
            }
        });
        interactions.get(0).activate();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getCharacterImage() {
        return image;
    }

}
