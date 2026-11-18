package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Entity / skin / book / banner / sign previews take raw screen coordinates and a size instead of using
 * the pose. While a scaled element is drawn, convert them so they line up with the rest of the screen
 * (e.g. the player model in the inventory).
 */
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsPipMixin {

    @ModifyVariable(method = {"submitEntityRenderState", "submitSkinRenderState", "submitBookModelRenderState", "submitBannerPatternRenderState", "submitSignRenderState"},
            at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int scaler$x0(int v) {
        return ScaleContext.pipX(((GuiGraphics) (Object) this).pose(), v);
    }

    @ModifyVariable(method = {"submitEntityRenderState", "submitSkinRenderState", "submitBookModelRenderState", "submitBannerPatternRenderState", "submitSignRenderState"},
            at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int scaler$y0(int v) {
        return ScaleContext.pipY(((GuiGraphics) (Object) this).pose(), v);
    }

    @ModifyVariable(method = {"submitEntityRenderState", "submitSkinRenderState", "submitBookModelRenderState", "submitBannerPatternRenderState", "submitSignRenderState"},
            at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private int scaler$x1(int v) {
        return ScaleContext.pipX(((GuiGraphics) (Object) this).pose(), v);
    }

    @ModifyVariable(method = {"submitEntityRenderState", "submitSkinRenderState", "submitBookModelRenderState", "submitBannerPatternRenderState", "submitSignRenderState"},
            at = @At("HEAD"), argsOnly = true, ordinal = 3)
    private int scaler$y1(int v) {
        return ScaleContext.pipY(((GuiGraphics) (Object) this).pose(), v);
    }

    /** The size (first float) of the previews that have one. */
    @ModifyVariable(method = {"submitEntityRenderState", "submitSkinRenderState", "submitBookModelRenderState", "submitSignRenderState"},
            at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float scaler$scale(float v) {
        return ScaleContext.pipScale(((GuiGraphics) (Object) this).pose(), v);
    }
}
