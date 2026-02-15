package willow.train.kuayue.mixins.mixin;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import willow.train.kuayue.initial.AllCompatMods;

import java.util.*;
import java.util.function.Predicate;

public class KuayueMixinPlugin implements IMixinConfigPlugin {

    protected final Map<String, Predicate<Void>> mixinConditions = new HashMap<>();

    public KuayueMixinPlugin() {
        mixinConditions.put("MixinRailwaysTrainUtils", v -> AllCompatMods.isRailwaysPresent());
        mixinConditions.put("MixinScheduleItem", v -> !AllCompatMods.isRailwaysPresent());
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String simpleString = mixinClassName.substring(mixinClassName.lastIndexOf(".") + 1);

        boolean b = Optional.ofNullable(mixinConditions.get(simpleString))
                .map(condition -> condition.test(null))
                .orElse(
                        Optional.ofNullable(mixinConditions.get(mixinClassName))
                                .map(condition -> condition.test(null))
                                .orElse(true)
                );
        return b;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    public static boolean isModLoaded(String modId) {
        boolean isLoaded = false;
        try {
            isLoaded = FMLLoader.getLoadingModList().getModFileById(modId) != null;
        } catch (NullPointerException e) {
            System.out.println("Failed to check mod " + modId + " presence.");
        }
        return isLoaded;
    }
}
