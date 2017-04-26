package joshie.harvest.shops.purchasable;

import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PurchasableObtainedMaterial extends PurchasableMaterials {
    private static final ItemStack OBTAINED = HFMining.MATERIALS.getStackFromEnum(Material.ADAMANTITE);

    public PurchasableObtainedMaterial(long cost, @Nonnull ItemStack stack, IRequirement... requirements) {
        super(cost, stack, requirements);
    }

    @Override
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return super.canList(world, player) && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(OBTAINED);
    }
}