package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class ItemAnimalTreat extends ItemHFEnum<ItemAnimalTreat, Treat> {
    public enum Treat implements IStringSerializable {
        COW, SHEEP, CHICKEN, GENERIC;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.US);
        }
    }

    public ItemAnimalTreat() {
        super(Treat.class);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if (target instanceof IAnimalTracked) {
            if (!target.worldObj.isRemote) {
                if (((IAnimalTracked) target).getData().treat(stack, player)) {
                    stack.stackSize--;
                }
            }

            return true;
        } else return false;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.ANIMAL_TREAT;
    }
}