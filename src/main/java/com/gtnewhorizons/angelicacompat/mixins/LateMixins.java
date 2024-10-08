package com.gtnewhorizons.angelicacompat.mixins;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import java.util.List;
import java.util.Set;

@LateMixin
public class LateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.angelicacompat.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        return Mixins.getLateMixins(loadedMods);
    }
}
