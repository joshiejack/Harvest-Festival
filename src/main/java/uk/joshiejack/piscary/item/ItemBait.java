package uk.joshiejack.piscary.item;

import uk.joshiejack.penguinlib.item.base.ItemBaseFishingRod;
import uk.joshiejack.penguinlib.item.base.ItemMulti;
import uk.joshiejack.piscary.Piscary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ItemBait extends ItemMulti<ItemBait.Bait> {
    public ItemBait() {
        super(new ResourceLocation(MODID, "bait"), Bait.class);
        setCreativeTab(Piscary.TAB);
    }

    public enum Bait implements IStringSerializable {
        BASIC, VANILLA;

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

    private ItemStack getClosest(NonNullList<ItemStack> mainInventory, int slot) {
        //Check to the right first
        int check = slot == 8 ? 0 : slot + 1;
        ItemStack stack = mainInventory.get(check);
        if (stack.getItem() instanceof ItemBaseFishingRod) return stack;
        else {
            check = slot == 0 ? 8 : slot + -1;
            stack = mainInventory.get(check);
            if (stack.getItem() instanceof ItemBaseFishingRod) return stack;
            else return null;
        }
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        int slot = getSlotStackIsIn(playerIn.inventory.mainInventory, stack);
        if (slot != -1) {
            ItemStack rod = getClosest(playerIn.inventory.mainInventory, slot);
            if (rod != null && ((ItemBaseFishingRod)rod.getItem()).addBait(rod, stack)) {
                stack.setCount(0); //Clear it out
                return new ActionResult<>(EnumActionResult.SUCCESS, stack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
