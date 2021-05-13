package uk.joshiejack.economy.shop;

import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Random;

public class MaterialCost {
    private static final Random rand = new Random();
    private final Holder holder;
    private int cost;

    public MaterialCost(String item, int cost) {
        this.holder = Holder.getFromString(item);
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public ItemStack getIcon() {
        NonNullList<ItemStack> list = holder.getStacks();
        ItemStack icon = list.size() == 0 ? new ItemStack(Items.BAKED_POTATO) : list.get(rand.nextInt(list.size()));
        icon.setCount(cost);
        return icon;
    }

    public NonNullList<ItemStack> getStacks() {
        return holder.getStacks();
    }

    public boolean isMet(EntityPlayer player, int amount) {
        return InventoryHelper.hasInInventory(player, holder, (cost * amount));
    }

    public void fulfill(EntityPlayer player) {
        InventoryHelper.takeItemsInInventory(player, holder, cost);
    }
}
