package joshie.harvest.api.npc.schedule;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class ScheduleElement {
    public static final BiMap<ResourceLocation, Class> REGISTRY = HashBiMap.create();
    private boolean satisfied = false;

    public boolean isSatisfied(EntityAgeable npc) {
        return satisfied;
    }

    public void execute(EntityAgeable npc) {
        satisfied = true;
    }

    public abstract void readFromNBT(NBTTagCompound tag);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
