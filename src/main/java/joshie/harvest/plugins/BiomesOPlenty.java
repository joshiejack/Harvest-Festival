package joshie.harvest.plugins;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static joshie.harvest.crops.HFCrops.DISABLE_VANILLA_MOISTURE;

@ObjectHolder("BiomesOPlenty")
@HFLoader(mods = "BiomesOPlenty")
@SuppressWarnings("unused")
public class BiomesOPlenty {
    public static final Item turnip_seeds = null;
    public static final Item turnip = null;
    public static final Block mushroom = null;
    public static final Block farmland_0 = null;
    public static final Block farmland_1 = null;
    public static final Block dirt = null;

    @SuppressWarnings("ConstantConditions, deprecation")
    public static void init() {
        DisableHandler.SEEDS_BLACKLIST.register(turnip_seeds);
        HFApi.crops.registerCropProvider(new ItemStack(turnip), HFCrops.TURNIP);
        Ingredient mushroomIngredient = Ingredient.INGREDIENTS.get("mushroom");
        HFApi.cooking.register(new ItemStack(mushroom, 1, 1), mushroomIngredient);
        HFApi.cooking.register(new ItemStack(mushroom, 1, 4), mushroomIngredient);
        if (DISABLE_VANILLA_MOISTURE) {
            farmland_0.setTickRandomly(false);
            farmland_1.setTickRandomly(false);
         }

        HFApi.crops.registerFarmlandToDirtMapping(farmland_0.getStateFromMeta(0), dirt.getStateFromMeta(0));
        HFApi.crops.registerFarmlandToDirtMapping(farmland_0.getStateFromMeta(8), dirt.getStateFromMeta(1));
        HFApi.crops.registerFarmlandToDirtMapping(farmland_1.getStateFromMeta(0), dirt.getStateFromMeta(2));
    }
}
