package joshie.harvest.player.tracking;

import joshie.harvest.core.util.holders.CropSoldStack;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.core.util.holders.SellHolderStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class Tracking {
    protected Set<CropSoldStack> cropTracker = new HashSet<>(); //Crops that have been harvested
    protected Set<SellHolderStack> sellTracker = new HashSet<>(); //Items That have been sold
    protected Set<ItemStackHolder> obtained = new HashSet<>(); //Items that have been obtained
    protected Set<ResourceLocation> recipes = new HashSet<>(); //Recipe Learnt

    public void addAsObtained(ItemStack stack) {
        obtained.add(ItemStackHolder.of(stack));
    }

    public boolean hasObtainedItem(ItemStack stack) {
        for (ItemStackHolder holder: obtained) {
            if (holder.matches(stack)) return true;
        }

        return false;
    }
}