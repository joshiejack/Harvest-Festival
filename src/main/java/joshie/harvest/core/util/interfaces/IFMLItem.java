package joshie.harvest.core.util.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

public interface IFMLItem<E extends Impl<E>> {
    E getNullValue();
    E getObjectFromStack(ItemStack stack);
    ItemStack getStackFromResource(ResourceLocation resource);
}
