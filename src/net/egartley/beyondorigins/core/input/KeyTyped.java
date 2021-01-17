package net.egartley.beyondorigins.core.input;

/**
 * An event that can be executed when a key is typed
 *
 * @see #onType()
 */
public class KeyTyped {

    public int keyCode;

    public KeyTyped(int keyCode) {
        this.keyCode = keyCode;
    }

    public void onType() {

    }

}
