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

import static net.minecraft.util.text.TextFormatting.WHITE;

public abstract class PurchaseableFML<I extends IForgeRegistryEntry.Impl<I>> implements IPurchaseable {
    protected I item;
    private long cost;

    public PurchaseableFML(long cost, ResourceLocation resource) {
        this.cost = cost;
        if (resource != null) {
            this.item = getRegistry().getValue(resource);
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
        ItemHelper.addToPlayerInventory(player, getDisplayStack().copy());
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + getDisplayStack().getDisplayName());
    }

    @Override
    public String getPurchaseableID() {
        return item.getRegistryName().toString().replace(":", "_");
    }
}
