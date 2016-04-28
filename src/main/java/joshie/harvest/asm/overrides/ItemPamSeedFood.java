package joshie.harvest.asm.overrides;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.plugins.harvestcraft.HarvestCraft;
import joshie.harvest.plugins.harvestcraft.HarvestCraftCrop;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ItemPamSeedFood {
    private static Cache<String, HarvestCraftCrop> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
    public static boolean isLoaded = false;

    public static ICrop getCrop(final ItemStack stack) {
        try {
            final String unlocalized = stack.getUnlocalizedName();
            return cache.get(unlocalized, new Callable<HarvestCraftCrop>() {
                @Override
                public HarvestCraftCrop call() {
                    for (HarvestCraftCrop crop : HarvestCraft.crops) {
                        if (crop.matches(stack)) {
                            cache.put(unlocalized, crop);
                            return crop;
                        }
                    }

                    return null; //If we failed to find the crop, return null
                }
            });
        } catch (ExecutionException e) {
            return null; //Return null on failure
        }
    }

    public static long getSellValue(ItemStack stack) {
        ICrop crop = getCrop(stack);
        return crop == null ? 0 : crop.getSellValue();
    }

    @SideOnly(Side.CLIENT)
    public static void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }
}