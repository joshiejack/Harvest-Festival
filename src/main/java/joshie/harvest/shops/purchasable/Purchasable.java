package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class Purchasable implements IPurchasable {
    protected final ItemStack stack;
    private final String resource;
    private final long cost;
    private String tooltip;
    private int stock;
    private Note note;

    public Purchasable(long cost, ItemStack stack) {
        this.cost = cost;
        this.stack = stack;
        this.resource = ((cost >= 0) ? "buy: " : "sell: ") + stackToString(stack);
    }

    public Purchasable addTooltip(String tooltip) {
        this.tooltip = "harvestfestival." + tooltip + ".tooltip";
        return this;
    }

    public Purchasable setStock(int stock) {
        this.stock = stock;
        return this;
    }

    public Purchasable setNote(Note note) {
        this.note = note;
        return this;
    }

    static String stackToString(ItemStack stack) {
        String string = stack.getItem().getRegistryName().toString();
        if (stack.getItemDamage() != 0) string = string + " " + stack.getItemDamage();
        if (stack.getTagCompound() != null) string = string + " " + stack.getTagCompound().hashCode();
        return string;
    }

    @Override
    public boolean canDo(World world, EntityPlayer player, int amount) {
        if (getCost() < 0) {
            return InventoryHelper.hasInInventory(player, ITEM_STACK, getDisplayStack(), (getDisplayStack().stackSize * amount));
        }

        return true;
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public int getStock() {
        return stock != 0 ? stock: getCost() < 0 ? 10 : Integer.MAX_VALUE;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stack;
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        if (getCost() < 0) {
            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, getDisplayStack(), getDisplayStack().stackSize);
        } else {
            SpawnItemHelper.addToPlayerInventory(player, getDisplayStack().copy());
        }

        if (note != null) HFApi.player.getTrackingForPlayer(player).learnNote(note);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        if (this.tooltip != null) {
            list.add(TextFormatting.AQUA + getDisplayName());
            list.add("---------------------------");
            String tooltip = WordUtils.wrap(TextHelper.localize(this.tooltip.toLowerCase(Locale.ENGLISH)), 40);
            list.addAll(Arrays.asList(tooltip.split("\r\n")));
        } else list.add(TextFormatting.WHITE + getDisplayName());
    }

    @Override
    public String getPurchaseableID() {
        return resource;
    }
}