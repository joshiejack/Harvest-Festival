package joshie.harvest.core.config;


import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;

public class General {
    public static boolean DEBUG_MODE = true;
    public static int OVERWORLD_ID;
    public static int MINING_ID;
    
    public static void init () {
        OVERWORLD_ID = getInteger("Overworld ID", 3);
        MINING_ID = getInteger("Mining World ID", 4);
    }
}
