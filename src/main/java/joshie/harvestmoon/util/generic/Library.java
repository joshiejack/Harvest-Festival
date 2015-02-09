package joshie.harvestmoon.util.generic;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Library {
    public static boolean DEBUG_ON;
    private static final Logger logger = LogManager.getLogger("Joshie-Lib");

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
