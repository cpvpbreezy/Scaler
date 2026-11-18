package net.cpvpbreezy.scaler.mixin;

import net.cpvpbreezy.scaler.ScaleContext;
import net.cpvpbreezy.scaler.ScaledElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Hotbar (with hearts, food, armor, XP bar) and scoreboard sidebar at their own scale. */
@Mixin(Gui.class)
public abstract class GuiMixin {
    @Inject(method = "renderHotbarAndDecorations", at = @At("HEAD"))
    private void scaler$hotbarBegin(GuiGraphics g, DeltaTracker d, CallbackInfo ci) {
        ScaleContext.begin(g.pose(), ScaledElement.HOTBAR);
    }

    @Inject(method = "renderHotbarAndDecorations", at = @At("RETURN"))
    private void scaler$hotbarEnd(GuiGraphics g, DeltaTracker d, CallbackInfo ci) {
        ScaleContext.end(g.pose());
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"))
    private void scaler$scoreBegin(GuiGraphics g, Objective o, CallbackInfo ci) {
        ScaleContext.begin(g.pose(), ScaledElement.SCOREBOARD);
    }

    @Inject(method = "displayScoreboardSidebar", at = @At("RETURN"))
    private void scaler$scoreEnd(GuiGraphics g, Objective o, CallbackInfo ci) {
        ScaleContext.end(g.pose());
    }
}
