package joshie.harvest.fishing.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class ItemJunk extends ItemHFEnum<ItemJunk, Junk> {
    public ItemJunk() {
        super(HFTab.FISHING, Junk.class);
    }

    public enum Junk implements IStringSerializable, ISellable {
        CAN(1L), BOOT(1L), TREASURE(10000L), BONES(1L), FOSSIL(5000L), BAIT(5L, 1L);

        private final long cost;
        private final long sell;

        Junk(long sell) {
            this.cost = 0L;
            this.sell = sell;
        }

        Junk(long cost, long sell) {
            this.cost = cost;
            this.sell = sell;
        }

        public long getCost() {
            return cost;
        }

        @Override
        public long getSellValue() {
            return sell;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    private int getSlotStackIsIn(NonNullList<ItemStack> mainInventory, ItemStack stack) {
        for (int i = 0; i < 9; i++) {
            if (mainInventory.get(i) == stack) return i;
        }

        return -1;
    }

    @Nonnull
    private ItemStack getClosest(NonNullList<ItemStack> mainInventory, int slot) {
        //Check to the right first
        int check = slot == 8 ? 0 : slot + 1;
        ItemStack stack = mainInventory.get(check);
        if (stack.getItem() == HFFishing.FISHING_ROD) return stack;
        else {
            check = slot == 0 ? 8 : slot + -1;
            stack = mainInventory.get(check);
            if (stack.getItem() == HFFishing.FISHING_ROD) return stack;
            else return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (getEnumFromStack(stack) == Junk.BAIT) {
            int slot = getSlotStackIsIn(playerIn.inventory.mainInventory, stack);
            if (slot != -1) {
                ItemStack rod = getClosest(playerIn.inventory.mainInventory, slot);
                if (!rod.isEmpty() && HFFishing.FISHING_ROD.addBait(rod, stack)) {
                    stack.setCount(0); //Clear out this stack
                    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                }
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
