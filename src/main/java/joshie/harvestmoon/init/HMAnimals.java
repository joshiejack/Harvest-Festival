package joshie.harvestmoon.init;

import static joshie.harvestmoon.animals.AnimalType.registerFoodAsType;
import static joshie.harvestmoon.animals.AnimalType.registerFoodsAsType;
import joshie.harvestmoon.animals.AnimalType.FoodType;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HMAnimals {
    public static void preInit() {
        registerFoodsAsType(FoodType.CHICKEN, Items.chicken, Items.cooked_chicken);
        registerFoodsAsType(FoodType.FISH, Items.fish, Items.cooked_fished);
        registerFoodsAsType(FoodType.FRUIT, Items.apple, Items.melon);
        registerFoodsAsType(FoodType.GRASS, Items.wheat);
        registerFoodsAsType(FoodType.REDMEAT, Items.porkchop, Items.beef, Items.cooked_porkchop, Items.cooked_beef);
        registerFoodsAsType(FoodType.SEED, Items.melon_seeds, Items.wheat_seeds, Items.pumpkin_seeds);
        registerFoodsAsType(FoodType.VEGETABLE, Items.carrot);
        registerFoodAsType(new ItemStack(HMItems.general, 1, ItemGeneral.CHICKEN_FEED), FoodType.SEED);
    }

    public static void init() {
        for (Crop crop : Crop.crops) {
            registerFoodAsType(crop.getCropStack(), crop.getFoodType());
        }
    }
}
