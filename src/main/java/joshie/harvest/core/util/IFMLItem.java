package joshie.harvest.core.util;

import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;

public interface IFMLItem<E extends Impl<E>> {
    E getNullValue();
}
