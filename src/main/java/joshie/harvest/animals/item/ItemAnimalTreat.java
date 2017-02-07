package joshie.harvest.animals.item;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalTreat.Treat;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalType;
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
        COW(HFAnimals.COWS), SHEEP(HFAnimals.SHEEP), CHICKEN(HFAnimals.CHICKENS), GENERIC(null);

        private final IAnimalType type;

        Treat(IAnimalType type) {
            this.type = type;
        }

        public IAnimalType getType() {
            return type;
        }

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
        AnimalAction action = getEnumFromStack(stack).getType() != null ? AnimalAction.TREAT_SPECIAL : AnimalAction.TREAT_GENERIC;
        if (stats != null && stats.performAction(player.worldObj, stack, action)) {
            stack.splitStack(1);
            return true;
        } else return false;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.ANIMAL_TREAT;
    }
}