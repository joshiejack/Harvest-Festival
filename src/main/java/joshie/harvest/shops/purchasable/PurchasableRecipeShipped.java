package joshie.harvest.shops.purchasable;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.knowledge.HFNotes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PurchasableRecipeShipped extends PurchasableMeal {
    private final Crop crop;

    public PurchasableRecipeShipped(ResourceLocation resource, Crop crop) {
        super(150, resource);
        this.setStock(1);
        this.setNote(HFNotes.RECIPES);
        this.crop = crop;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(crop.getCropStack(1));
    }

    @Override
    public ItemStack getDisplayStack() {
        return HFCooking.RECIPE.getStackFromObject(item);
    }
}
