package net.cpvpbreezy.scaler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Version-specific bits used by the shared code (26.x).
 * 26.1.x keeps the open screen on {@code Minecraft} ({@code screen} / {@code setScreen});
 * 26.2+ moved it to {@code Minecraft.gui} ({@code screen()} / {@code setScreen}). Looked up once.
 */
public final class Platform {
    private static final MethodHandle GET_SCREEN;
    private static final MethodHandle SET_SCREEN;

    static {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodHandle get, set;
        try {
            get = lookup.findGetter(Minecraft.class, "screen", Screen.class);
            set = lookup.findVirtual(Minecraft.class, "setScreen", MethodType.methodType(void.class, Screen.class));
        } catch (ReflectiveOperationException e) {
            try {
                MethodHandle gui = lookup.findGetter(Minecraft.class, "gui", net.minecraft.client.gui.Gui.class);
                Class<?> guiClass = net.minecraft.client.gui.Gui.class;
                get = MethodHandles.filterReturnValue(gui,
                        lookup.findVirtual(guiClass, "screen", MethodType.methodType(Screen.class)));
                set = MethodHandles.filterArguments(
                        lookup.findVirtual(guiClass, "setScreen", MethodType.methodType(void.class, Screen.class)), 0, gui);
            } catch (ReflectiveOperationException e2) {
                throw new IllegalStateException("Scaler: can't find the current screen in this Minecraft version", e2);
            }
        }
        GET_SCREEN = get;
        SET_SCREEN = set;
    }

    private Platform() {}

    public static Screen currentScreen() {
        try {
            return (Screen) GET_SCREEN.invoke(Minecraft.getInstance());
        } catch (Throwable t) {
            return null;
        }
    }

    public static void setScreen(Screen screen) {
        try {
            SET_SCREEN.invoke(Minecraft.getInstance(), screen);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
