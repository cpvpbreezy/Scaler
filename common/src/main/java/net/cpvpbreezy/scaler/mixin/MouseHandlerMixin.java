package net.cpvpbreezy.scaler.mixin;

import com.mojang.blaze3d.platform.Window;
import net.cpvpbreezy.scaler.ScaleContext;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Mouse positions and drags for a scaled chest / inventory screen are in that screen's own units. */
@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Inject(method = "getScaledXPos(Lcom/mojang/blaze3d/platform/Window;D)D", at = @At("RETURN"), cancellable = true)
    private static void scaler$x(Window window, double d, CallbackInfoReturnable<Double> cir) {
        scaler$apply(cir);
    }

    @Inject(method = "getScaledYPos(Lcom/mojang/blaze3d/platform/Window;D)D", at = @At("RETURN"), cancellable = true)
    private static void scaler$y(Window window, double d, CallbackInfoReturnable<Double> cir) {
        scaler$apply(cir);
    }

    private static void scaler$apply(CallbackInfoReturnable<Double> cir) {
        if (ScaleContext.isActive()) return; // the window already reports the scaled size
        float f = ScaleContext.screenFactor(net.cpvpbreezy.scaler.Platform.currentScreen());
        if (f != 1f) cir.setReturnValue(cir.getReturnValue() / f);
    }
}
