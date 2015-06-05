package joshie.harvest.init;

import static joshie.harvest.animals.AnimalType.registerFoodAsType;
import static joshie.harvest.animals.AnimalType.registerFoodsAsType;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;
import joshie.harvest.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HFAnimals {
    public static void preInit() {
        registerFoodsAsType(AnimalFoodType.CHICKEN, Items.chicken, Items.cooked_chicken);
        registerFoodsAsType(AnimalFoodType.FISH, Items.fish, Items.cooked_fished);
        registerFoodsAsType(AnimalFoodType.FRUIT, Items.apple, Items.melon);
        registerFoodsAsType(AnimalFoodType.GRASS, Items.wheat);
        registerFoodsAsType(AnimalFoodType.REDMEAT, Items.porkchop, Items.beef, Items.cooked_porkchop, Items.cooked_beef);
        registerFoodsAsType(AnimalFoodType.SEED, Items.melon_seeds, Items.wheat_seeds, Items.pumpkin_seeds);
        registerFoodsAsType(AnimalFoodType.VEGETABLE, Items.carrot);
        registerFoodAsType(new ItemStack(HFItems.general, 1, ItemGeneral.CHICKEN_FEED), AnimalFoodType.SEED);
    }

    public static void init() {
        for (ICrop crop : Crop.crops) {
            registerFoodAsType(crop.getCropStack(), crop.getFoodType());
        }
    }
}
