package com.gtnewhorizons.angelicacompat.asm;

import com.gtnewhorizons.angelicacompat.core.AngelicaCompatCore;

public class AsmUtil {

    public static String obf(String deobf, String obf) {
        if (AngelicaCompatCore.isObfEnv()) {
            return obf;
        }
        return deobf;
    }
}
