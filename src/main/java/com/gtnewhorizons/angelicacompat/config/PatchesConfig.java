package com.gtnewhorizons.angelicacompat.config;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = "angelicacompat", category = "patches")
public class PatchesConfig {

    @Config.Comment("Extra Utilities")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean patchExtraUtils;

}
