package uk.joshiejack.penguinlib.util.helpers.minecraft;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

public class InventoryHelper {
    public static boolean isHolding(EntityPlayer player, Holder search, int count) {
        return getCountHeld(player, search) >= count;
    }

    private static int getCountHeld(EntityPlayer player, Holder search) {
        if (search.matches(player.getHeldItemMainhand())) {
            return player.getHeldItemMainhand().getCount();
        } else return 0;
    }

    public static boolean hasInInventory(EntityPlayer player, ItemStack stack, int count) {
        return hasInInventory(player, new HolderMeta(stack), count);
    }

    public static boolean hasInInventory(EntityPlayer player, Holder search, int count) {
        return getCount(player, search) >= count;
    }

    public static boolean hasInInventory(IItemHandler handler, ItemStack stack, int count) {
        return getCount(handler, new HolderMeta(stack)) >= count;
    }

    public static boolean hasInInventory(IItemHandler handler, Holder search, int count) {
        return getCount(handler, search) >= count;
    }

    private static int getCount(IItemHandler handler, Holder search) {
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty() && search.matches(stack)) count += stack.getCount();
        }

        return count;
    }

    public static int getCount(EntityPlayer player, Holder search) {
        int count = 0;
        for (ItemStack item: player.inventory.mainInventory) {
            if (item.isEmpty()) continue;
            if (search.matches(item)) {
                count += item.getCount();
            }
        }

        ItemStack offhand = player.inventory.offHandInventory.get(0);
        if (!offhand.isEmpty() && search.matches(offhand)) {
            count += offhand.getCount();
        }

        return count;
    }

    public static int takeItemsInInventory(EntityPlayer player, Holder holder, int count) {
        //Take from the mainhand first before anything else
        if (holder.matches(player.getHeldItemMainhand())) {
            count -= reduceHeld(player, EnumHand.MAIN_HAND, count);
        }

        return takeItems(player, holder, count);
    }

    private static int reduceHeld(EntityPlayer player, EnumHand hand, int amount) {
        ItemStack held = player.getHeldItem(hand);
        if (held.getCount() <= amount) {
            int ret = held.getCount();
            player.setHeldItem(hand, ItemStack.EMPTY);
            return ret;
        } else {
            held.shrink(amount);
            return amount;
        }
    }

    private static int takeItems(EntityPlayer player, Holder search, int amount) {
        int toTake = amount;
        ItemStack offhand = player.inventory.offHandInventory.get(0);
        if (!offhand.isEmpty() && search.matches(offhand)) {
            ItemStack taken = offhand.splitStack(toTake);
            toTake -= taken.getCount();
            if (offhand.getCount() <= 0) player.inventory.offHandInventory.set(0, ItemStack.EMPTY);
            if (toTake <= 0) {
                return 0; //No further processing neccessary
            }
        }

        //Main Inventory
        for (int i = 0; i < player.inventory.mainInventory.size() && toTake > 0; i++) {
            ItemStack stack = player.inventory.mainInventory.get(i);
            if (!stack.isEmpty() && search.matches(stack)) {
                ItemStack taken = stack.splitStack(toTake);
                toTake -= taken.getCount();
                if (stack.getCount() <= 0) player.inventory.mainInventory.set(i, ItemStack.EMPTY); //Clear
                if (toTake <= 0) return 0; //No further processing neccessary
            }
        }

        return toTake;
    }

    public static NonNullList<ItemStack> getAllStacks(List<IItemHandlerModifiable> inventories) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (IItemHandlerModifiable handler: inventories) {
            for (int i = 0 ; i < handler.getSlots(); i++) {
                ItemStack item = handler.getStackInSlot(i);
                if (!item.isEmpty()) {
                    stacks.add(item);
                }
            }
        }

        return stacks;
    }

    public static List<FluidStack> getAllFluids(List<IItemHandlerModifiable> inventories) {
        List<FluidStack> list = Lists.newArrayList();
        for (IItemHandlerModifiable handler: inventories) {
            for (int i = 0 ; i < handler.getSlots(); i++) {
                ItemStack item = handler.getStackInSlot(i);
                if (!item.isEmpty()) {
                    FluidStack stack = FluidUtil.getFluidContained(item);
                    if (stack != null) {
                        list.add(stack);
                    }
                }
            }
        }

        return list;
    }
}
