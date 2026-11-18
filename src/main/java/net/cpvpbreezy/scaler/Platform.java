package net.cpvpbreezy.scaler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/** Version-specific bits used by the shared code (1.21.11). */
public final class Platform {
    private Platform() {}

    public static Screen currentScreen() {
        return Minecraft.getInstance().screen;
    }

    public static void setScreen(Screen screen) {
        Minecraft.getInstance().setScreen(screen);
    }
}
