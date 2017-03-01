package joshie.harvest.plugins.crafttweaker.wrappers;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.IRequirement;
import joshie.harvest.shops.purchasable.PurchasableMaterials;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class PurchasableWrapperMaterials extends PurchasableMaterials {
    public IPurchasable original;

    public PurchasableWrapperMaterials(PurchasableMaterials purchasable, int wood, int stone, long cost) {
        super(cost, wood, stone, null);
        this.original = purchasable;
    }

    public PurchasableWrapperMaterials(IPurchasable purchasable, long cost, IRequirement... requirements) {
        super(cost, null, requirements);
        this.original = purchasable;
    }

    @Override
    public int getStock() {
        return original.getStock();
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return original.canDo(world, player, amount);
    }

    @Override
    public boolean canList(@Nonnull World world, @Nonnull EntityPlayer player) {
        return original.canList(world, player);
    }

    @Override
    public ItemStack getDisplayStack() {
        return original.getDisplayStack();
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        original.onPurchased(player);
    }

    @Override
    public boolean isPurchasable(World world, EntityPlayer player) {
        return !(original instanceof PurchasableMaterials) || ((PurchasableMaterials)original).isPurchasable(world, player);
    }

    @Override
    public String getDisplayName() {
        return original.getDisplayName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        original.addTooltip(list);
    }

    @Override
    public String getPurchaseableID() {
        return original.getPurchaseableID();
    }
}