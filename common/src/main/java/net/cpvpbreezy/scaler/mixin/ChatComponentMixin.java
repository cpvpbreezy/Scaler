package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.cpvpbreezy.scaler.ScaledElement;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Chat already has its own scale, used for drawing and for clicking/hovering messages.
 * Multiplying it (and the chat box size) keeps all of that working at the custom scale.
 */
@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    @Inject(method = "getScale", at = @At("RETURN"), cancellable = true)
    private void scaler$scale(CallbackInfoReturnable<Double> cir) {
        float f = ScaleContext.factor(ScaledElement.CHAT);
        if (f != 1f) cir.setReturnValue(cir.getReturnValue() * f);
    }

    @Inject(method = "getWidth()I", at = @At("RETURN"), cancellable = true)
    private void scaler$width(CallbackInfoReturnable<Integer> cir) {
        float f = ScaleContext.factor(ScaledElement.CHAT);
        if (f != 1f) cir.setReturnValue(Math.max(1, Math.round(cir.getReturnValue() * f)));
    }

    @Inject(method = "getHeight()I", at = @At("RETURN"), cancellable = true)
    private void scaler$height(CallbackInfoReturnable<Integer> cir) {
        float f = ScaleContext.factor(ScaledElement.CHAT);
        if (f != 1f) cir.setReturnValue(Math.max(1, Math.round(cir.getReturnValue() * f)));
    }
}
