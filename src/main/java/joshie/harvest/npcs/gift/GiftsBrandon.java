package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsBrandon extends Gifts {
    public GiftsBrandon() {
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC), Quality.AWESOME);
        stackRegistry.register(Blocks.TORCH, Quality.GOOD);
        categoryRegistry.put(MINERAL, Quality.GOOD);
        categoryRegistry.put(PLANT, Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(MUSHROOM, Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.PANCAKE), Quality.DISLIKE);
        categoryRegistry.put(FRUIT, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.BAD);
        stackRegistry.register(Items.NETHER_STAR, Quality.BAD);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE), Quality.BAD);
        stackRegistry.register(Ore.of("flower"), Quality.BAD);
        stackRegistry.register(Ore.of("treeSapling"), Quality.TERRIBLE);
    }
}