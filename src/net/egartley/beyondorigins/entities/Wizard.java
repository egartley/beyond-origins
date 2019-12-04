package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.ImageStore;
import net.egartley.beyondorigins.ingame.Item;
import net.egartley.beyondorigins.ui.NotificationBanner;
import net.egartley.gamelib.abstracts.AnimatedEntity;
import net.egartley.gamelib.graphics.Animation;
import net.egartley.gamelib.graphics.EntityExpression;
import net.egartley.gamelib.graphics.SpriteSheet;
import net.egartley.gamelib.interfaces.Character;
import net.egartley.gamelib.logic.dialogue.CharacterDialogue;
import net.egartley.gamelib.logic.dialogue.DialogueExchange;
import net.egartley.gamelib.logic.events.DialogueExchangeFinishedEvent;
import net.egartley.gamelib.logic.interaction.BoundaryPadding;
import net.egartley.gamelib.logic.interaction.EntityBoundary;
import net.egartley.gamelib.logic.interaction.EntityEntityInteraction;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wizard extends AnimatedEntity implements Character {

    private final int ANIMATION_THRESHOLD = 210;

    public boolean metPlayer = false;
    public boolean foundHat = false;
    public boolean wearingHat = false;

    private EntityExpression meetPlayerExpression;
    private EntityExpression foundHatExpression;
    private DialogueExchange dialogue_meetPlayer;
    private DialogueExchange dialogue_gotHat;
    private DialogueExchange dialogue_playerGeneric;

    public Wizard() {
        super("Wizard", new SpriteSheet(ImageStore.get(ImageStore.WIZARD_DEFAULT), 30, 44, 2, 4));
        speed = 0.8;
        image = animations.get(0).sprite.toBufferedImage(0);
        sheets.add(new SpriteSheet(ImageStore.get(ImageStore.WIZARD_WITH_HAT), 30, 56, 2, 4));

        meetPlayerExpression = new EntityExpression(EntityExpression.ATTENTION, this);
        foundHatExpression = new EntityExpression(EntityExpression.HEART, this);

        dialogue_meetPlayer = new DialogueExchange(new CharacterDialogue(this, "wizard/meet-player-1.def"),
                new CharacterDialogue(this, "wizard/meet-player-2.def"),
                new CharacterDialogue(Entities.PLAYER, "player/meet-wizard-1.def"),
                new CharacterDialogue(this, "wizard/meet-player-3.def"));
        dialogue_gotHat = new DialogueExchange(new CharacterDialogue(this, "wizard/player-found-hat-1.def"),
                new CharacterDialogue(this, "wizard/player-found-hat-2.def"));
        dialogue_playerGeneric = new DialogueExchange(new CharacterDialogue(this, "wizard/generic", true, 3));

        DialogueController.addFinished(new DialogueExchangeFinishedEvent(dialogue_meetPlayer) {
            @Override
            public void onFinish() {
                metPlayer = true;
                Game.in().map.sector.pushNotification(new NotificationBanner("New quest added", "items/wizard-hat.png"));
            }
        });
        DialogueController.addFinished(new DialogueExchangeFinishedEvent(dialogue_gotHat) {
            @Override
            public void onFinish() {
                // take hat from player
                Game.in().playerMenu.take(Item.WIZARD_HAT);
                // switch to sprite that is wearing the hat
                setSpriteSheet(1);
                wearingHat = true;
                // update position so that it looks like he doesn't move (hat makes him "taller")
                y(y() - 12);

                interactions.get(0).collision.end();
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

    /**
     * Called whenever the player interacts with the wizard
     */
    private void onPlayerInteraction() {
        boolean playerHasHat = Game.in().playerMenu.has(Item.WIZARD_HAT);
        if (!foundHat) {
            if (!playerHasHat) {
                Game.in().dialogue.startExchange(dialogue_meetPlayer);
            } else {
                foundHat = true;
                Game.in().dialogue.startExchange(dialogue_gotHat);
            }
        } else if (wearingHat) {
            Game.in().dialogue.startExchange(dialogue_playerGeneric);
        }
    }

    @Override
    public void setAnimations() {
        animations.add(new Animation(sprites.get(0), ANIMATION_THRESHOLD));
        animations.add(new Animation(sprites.get(1), ANIMATION_THRESHOLD));
        animation = animations.get(0);
    }

    @Override
    protected void setBoundaries() {
        boundaries.add(new EntityBoundary(this, sprite, new BoundaryPadding(0)));
        defaultBoundary = boundaries.get(0);
    }

    @Override
    protected void setCollisions() {

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
        return id;
    }

    @Override
    public BufferedImage getCharacterImage() {
        return image;
    }

}
