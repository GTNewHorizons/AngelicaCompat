package com.gtnewhorizons.angelicacompat.asm.handlers;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class StacksOnStacksCompatHandler implements CompatHandler {

    @Override
    public Map<String, List<String>> getFieldLevelTessellator() {
        return ImmutableMap
            .of("com.tierzero.stacksonstacks.util.ClientUtils", ImmutableList.of("drawQuad", "drawRectangularPrism"));
    }

    @Override
    public Map<String, List<String>> getTileEntityNullGuard() {
        return ImmutableMap
            .of("com.tierzero.stacksonstacks.client.render.RenderTilePile", ImmutableList.of("renderWorldBlock"));
    }

    @Override
    public Map<String, Boolean> getThreadSafeISBRHAnnotations() {
        return ImmutableMap.of("com.tierzero.stacksonstacks.client.render.RenderTilePile", true);
    }

    @Override
    public List<String> extraTransformers() {
        return ImmutableList.of("com.gtnewhorizons.angelicacompat.asm.transformers.specific.StacksOnStacksTransformer");
    }
}
