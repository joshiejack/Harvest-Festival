package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.gathering.HFGathering;
import joshie.harvest.gathering.block.BlockNature.NaturalBlock;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Items;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsJamie extends Gifts {
    public GiftsJamie() {
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.ALEXANDRITE), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), Quality.AWESOME);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE), Quality.AWESOME);
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.ANGLER), Quality.GOOD);
        stackRegistry.register(Ore.of("cropPineapple"), Quality.GOOD);
        stackRegistry.register(Ore.of("cropCabbage"), Quality.GOOD);
        stackRegistry.register(Ore.of("cropBanana"), Quality.GOOD);
        categoryRegistry.put(MONSTER, Quality.GOOD);
        categoryRegistry.put(KNOWLEDGE, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.PIRANHA), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CUCUMBER_PICKLED), Quality.DISLIKE);
        stackRegistry.register(Ore.of("cropCucumber"), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_MATSUTAKE), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_MUSHROOM), Quality.DISLIKE);
        categoryRegistry.put(MUSHROOM, Quality.DISLIKE);
        categoryRegistry.put(MAGIC, Quality.DISLIKE);
        stackRegistry.register(Ore.of("cropWatermelon"), Quality.BAD);
        stackRegistry.register(Items.STICK, Quality.TERRIBLE);
        stackRegistry.register(HFGathering.NATURE.getStackFromEnum(NaturalBlock.MATSUTAKE), Quality.TERRIBLE);
        stackRegistry.register(HFCore.FLOWERS.getStackFromEnum(FlowerType.WEED), Quality.TERRIBLE);
    }
}