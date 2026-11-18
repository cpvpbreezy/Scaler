package net.cpvpbreezy.scaler;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.joml.Matrix3x2fStack;

import java.util.ArrayDeque;

/**
 * Draws one GUI element at its own GUI scale.
 *
 * While an element is being drawn, the pose is scaled by (custom scale / vanilla scale) and the window
 * reports a matching smaller (or bigger) GUI size, so the element lays itself out as if the whole game
 * were running at the custom scale. Everything else keeps the normal GUI scale.
 */
public final class ScaleContext {
    private static final ArrayDeque<Boolean> PUSHED = new ArrayDeque<>();
    private static boolean active;
    private static Thread owner;
    private static int virtualWidth, virtualHeight;

    private ScaleContext() {}

    /** The factor between the custom and the vanilla GUI scale for this element, or 1 when it is off. */
    public static float factor(ScaledElement element) {
        ScalerConfig.Entry entry = ScalerConfig.get(element);
        if (!entry.enabled) return 1f;
        int vanilla = Minecraft.getInstance().getWindow().getGuiScale();
        if (vanilla <= 0) return 1f;
        return (float) (entry.scale / vanilla);
    }

    public static boolean isActive() {
        return active && Thread.currentThread() == owner;
    }

    public static int virtualWidth() {
        return virtualWidth;
    }

    public static int virtualHeight() {
        return virtualHeight;
    }

    /** Starts drawing {@code element} at its custom scale. Always pair with {@link #end}. */
    public static void begin(Matrix3x2fStack pose, ScaledElement element) {
        float f = factor(element);
        if (active || Math.abs(f - 1f) < 1.0e-4f) {
            PUSHED.push(Boolean.FALSE);
            return;
        }
        Window window = Minecraft.getInstance().getWindow();
        int w = window.getGuiScaledWidth();
        int h = window.getGuiScaledHeight();
        int vw = Math.max(1, (int) Math.floor(w / f));
        int vh = Math.max(1, (int) Math.floor(h / f));
        pose.pushMatrix();
        pose.translate((w - vw * f) * element.anchorX, (h - vh * f) * element.anchorY);
        pose.scale(f, f);
        virtualWidth = vw;
        virtualHeight = vh;
        owner = Thread.currentThread();
        active = true;
        PUSHED.push(Boolean.TRUE);
    }

    public static void end(Matrix3x2fStack pose) {
        if (PUSHED.isEmpty()) return;
        if (PUSHED.pop()) {
            active = false;
            pose.popMatrix();
        }
    }

    // Picture-in-picture parts of the GUI (the player model in the inventory, the enchanting book,
    // the loom banner...) are placed with raw screen coordinates instead of the pose, so while an
    // element is scaled their corners and size are converted here.

    public static int pipX(org.joml.Matrix3x2fc pose, int x) {
        return isActive() ? Math.round(pose.m00() * x + pose.m20()) : x;
    }

    public static int pipY(org.joml.Matrix3x2fc pose, int y) {
        return isActive() ? Math.round(pose.m11() * y + pose.m21()) : y;
    }

    public static float pipScale(org.joml.Matrix3x2fc pose, float scale) {
        return isActive() ? scale * pose.m00() : scale;
    }

    /** Factor for the screen that is open, if it is a chest / inventory style screen. */
    public static float screenFactor(Screen screen) {
        return screen instanceof AbstractContainerScreen<?> ? factor(ScaledElement.CONTAINER) : 1f;
    }
}
