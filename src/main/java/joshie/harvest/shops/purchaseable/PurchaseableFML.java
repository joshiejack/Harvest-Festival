package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.helpers.generic.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public abstract class PurchaseableFML<I extends IForgeRegistryEntry.Impl<I>> implements IPurchaseable {
    protected I item;
    private long cost;

    public PurchaseableFML(long cost, String meal) {
        this.cost = cost;
        if (!meal.equals("")) {
            this.item = getRegistry().getValue(new ResourceLocation(MODID, meal));
        }
    }

    public abstract IForgeRegistry<I> getRegistry();

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return canBuy(world, player);
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        ItemHelper.addToPlayerInventory(player, getDisplayStack());
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {}
}
