package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.cpvpbreezy.scaler.ScaledElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hotbar (with hearts, food, armor, XP bar) and scoreboard sidebar at their own scale.
 * Minecraft 26.1.x keeps the HUD in {@code Gui}; 26.2+ moved it to {@code Hud} (see {@link HudMixin262}).
 * {@link net.cpvpbreezy.scaler.ScalerMixinPlugin} applies only the one that matches the game.
 */
@Pseudo
@Mixin(targets = "net.minecraft.client.gui.Gui")
public abstract class HudMixin261 {
    @Inject(method = "extractHotbarAndDecorations", at = @At("HEAD"))
    private void scaler$hotbarBegin(GuiGraphicsExtractor g, DeltaTracker d, CallbackInfo ci) {
        ScaleContext.begin(g.pose(), ScaledElement.HOTBAR);
    }

    @Inject(method = "extractHotbarAndDecorations", at = @At("RETURN"))
    private void scaler$hotbarEnd(GuiGraphicsExtractor g, DeltaTracker d, CallbackInfo ci) {
        ScaleContext.end(g.pose());
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"))
    private void scaler$scoreBegin(GuiGraphicsExtractor g, Objective o, CallbackInfo ci) {
        ScaleContext.begin(g.pose(), ScaledElement.SCOREBOARD);
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("RETURN"))
    private void scaler$scoreEnd(GuiGraphicsExtractor g, Objective o, CallbackInfo ci) {
        ScaleContext.end(g.pose());
    }
}
