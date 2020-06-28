package net.egartley.beyondorigins.controllers;

import net.egartley.gamelib.input.KeyTyped;

import java.util.ArrayList;

public class KeyboardController {

    private static final ArrayList<KeyTyped> typeds = new ArrayList<>();

    public static void onKeyTyped(int keyCode) {
        ArrayList<KeyTyped> copy = (ArrayList<KeyTyped>) typeds.clone();
        for (KeyTyped kt : copy) {
            if (kt.keyCode == keyCode) {
                kt.onType();
            }
        }
    }

    public static void addKeyTyped(KeyTyped kt) {
        typeds.add(kt);
    }

    public static void removeKeyTyped(KeyTyped kt) {
        typeds.remove(kt);
    }

}
