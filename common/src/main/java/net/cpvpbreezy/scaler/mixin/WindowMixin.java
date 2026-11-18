package net.cpvpbreezy.scaler.mixin;

import com.mojang.blaze3d.platform.Window;
import net.cpvpbreezy.scaler.ScaleContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** While a scaled element is drawn, the GUI size it sees matches its own scale. */
@Mixin(Window.class)
public abstract class WindowMixin {
    @Inject(method = "getGuiScaledWidth", at = @At("RETURN"), cancellable = true)
    private void scaler$width(CallbackInfoReturnable<Integer> cir) {
        if (ScaleContext.isActive()) cir.setReturnValue(ScaleContext.virtualWidth());
    }

    @Inject(method = "getGuiScaledHeight", at = @At("RETURN"), cancellable = true)
    private void scaler$height(CallbackInfoReturnable<Integer> cir) {
        if (ScaleContext.isActive()) cir.setReturnValue(ScaleContext.virtualHeight());
    }
}
