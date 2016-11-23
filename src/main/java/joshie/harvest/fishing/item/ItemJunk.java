package joshie.harvest.fishing.item;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.util.interfaces.ISellable;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
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

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (getEnumFromStack(stack) == Junk.BAIT) {
            for (ItemStack stackz: playerIn.inventory.mainInventory) {
                if (stackz != null && stackz.getItem() == HFFishing.FISHING_ROD) {
                    if (HFFishing.FISHING_ROD.addBait(stackz, stack)) {
                        stack.stackSize = 0; //Clear out this stack
                        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                    }
                }
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
