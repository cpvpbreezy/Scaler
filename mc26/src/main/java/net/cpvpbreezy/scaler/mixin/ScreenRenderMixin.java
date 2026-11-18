package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.cpvpbreezy.scaler.ScaledElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Draws chest / inventory screens (with their tooltips) at the custom scale. */
@Mixin(Screen.class)
public abstract class ScreenRenderMixin {
    @Inject(method = "extractRenderStateWithTooltipAndSubtitles", at = @At("HEAD"))
    private void scaler$begin(GuiGraphicsExtractor g, int x, int y, float delta, CallbackInfo ci) {
        if ((Object) this instanceof AbstractContainerScreen<?>) ScaleContext.begin(g.pose(), ScaledElement.CONTAINER);
    }

    @Inject(method = "extractRenderStateWithTooltipAndSubtitles", at = @At("RETURN"))
    private void scaler$end(GuiGraphicsExtractor g, int x, int y, float delta, CallbackInfo ci) {
        if ((Object) this instanceof AbstractContainerScreen<?>) ScaleContext.end(g.pose());
    }
}
