package net.egartley.beyondorigins.entities;

import net.egartley.beyondorigins.controllers.DialogueController;
import net.egartley.beyondorigins.data.Images;
import net.egartley.beyondorigins.data.Items;
import net.egartley.beyondorigins.data.Quests;
import net.egartley.beyondorigins.gamestates.ingame.InGameState;
import net.egartley.beyondorigins.ingame.Quest;
import net.egartley.beyondorigins.ingame.QuestObjective;
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
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Wizard extends AnimatedEntity implements Character {

    private final int ANIMATION_THRESHOLD = 210;

    public boolean metPlayer = false;
    public boolean foundHat = false;
    public boolean wearingHat = false;

    private final EntityExpression meetPlayerExpression;
    private final EntityExpression foundHatExpression;
    private final DialogueExchange dialogue_meetPlayer;
    private final DialogueExchange dialogue_gotHat;
    private final DialogueExchange dialogue_playerGeneric;

    public Wizard() {
        super("Wizard", new SpriteSheet(Images.get(Images.WIZARD_DEFAULT), 30, 44, 2, 4));
        speed = 0.8;
        image = animations.get(0).sprite.asImage();
        sheets.add(new SpriteSheet(Images.get(Images.WIZARD_WITH_HAT), 30, 56, 2, 4));

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
                Quest quest = new Quest(Quests.WIZARD_HAT, "Missing hat", "The wizard's hat has gone missing! You must find it and ensure its safe return.");
                quest.objectives.add(new QuestObjective("Locate the Wizard's hat", "It's in one of the trees, dummy!"));
                quest.objectives.add(new QuestObjective("Return the hat", "Go back to the Wizard and give him back his magical hat."));
                InGameState.quests.add(quest, true);
            }
        });
        DialogueController.addFinished(new DialogueExchangeFinishedEvent(dialogue_gotHat) {
            @Override
            public void onFinish() {
                // take hat from player
                Entities.PLAYER.inventory.remove(Items.WIZARD_HAT);
                // switch to sprite that is wearing the hat
                setSpriteSheet(1);
                wearingHat = true;
                // update position so that it looks like he doesn't move (hat makes him "taller")
                y(y() - 12);

                interactions.get(0).collision.end();

                InGameState.quests.get(Quests.WIZARD_HAT).objectives.get(1).complete();
                InGameState.quests.get(Quests.WIZARD_HAT).complete();
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
        boolean playerHasHat = Entities.PLAYER.inventory.contains(Items.WIZARD_HAT);
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
        return name;
    }

    @Override
    public Image getCharacterImage() {
        return image;
    }

}
