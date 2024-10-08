package com.gtnewhorizons.angelicacompat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Common {

    public static final Logger log = LogManager.getLogger("AngelicaCompat");

    static {
        Common.log.info("Initializing AngelicaCompat");
    }
}
