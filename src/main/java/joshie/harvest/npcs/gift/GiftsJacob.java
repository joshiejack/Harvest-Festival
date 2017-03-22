package joshie.harvest.npcs.gift;

import joshie.harvest.api.core.Ore;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.MEAL;

@SuppressWarnings("unused")
public class GiftsJacob extends Gifts {
    public GiftsJacob() {
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.MANTARAY), Quality.AWESOME);
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.ELECTRICRAY), Quality.AWESOME);
        stackRegistry.register(HFFishing.FISH.getStackFromEnum(Fish.STINGRAY), Quality.AWESOME);
        stackRegistry.register(Ore.of("fish"), Quality.GOOD);
        categoryRegistry.put(FISH, Quality.GOOD);
        categoryRegistry.put(FLOWER, Quality.DECENT);
        categoryRegistry.put(KNOWLEDGE, Quality.DISLIKE);
        categoryRegistry.put(JUNK, Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SANDWICH), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SANDWICH_HERB), Quality.DISLIKE);
        stackRegistry.register(MEAL.getStackFromEnum(Meal.SANDWICH_FRUIT), Quality.DISLIKE);
        stackRegistry.register(Ore.of("cropEggplant"), Quality.DISLIKE);
        categoryRegistry.put(MINERAL, Quality.BAD);
        stackRegistry.register(HFMining.MATERIALS.getStackFromEnum(Material.JUNK), Quality.TERRIBLE);
    }
}