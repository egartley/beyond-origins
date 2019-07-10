package net.egartley.beyondorigins.gamestates;

import net.egartley.beyondorigins.Game;
import net.egartley.beyondorigins.ui.MenuButton;
import net.egartley.gamelib.logic.math.Calculate;
import net.egartley.gamelib.objects.GameState;

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
        buttons.add(new MenuButton("Quit", true, (Game.WINDOW_WIDTH / 2) - 50, (Game.WINDOW_HEIGHT / 2) - 16 + 52, 100, 32) {
            @Override
            public void onClick() {
                Game.quit();
            }
        });
    }

    @Override
    public void render(Graphics graphics) {
        // background
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, Game.WINDOW_WIDTH + 1, Game.WINDOW_HEIGHT + 1);

        // logo placeholder
        graphics.setColor(Color.BLACK);
        graphics.drawRect(Calculate.getCenter(Game.WINDOW_WIDTH / 2, 300), 52, 300, 150);

        for (MenuButton b : buttons) {
            b.render(graphics);
        }
    }

    @Override
    public void tick() {
        for (MenuButton b : buttons) {
            b.tick();
        }
    }
}
