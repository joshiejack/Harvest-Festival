package joshie.harvest.shops.purchasable;

import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchasableObtainedMaterial extends PurchasableMaterials {
    private static final ItemStack obtained = HFMining.MATERIALS.getStackFromEnum(Material.ADAMANTITE);

    public PurchasableObtainedMaterial(long cost, ItemStack stack, IRequirement... requirements) {
        super(cost, stack, requirements);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return super.canList(world, player) && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(obtained);
    }
}