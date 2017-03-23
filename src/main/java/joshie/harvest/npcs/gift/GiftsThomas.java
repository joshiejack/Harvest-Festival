package joshie.harvest.npcs.gift;


import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsThomas extends Gifts {
    public GiftsThomas() {
        stackRegistry.register(Blocks.TNT, Quality.AWESOME);
        stackRegistry.register(Ore.of("gunpowder"), Quality.AWESOME);
        stackRegistry.register(Items.TNT_MINECART, Quality.AWESOME);
        categoryRegistry.put(MEAT, Quality.GOOD);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.DOUGHNUT), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.ICE_CREAM), Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(MUSHROOM, Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_MATSUTAKE), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_MUSHROOM), Quality.DISLIKE);
        categoryRegistry.put(MAGIC, Quality.BAD);
        categoryRegistry.put(KNOWLEDGE, Quality.BAD);
        stackRegistry.register(Items.POTIONITEM, Quality.TERRIBLE);
        stackRegistry.register(Items.LINGERING_POTION, Quality.TERRIBLE);
        stackRegistry.register(Items.SPLASH_POTION, Quality.TERRIBLE);
        stackRegistry.register(Items.EXPERIENCE_BOTTLE, Quality.TERRIBLE);
        stackRegistry.register(Items.ENCHANTED_BOOK, Quality.TERRIBLE);
    }
}