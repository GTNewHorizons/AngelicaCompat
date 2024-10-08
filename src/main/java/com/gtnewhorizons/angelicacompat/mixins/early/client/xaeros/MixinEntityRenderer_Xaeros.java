package com.gtnewhorizons.angelicacompat.mixins.early.client.xaeros;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.client.GuiIngameForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gtnewhorizons.angelicacompat.ModStatus;
import com.llamalad7.mixinextras.sugar.Local;

import xaero.common.core.XaeroMinimapCore;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer_Xaeros {

    @Shadow
    private Minecraft mc;

    @Inject(
        method = "updateCameraAndRender",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(FZII)V"))
    public void angelicacompat$callXaeros(CallbackInfo ci, @Local(ordinal = 0) float partialTicks) {
        if (ModStatus.isXaerosMinimapLoaded && mc.ingameGUI instanceof GuiIngameForge) {
            // this used to be called by asming into renderGameOverlay, but we removed it
            XaeroMinimapCore.beforeIngameGuiRender(partialTicks);
        }
    }
}
