package net.egartley.beyondorigins.engine.input;

/**
 * An event that can be executed when a key is typed
 */
public class KeyTyped {

    public int keyCode;

    public KeyTyped(int keyCode) {
        this.keyCode = keyCode;
    }

    public void onType() {

    }

}
