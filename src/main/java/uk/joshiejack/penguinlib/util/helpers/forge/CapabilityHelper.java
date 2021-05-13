package uk.joshiejack.penguinlib.util.helpers.forge;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CapabilityHelper {
    public static <E> E getCapabilityFromStack(ItemStack stack, Capability<E> e) {
        return stack.getCapability(e, EnumFacing.DOWN);
    }

    @Nullable
    public static <E> E getCapabilityFromEntity(Entity entity, Capability<E> e) {
        if (entity.hasCapability(e, EnumFacing.DOWN)) {
            return entity.getCapability(e, EnumFacing.DOWN);
        }

        return null;
    }
}
