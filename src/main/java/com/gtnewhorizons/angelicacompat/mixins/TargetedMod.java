package com.gtnewhorizons.angelicacompat.mixins;

import cpw.mods.fml.common.Mod;

public enum TargetedMod {

    VANILLA("Minecraft", null),
    CAMPFIRE_BACKPORT("CampfireBackport", null, "campfirebackport"),
    EXTRA_UTILITIES("ExtraUtilities", null, "ExtraUtilities"),
    MINEFACTORY_RELOADED("MineFactory Reloaded", null, "MineFactoryReloaded"),
    XAEROS_MINIMAP("Xaeros Minimap", null, "XaeroMinimap");

    /** The "name" in the {@link Mod @Mod} annotation */
    public final String modName;
    /** Class that implements the IFMLLoadingPlugin interface */
    public final String coreModClass;
    /** The "modid" in the {@link Mod @Mod} annotation */
    public final String modId;

    TargetedMod(String modName, String coreModClass) {
        this(modName, coreModClass, null);
    }

    TargetedMod(String modName, String coreModClass, String modId) {
        this.modName = modName;
        this.coreModClass = coreModClass;
        this.modId = modId;
    }

    @Override
    public String toString() {
        return "TargetedMod{modName='" + modName + "', coreModClass='" + coreModClass + "', modId='" + modId + "'}";
    }
}
