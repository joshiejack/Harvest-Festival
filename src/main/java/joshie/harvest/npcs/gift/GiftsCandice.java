package joshie.harvest.npcs.gift;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.core.Size;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsCandice extends Gifts {
    public GiftsCandice() {
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL), Quality.AWESOME);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.MEDIUM), Quality.AWESOME);
        stackRegistry.register(HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.LARGE), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.MILK_HOT), Quality.AWESOME);
        stackRegistry.register(HFCooking.MEAL.getStackFromEnum(Meal.MILK_STRAWBERRY), Quality.AWESOME);
        stackRegistry.register(Items.MILK_BUCKET, Quality.AWESOME);
        categoryRegistry.put(MILK, Quality.GOOD);
        stackRegistry.register(Ore.of("cropStrawberry"), Quality.DECENT);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(MUSHROOM, Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.BUN_JAM), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.BREAD_RAISIN), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.BREAD_CURRY), Quality.DISLIKE);
        categoryRegistry.put(HERB, Quality.DISLIKE);
        categoryRegistry.put(FRUIT, Quality.BAD);
        stackRegistry.register(Items.GOLDEN_APPLE, Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropApple"), Quality.TERRIBLE);
        stackRegistry.register(Ore.of("cropWatermelon"), Quality.TERRIBLE);
    }
}
