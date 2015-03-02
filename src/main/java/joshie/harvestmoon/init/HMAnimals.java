package joshie.harvestmoon.init;

import static joshie.harvestmoon.animals.AnimalType.registerFoodAsType;
import static joshie.harvestmoon.animals.AnimalType.registerFoodsAsType;
import joshie.harvestmoon.api.AnimalFoodType;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HMAnimals {
    public static void preInit() {
        registerFoodsAsType(AnimalFoodType.CHICKEN, Items.chicken, Items.cooked_chicken);
        registerFoodsAsType(AnimalFoodType.FISH, Items.fish, Items.cooked_fished);
        registerFoodsAsType(AnimalFoodType.FRUIT, Items.apple, Items.melon);
        registerFoodsAsType(AnimalFoodType.GRASS, Items.wheat);
        registerFoodsAsType(AnimalFoodType.REDMEAT, Items.porkchop, Items.beef, Items.cooked_porkchop, Items.cooked_beef);
        registerFoodsAsType(AnimalFoodType.SEED, Items.melon_seeds, Items.wheat_seeds, Items.pumpkin_seeds);
        registerFoodsAsType(AnimalFoodType.VEGETABLE, Items.carrot);
        registerFoodAsType(new ItemStack(HMItems.general, 1, ItemGeneral.CHICKEN_FEED), AnimalFoodType.SEED);
    }

    public static void init() {
        for (ICrop crop : Crop.crops) {
            registerFoodAsType(crop.getCropStack(), crop.getFoodType());
        }
    }
}
