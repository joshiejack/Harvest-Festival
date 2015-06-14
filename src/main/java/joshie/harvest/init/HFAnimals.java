package joshie.harvest.init;

import joshie.harvest.animals.AnimalRegistry;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;
import joshie.harvest.items.ItemGeneral;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HFAnimals {
    public static void preInit() {
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.CHICKEN, Items.chicken, Items.cooked_chicken);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.FISH, Items.fish, Items.cooked_fished);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.FRUIT, Items.apple, Items.melon);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.GRASS, Items.wheat);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.REDMEAT, Items.porkchop, Items.beef, Items.cooked_porkchop, Items.cooked_beef);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.SEED, Items.melon_seeds, Items.wheat_seeds, Items.pumpkin_seeds);
        AnimalRegistry.registerFoodsAsType(AnimalFoodType.VEGETABLE, Items.carrot);
        HFApi.ANIMALS.registerFoodAsType(new ItemStack(HFItems.general, 1, ItemGeneral.CHICKEN_FEED), AnimalFoodType.SEED);
    }

    public static void init() {
        for (ICrop crop : Crop.crops) {
            HFApi.ANIMALS.registerFoodAsType(crop.getCropStack(), crop.getFoodType());
        }
    }
}
