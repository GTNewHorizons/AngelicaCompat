package com.gtnewhorizons.angelicacompat.loading;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import com.gtnewhorizons.angelica.config.AngelicaConfig;
import com.gtnewhorizons.angelicacompat.Common;
import com.gtnewhorizons.angelicacompat.config.PatchesConfig;

public class MixinCompatHackTweaker implements ITweaker {

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        if (AngelicaConfig.enableHudCaching && PatchesConfig.patchXaerosMinimap) {
            disableXaerosMinimapWaypointTransformer();
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {

    }

    @Override
    public String getLaunchTarget() {
        return null;
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

    private void disableXaerosMinimapWaypointTransformer() {
        try {
            LaunchClassLoader lcl = Launch.classLoader;
            Field xformersField = lcl.getClass()
                .getDeclaredField("transformers");
            xformersField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<IClassTransformer> xformers = (List<IClassTransformer>) xformersField.get(lcl);
            for (int idx = xformers.size() - 1; idx >= 0; idx--) {
                final String name = xformers.get(idx)
                    .getClass()
                    .getName();
                if (name.startsWith("xaero.common.core.transformer.GuiIngameForgeTransformer")) {
                    Common.log.info("Removing transformer " + name);
                    xformers.remove(idx);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
