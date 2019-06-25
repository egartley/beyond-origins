package net.egartley.beyondorigins.controllers;

import net.egartley.gamelib.input.KeyTyped;

import java.util.ArrayList;

public class KeyboardController {

    private static ArrayList<KeyTyped> typeds = new ArrayList<>();

    public static void onKeyTyped(int keyCode) {
        for (KeyTyped kt : typeds) {
            if (kt.keyCode == keyCode) {
                kt.onType();
            }
        }
    }

    public static void addKeyTyped(KeyTyped kt) {
        if (!typeds.contains(kt)) {
            typeds.add(kt);
        }
    }

    public static void removeKeyTyped(KeyTyped kt) {
        typeds.remove(kt);
    }

}
