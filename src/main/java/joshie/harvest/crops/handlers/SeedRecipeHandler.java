package joshie.harvest.crops.handlers;

import joshie.harvest.crops.HFCrops;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;

public class SeedRecipeHandler extends ShapelessOreRecipe {
    public SeedRecipeHandler(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world) {
        ArrayList<Object> required = new ArrayList<>(input);
        for (int x = 0; x < var1.getSizeInventory(); x++) {
            ItemStack slot = var1.getStackInSlot(x);
            if (!slot.isEmpty()) {
                boolean inRecipe = false;

                for (Object aRequired : required) {
                    boolean match = false;
                    if (aRequired instanceof ItemStack && ((ItemStack) aRequired).getItem() == HFCrops.SEEDS && slot.getItem() == HFCrops.SEEDS) {
                        match = HFCrops.SEEDS.getCropFromStack(slot) == HFCrops.SEEDS.getCropFromStack((ItemStack) aRequired);
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(aRequired);
                        break;
                    }
                }

                if (!inRecipe) {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }
}
