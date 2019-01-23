package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Debug;
import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.objects.GameState;
import net.egartley.beyondorigins.ui.MenuButton;

import java.awt.*;
import java.util.ArrayList;

public class MainMenuState extends GameState {

    public ArrayList<MenuButton> buttons = new ArrayList<>();

    public MainMenuState() {
        identificationNumber = GameState.MAIN_MENU;
        buttons.add(new MenuButton("Play", true, (Game.WINDOW_WIDTH / 2) - 50, (Game.WINDOW_HEIGHT / 2) - 16, 100, 32) {
            @Override
            public void onClick() {
                Game.currentGameState = Game.inGameState;
            }
        });
    }

    @Override
    public void render(Graphics graphics) {
        for (MenuButton b : buttons) {
            if (!b.setFontMetrics) {
                b.setFontMetrics(graphics.getFontMetrics(MenuButton.font));
            }
            b.render(graphics);
        }
    }

    @Override
    public void tick() {
        buttons.get(0).tick();
    }
}
