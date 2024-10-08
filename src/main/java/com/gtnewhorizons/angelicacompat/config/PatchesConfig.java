package com.gtnewhorizons.angelicacompat.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = "angelicacompat", category = "patches")
public class PatchesConfig {

    @Config.Comment("Extra Utilities")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean patchExtraUtils;

    @Config.Comment("Campfire Backport")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean patchCampfireBackport;

    @Config.Comment("MineFactory Reloaded")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean patchMFR;

    @Config.Comment("Xaeros Minimap")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean patchXaerosMinimap;

}
