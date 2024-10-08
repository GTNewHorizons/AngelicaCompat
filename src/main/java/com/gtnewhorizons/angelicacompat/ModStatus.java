package com.gtnewhorizons.angelicacompat;

import com.gtnewhorizons.angelicacompat.mixins.TargetedMod;

import cpw.mods.fml.common.Loader;

public class ModStatus {

    public static boolean isXaerosMinimapLoaded;

    public static void preInit() {
        isXaerosMinimapLoaded = Loader.isModLoaded(TargetedMod.XAEROS_MINIMAP.modId);
    }
}
