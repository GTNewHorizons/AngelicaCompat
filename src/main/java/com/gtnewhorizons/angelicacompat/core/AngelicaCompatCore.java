package com.gtnewhorizons.angelicacompat.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import com.gtnewhorizons.angelicacompat.asm.AsmTransformers;
import com.gtnewhorizons.angelicacompat.config.PatchesConfig;
import com.gtnewhorizons.angelicacompat.loading.MixinCompatHackTweaker;
import com.gtnewhorizons.angelicacompat.mixins.Mixins;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class AngelicaCompatCore implements IFMLLoadingPlugin, IEarlyMixinLoader {

    static {
        try {
            ConfigurationManager.registerConfig(PatchesConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] transformerClasses;

    @Override
    public String getMixinConfig() {
        return "mixins.angelicacompat.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return Mixins.getEarlyMixins(loadedCoreMods);
    }

    @Override
    public String[] getASMTransformerClass() {
        final List<String> mixinTweakClasses = GlobalProperties
            .get(MixinServiceLaunchWrapper.BLACKBOARD_KEY_TWEAKCLASSES);
        if (mixinTweakClasses != null) {
            mixinTweakClasses.add(MixinCompatHackTweaker.class.getName());
        }

        if (transformerClasses == null) {
            transformerClasses = AsmTransformers.getTransformers();
        }
        return transformerClasses;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
