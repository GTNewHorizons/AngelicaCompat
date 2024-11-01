package com.gtnewhorizons.angelicacompat.asm.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.gtnewhorizons.angelicacompat.config.PatchesConfig;

import lombok.Getter;

public enum CompatHandlers {

    STACKS_ON_STACKS(() -> PatchesConfig.patchStacksOnStacks, new StacksOnStacksCompatHandler()),
    EXTRA_UTILS(() -> PatchesConfig.patchExtraUtils, new ExtraUtilsCompatHandler()),
    THAUMCRAFT(() -> PatchesConfig.patchThaumcraft, new ThaumcraftCompatHandler()),
    THAUMIC_HORIZONS(() -> PatchesConfig.patchThaumicHorizons, new ThaumicHorizonsCompatHandler());

    private static List<CompatHandler> compatHandlers = null;

    private final Supplier<Boolean> applyIf;

    @Getter
    private final CompatHandler handler;

    CompatHandlers(Supplier<Boolean> applyIf, CompatHandler handler) {
        this.applyIf = applyIf;
        this.handler = handler;
    }

    public boolean shouldBeLoaded() {
        return applyIf.get();
    }

    public static List<CompatHandler> getHandlers() {
        if (compatHandlers != null) {
            return compatHandlers;
        }
        compatHandlers = new ArrayList<>();
        for (CompatHandlers handler : values()) {
            if (handler.shouldBeLoaded()) {
                compatHandlers.add(handler.getHandler());
            }
        }
        return compatHandlers;
    }
}
