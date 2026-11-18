package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Entity / book / banner previews take raw screen coordinates and a size instead of using the pose.
 * While a scaled element is drawn, convert them so they line up with the rest of the screen
 * (e.g. the player model in the inventory).
 */
@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsPipMixin {
    @ModifyVariable(method = {"entity", "book", "bannerPattern"}, at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int scaler$x0(int v) {
        return ScaleContext.pipX(((GuiGraphicsExtractor) (Object) this).pose(), v);
    }

    @ModifyVariable(method = {"entity", "book", "bannerPattern"}, at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int scaler$y0(int v) {
        return ScaleContext.pipY(((GuiGraphicsExtractor) (Object) this).pose(), v);
    }

    @ModifyVariable(method = {"entity", "book", "bannerPattern"}, at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private int scaler$x1(int v) {
        return ScaleContext.pipX(((GuiGraphicsExtractor) (Object) this).pose(), v);
    }

    @ModifyVariable(method = {"entity", "book", "bannerPattern"}, at = @At("HEAD"), argsOnly = true, ordinal = 3)
    private int scaler$y1(int v) {
        return ScaleContext.pipY(((GuiGraphicsExtractor) (Object) this).pose(), v);
    }

    /** The size (first float) of the previews that have one. */
    @ModifyVariable(method = {"entity", "book"}, at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float scaler$scale(float v) {
        return ScaleContext.pipScale(((GuiGraphicsExtractor) (Object) this).pose(), v);
    }
}
