package joshie.harvestmoon.init.cooking;

import joshie.harvestmoon.cooking.Seasoning;

public class HMSeasonings {
    public static Seasoning salt;
    
    public static void init() {
        salt = new Seasoning("salt", 0.0F, 0);
    }
}
