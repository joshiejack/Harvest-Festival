package joshie.harvest.shops.purchasable;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.shops.requirement.Materials;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchasableObtainedMaterial extends PurchasableMaterials {
    private final ItemStack obtained;

    public PurchasableObtainedMaterial(long cost, ItemStack stack, Material material, int amount) {
        super(cost, stack, Materials.of(material, amount));
        this.obtained = HFMining.MATERIALS.getStackFromEnum(material);
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return super.canList(world, player) && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(obtained);
    }
}