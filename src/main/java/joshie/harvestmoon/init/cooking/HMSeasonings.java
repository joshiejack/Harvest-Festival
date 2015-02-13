package joshie.harvestmoon.init.cooking;

import static joshie.harvestmoon.cooking.FoodRegistry.register;
import joshie.harvestmoon.cooking.Seasoning;
import joshie.harvestmoon.init.HMItems;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class HMSeasonings {
    public static Seasoning salt;
    public static Seasoning sugar;

    public static void init() {
        addSeasonings();
        assignSeasonings();
    }

    private static void addSeasonings() {
        salt = new Seasoning("salt", 0.01F, 0);
        sugar = new Seasoning("sugar", 0.0F, 1);
    }

    private static void assignSeasonings() {
        register(new ItemStack(HMItems.general, 1, ItemGeneral.SALT), salt);
        register(new ItemStack(Items.sugar, 1, OreDictionary.WILDCARD_VALUE), sugar);
    }
}
