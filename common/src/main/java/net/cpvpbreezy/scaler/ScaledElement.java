package net.cpvpbreezy.scaler;

import net.minecraft.network.chat.Component;

/** The parts of the GUI Scaler can draw at their own GUI scale. */
public enum ScaledElement {
    HOTBAR("hotbar", 0.5f, 1.0f),
    CHAT("chat", 0f, 0f),
    SCOREBOARD("scoreboard", 1.0f, 0.5f),
    CONTAINER("container", 0f, 0f);

    public final String key;
    /** Which screen edge the element stays attached to when its size changes (0 = left/top, 1 = right/bottom). */
    public final float anchorX, anchorY;

    ScaledElement(String key, float anchorX, float anchorY) {
        this.key = key;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public Component label() {
        return Component.translatable("scaler.element." + key);
    }
}
