package joshie.harvest.api.npc.task;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import joshie.harvest.api.npc.NPCEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class TaskElement {
    public static final BiMap<ResourceLocation, Class> REGISTRY = HashBiMap.create();
    protected boolean satisfied = false;

    public boolean isSatisfied(NPCEntity npc) {
        return satisfied;
    }

    public void execute(NPCEntity npc) {
        satisfied = true;
    }

    public abstract void readFromNBT(NBTTagCompound tag);
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
