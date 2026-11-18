package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** A scaled chest / inventory screen is laid out for the GUI size that matches its scale. */
@Mixin(Screen.class)
public abstract class ScreenSizeMixin {
    @ModifyVariable(method = "init(II)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int scaler$initW(int w) {
        return scaler$scale(w);
    }

    @ModifyVariable(method = "init(II)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int scaler$initH(int h) {
        return scaler$scale(h);
    }

    @ModifyVariable(method = "resize(II)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int scaler$resizeW(int w) {
        return scaler$scale(w);
    }

    @ModifyVariable(method = "resize(II)V", at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int scaler$resizeH(int h) {
        return scaler$scale(h);
    }

    private int scaler$scale(int v) {
        float f = ScaleContext.screenFactor((Screen) (Object) this);
        return f == 1f ? v : Math.max(1, (int) Math.floor(v / f));
    }
}
