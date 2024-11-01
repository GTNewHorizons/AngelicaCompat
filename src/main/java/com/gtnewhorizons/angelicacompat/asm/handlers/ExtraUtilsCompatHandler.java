package com.gtnewhorizons.angelicacompat.asm.handlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class ExtraUtilsCompatHandler implements CompatHandler {

    @Override
    public Map<String, Boolean> getThreadSafeISBRHAnnotations() {
        return ImmutableMap.of(
            "com.rwtema.extrautils.block.render.RenderBlockColor",
            false,
            "com.rwtema.extrautils.block.render.RenderBlockConnectedTextures",
            true,
            "com.rwtema.extrautils.block.render.RenderBlockConnectedTexturesEthereal",
            true,
            "com.rwtema.extrautils.block.render.RenderBlockFullBright",
            false,
            "com.rwtema.extrautils.block.render.RenderBlockSpike",
            false);
    }

}
