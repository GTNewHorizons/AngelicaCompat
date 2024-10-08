package com.gtnewhorizons.angelicacompat.mixins.late.client.extrautils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import com.rwtema.extrautils.block.render.FakeRenderBlocks;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTextures;

@Mixin(RenderBlockConnectedTextures.class)
public class MixinRenderBlockConnectedTextures {

    @Unique
    private FakeRenderBlocks newFakeRender = new FakeRenderBlocks();

    /**
     * @author Cleptomania
     * @reason Thread safety compat (don't use static FakeRenderBlocks anymore)
     */
    @Overwrite(remap = false)
    public FakeRenderBlocks getFakeRender() {
        return newFakeRender;
    }
}
