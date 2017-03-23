package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsDaniel extends Gifts {
    public GiftsDaniel() {
        stackRegistry.register(Ore.of("enderpearl"), Quality.AWESOME);
        stackRegistry.register(Items.ENDER_EYE, Quality.AWESOME);
        stackRegistry.register(Items.CHORUS_FRUIT, Quality.AWESOME);
        stackRegistry.register(Items.DRAGON_BREATH, Quality.AWESOME);
        stackRegistry.register(Blocks.DRAGON_EGG, Quality.AWESOME);
        categoryRegistry.put(MINERAL, Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.DECENT);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.BAD);
        stackRegistry.register(new ItemStack(Items.COOKED_FISH, 1, 0), Quality.TERRIBLE);
        stackRegistry.register(new ItemStack(Items.COOKED_FISH, 1, 1), Quality.TERRIBLE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.FISH_GRILLED), Quality.TERRIBLE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.FISHSTICKS), Quality.TERRIBLE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.STEW_FISH), Quality.TERRIBLE);
    }
}