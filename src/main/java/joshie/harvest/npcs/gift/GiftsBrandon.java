package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemIngredients.Ingredient;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsBrandon extends Gifts {
    public GiftsBrandon() {
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL), Quality.AWESOME);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.MYTHIC), Quality.AWESOME);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_FRIED), Quality.AWESOME);
        stackRegistry.register(Ore.of("torch"), Quality.GOOD);
        stackRegistry.register(HFMining.LADDER, Quality.GOOD);
        stackRegistry.register(HFMining.MINING_TOOL, Quality.GOOD);
        categoryRegistry.put(MINERAL, Quality.GOOD);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.RICEBALL), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_PINEAPPLE), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_GRAPE), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_PEACH), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_BANANA), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_ORANGE), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_APPLE), Quality.DECENT);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.JUICE_FRUIT), Quality.DECENT);
        categoryRegistry.put(ART, Quality.DISLIKE);
        categoryRegistry.put(PLANT, Quality.DISLIKE);
        categoryRegistry.put(FLOWER, Quality.DISLIKE);
        categoryRegistry.put(MUSHROOM, Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SANDWICH_FRUIT), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.CUCUMBER_PICKLED), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_MATSUTAKE), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.RICE_MUSHROOM), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.PANCAKE), Quality.DISLIKE);
        categoryRegistry.put(FRUIT, Quality.DISLIKE);
        categoryRegistry.put(FISH, Quality.BAD);
        stackRegistry.register(Ore.of("netherStar"), Quality.BAD);
        stackRegistry.register(HFCooking.INGREDIENTS.getStackFromEnum(Ingredient.WINE), Quality.BAD);
        stackRegistry.register(Ore.of("flower"), Quality.BAD);
        stackRegistry.register(Ore.of("treeSapling"), Quality.TERRIBLE);
    }
}