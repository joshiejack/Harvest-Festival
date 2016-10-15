package joshie.harvest.plugins;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.core.handlers.DisableHandler.VanillaSeeds;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("BiomesOPlenty")
@HFLoader(mods = "BiomesOPlenty")
public class BiomesOPlenty {
    public static final Item turnip_seeds = null;
    public static final Item turnip = null;
    public static final Block mushroom = null;

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        VanillaSeeds.BLACKLIST.register(turnip_seeds);
        HFApi.crops.registerCropProvider(new ItemStack(turnip), HFCrops.TURNIP);
        Ingredient mushroomIngredient = Ingredient.INGREDIENTS.get("mushroom");
        HFApi.cooking.register(new ItemStack(mushroom, 1, 1), mushroomIngredient);
        HFApi.cooking.register(new ItemStack(mushroom, 1, 4), mushroomIngredient);
    }
}
