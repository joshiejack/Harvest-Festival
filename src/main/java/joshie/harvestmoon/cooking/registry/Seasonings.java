package joshie.harvestmoon.cooking.registry;

import joshie.harvestmoon.cooking.Seasoning;

public class Seasonings {
    public static Seasoning salt;
    
    public static void init() {
        salt = new Seasoning(0.0F, 0, 1);
    }
}
