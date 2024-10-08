package com.gtnewhorizons.angelicacompat.asm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.gtnewhorizon.gtnhmixins.core.GTNHMixinsCore;
import com.gtnewhorizons.angelicacompat.Common;
import com.gtnewhorizons.angelicacompat.config.PatchesConfig;
import com.gtnewhorizons.angelicacompat.mixins.TargetedMod;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLLaunchHandler;

public enum AsmTransformers {

    EXTRA_UTILITIES_ISBRH("Extra Utilities ThreadSafeISBRH Transformer", () -> PatchesConfig.patchExtraUtils,
        Side.CLIENT, "com.gtnewhorizons.angelicacompat.asm.transformers.ExtraUtilsTransformer"),
    CAMPFIRE_BACKPORT_ISBRH("Campfire Backport ThreadSafeISBRH Transformer", () -> PatchesConfig.patchCampfireBackport,
        Side.CLIENT, "com.gtnewhorizons.angelicacompat.asm.transformers.CampfireBackportTransformer");

    private final Supplier<Boolean> applyIf;
    private final Side side;
    private final String[] transformerClasses;
    private final List<TargetedMod> targetedMods;
    private final List<TargetedMod> excludedMods;

    AsmTransformers(@SuppressWarnings("unused") String description, Supplier<Boolean> applyIf, Side side,
        String... transformers) {
        this.applyIf = applyIf;
        this.side = side;
        this.transformerClasses = transformers;
        this.targetedMods = new ArrayList<>();
        this.excludedMods = new ArrayList<>();
    }

    AsmTransformers(@SuppressWarnings("unused") String description, Supplier<Boolean> applyIf, Side side,
        List<TargetedMod> targetedMods, List<TargetedMod> excludedMods, String... transformers) {
        this.applyIf = applyIf;
        this.side = side;
        this.transformerClasses = transformers;
        this.targetedMods = targetedMods;
        this.excludedMods = excludedMods;
    }

    private boolean shouldBeLoaded(Set<String> loadedCoreMods) {
        if (loadedCoreMods == null) loadedCoreMods = Collections.emptySet();

        return applyIf.get() && shouldLoadSide()
            && allModsLoaded(targetedMods, loadedCoreMods)
            && noModsLoaded(excludedMods, loadedCoreMods);
    }

    private boolean allModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods) {
        // If no mods are targeted, we're good to go
        if (targetedMods == null || targetedMods.isEmpty()) return true;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && !loadedCoreMods.contains(target.coreModClass)) return false;
        }

        return true;
    }

    private boolean noModsLoaded(List<TargetedMod> targetedMods, Set<String> loadedCoreMods) {
        if (targetedMods == null || targetedMods.isEmpty()) return true;

        for (TargetedMod target : targetedMods) {
            if (target == TargetedMod.VANILLA) continue;

            if (!loadedCoreMods.isEmpty() && target.coreModClass != null
                && loadedCoreMods.contains(target.coreModClass)) return false;
        }

        return true;
    }

    private boolean shouldLoadSide() {
        return side == Side.BOTH || (side == Side.SERVER && FMLLaunchHandler.side()
            .isServer())
            || (side == Side.CLIENT && FMLLaunchHandler.side()
                .isClient());
    }

    @SuppressWarnings("unchecked")
    public static String[] getTransformers() {
        Set<String> coremods;
        // Gross. Also Note - this will only catch coremods loaded by the time this is called.
        try {
            Field loadPlugins = CoreModManager.class.getDeclaredField("loadPlugins");
            loadPlugins.setAccessible(true);
            List<?> loadedCoremods = (List<?>) loadPlugins.get(null);

            Class<?> mixinCore = Class.forName("com.gtnewhorizon.gtnhmixins.core.GTNHMixinsCore");
            Method getLoadedCoremods = mixinCore.getDeclaredMethod("getLoadedCoremods", List.class);
            getLoadedCoremods.setAccessible(true);
            // Might change some class loading behavior, but it's set to load pretty early so it should be fine
            // However I didn't notice GTNHMixinsCore in the loaded core mods... (but did notice the Unimixins-All
            // coremod)
            coremods = (Set<String>) getLoadedCoremods.invoke(new GTNHMixinsCore(), loadedCoremods);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
            | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        final List<String> transformerList = new ArrayList<>();
        for (AsmTransformers transformer : values()) {
            if (transformer.shouldBeLoaded(coremods)) {
                Common.log.info("Loading transformer {}", (Object[]) transformer.transformerClasses);
                transformerList.addAll(Arrays.asList(transformer.transformerClasses));
            } else {
                Common.log.info("Not loading transformer {}", (Object[]) transformer.transformerClasses);
            }
        }
        return transformerList.toArray(new String[0]);
    }

    private enum Side {
        BOTH,
        CLIENT,
        SERVER
    }
}
