package net.egartley.beyondorigins.core.controllers;

import net.egartley.beyondorigins.core.input.KeyTyped;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class KeyboardController {

    private static final ArrayList<KeyTyped> typeds = new ArrayList<>();

    public static void onKeyTyped(int keyCode) {
        try {
            for (KeyTyped kt : typeds) {
                if (kt.keyCode == keyCode) {
                    kt.onType();
                }
            }
        } catch (ConcurrentModificationException e) {
            // ignore for now
        }
    }

    public static void addKeyTyped(KeyTyped kt) {
        typeds.add(kt);
    }

    public static void removeKeyTyped(KeyTyped kt) {
        typeds.remove(kt);
    }

}
