package joshie.harvest.core.util.interfaces;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface INBTSerializableMap<R, T, N extends NBTBase> extends INBTSerializable<N> {
    void buildMap(Map<R, T> map);
}
