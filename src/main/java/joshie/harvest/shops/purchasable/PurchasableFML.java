package joshie.harvest.shops.purchasable;

import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static net.minecraft.util.text.TextFormatting.WHITE;

public abstract class PurchasableFML<I extends IForgeRegistryEntry.Impl<I>> implements IPurchasable {
    protected I item;
    private final long cost;
    protected String tooltip;
    private int stock;

    public PurchasableFML(long cost, ResourceLocation resource) {
        this.cost = cost;
        if (resource != null) {
            this.item = getRegistry().getValue(resource);
        }
    }

    public abstract IForgeRegistry<I> getRegistry();

    public PurchasableFML addTooltip(String tooltip) {
        this.tooltip = "harvestfestival." + tooltip + ".tooltip";
        return this;
    }

    public PurchasableFML setStock(int stock) {
        this.stock = stock;
        return this;
    }

    @Override
    public int getStock() {
        return stock != 0 ? stock: getCost() < 0 ? 10 : Integer.MAX_VALUE;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        return true;
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        if (getCost() < 0) {
            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, getDisplayStack(), getDisplayStack().stackSize);
        } else {
            SpawnItemHelper.addToPlayerInventory(player, getDisplayStack().copy());
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + getDisplayStack().getDisplayName());
        if (this.tooltip != null) {
            list.add("---------------------------");
            String tooltip = WordUtils.wrap(TextHelper.localize(this.tooltip.toLowerCase(Locale.ENGLISH)), 40);
            list.addAll(Arrays.asList(tooltip.split("\r\n")));
        }
    }

    @Override
    public String getPurchaseableID() {
        return ((cost >= 0) ? "buy: " : "sell: ") + item.getRegistryName().toString().replace(":", "_");
    }
}
