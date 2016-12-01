package joshie.harvest.plugins;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import static joshie.harvest.crops.HFCrops.DISABLE_VANILLA_MOISTURE;

@ObjectHolder("BiomesOPlenty")
@HFLoader(mods = "BiomesOPlenty")
public class BiomesOPlenty {
    public static final Item turnip_seeds = null;
    public static final Item turnip = null;
    public static final Block mushroom = null;
    public static final Block farmland_0 = null;
    public static final Block farmland_1 = null;

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        DisableHandler.BLACKLIST.register(turnip_seeds);
        HFApi.crops.registerCropProvider(new ItemStack(turnip), HFCrops.TURNIP);
        Ingredient mushroomIngredient = Ingredient.INGREDIENTS.get("mushroom");
        HFApi.cooking.register(new ItemStack(mushroom, 1, 1), mushroomIngredient);
        HFApi.cooking.register(new ItemStack(mushroom, 1, 4), mushroomIngredient);
        if (DISABLE_VANILLA_MOISTURE) {
            farmland_0.setTickRandomly(false);
            farmland_1.setTickRandomly(false);
        }
    }
}
