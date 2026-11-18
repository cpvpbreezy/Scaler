package net.cpvpbreezy.scaler;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/** Picks the HUD mixin for the running game: 26.1.x draws the HUD in Gui, 26.2+ in Hud. */
public final class ScalerMixinPlugin implements IMixinConfigPlugin {
    private boolean hudClass;

    @Override
    public void onLoad(String mixinPackage) {
        try {
            Version mc = FabricLoader.getInstance().getModContainer("minecraft").orElseThrow().getMetadata().getVersion();
            hudClass = mc.compareTo(SemanticVersion.parse("26.2")) >= 0;
        } catch (Exception e) {
            hudClass = true;
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith(".HudMixin261")) return !hudClass;
        if (mixinClassName.endsWith(".HudMixin262")) return hudClass;
        return true;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
