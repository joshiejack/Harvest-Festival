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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class Purchasable implements IPurchasable {
    protected String resource;
    protected ItemStack stack;
    protected long cost;
    private String tooltip;
    private int stock;
    private Note note;

    public Purchasable(){}
    public Purchasable(long cost, ItemStack stack) {
        this.cost = cost;
        this.stack = stack;
        this.resource = ((cost >= 0) ? "buy:" : "sell:") + stackToString(stack);
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

    private static String stackToString(ItemStack stack) {
        if (stack == null) return "null";
        String string = stack.getItem().getRegistryName().toString().replace(":", "_");
        if (stack.getItemDamage() != 0) string = string + "_" + stack.getItemDamage();
        if (stack.getTagCompound() != null) string = string + "_" + stack.getTagCompound().hashCode();
        return string;
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        return getCost() >= 0 || InventoryHelper.hasInInventory(player, ITEM_STACK, getDisplayStack(), (getDisplayStack().getCount() * amount));
    }

    @Override
    public long getCost() {
        return cost;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public ItemStack getDisplayStack() {
        return stack;
    }

    protected ItemStack getPurchasedStack() {
        return getDisplayStack();
    }

    @Override
    public void onPurchased(EntityPlayer player) {
        if (getCost() < 0) {
            InventoryHelper.takeItemsInInventory(player, ITEM_STACK, getPurchasedStack(), getPurchasedStack().getCount());
        } else {
            SpawnItemHelper.addToPlayerInventory(player, getPurchasedStack().copy());
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