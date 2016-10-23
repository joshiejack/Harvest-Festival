package joshie.harvest.animals.item;

import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.EntityHelper;
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
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemAnimalTreat() {
        super(Treat.class);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        AnimalStats stats = EntityHelper.getStats(target);
        if (stats != null && stats.performAction(player.worldObj, player, stack, AnimalAction.TREAT)) {
            stack.splitStack(1);
            return true;
        } else return false;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.ANIMAL_TREAT;
    }
}