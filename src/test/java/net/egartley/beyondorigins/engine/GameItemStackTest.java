package net.egartley.beyondorigins.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameItemStackTest {

    @Test
    void addLessThanMax() {
        // int amount = 14;
        // GameItem item = new GameItem(...);
        // GameItemStack stack = new GameItemStack(item, 1);

        // stack.add(amount);

        // assertEquals(amount + 1, stack.quantity);
    }

    @Test
    void addMoreThanMax() {
        // int amount = GameItemStack.MAX_AMOUNT + 1;
        // GameItem item = new GameItem(...);
        // GameItemStack stack = new GameItemStack(item, 1);

        // stack.add(amount);

        // assertEquals(GameItemStack.MAX_AMOUNT, stack.quantity);
    }

    @Test
    void takeWithRemaining() {
        // int initialAmount = 20, takeAmount = 5;
        // GameItem item = new GameItem(...);
        // GameItemStack stack = new GameItemStack(item, initialAmount);

        // stack.take(takeAmount);

        // assertEquals(initialAmount - takeAmount, stack.quantity);
    }

    @Test
    void takeMoreThanHave() {
        // int initialAmount = 20, takeAmount = 25;
        // GameItem item = new GameItem(...);
        // GameItemStack stack = new GameItemStack(item, initialAmount);

        // stack.take(takeAmount);

        // assertEquals(0, stack.quantity);
    }

}
