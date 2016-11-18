package joshie.harvest.core.util.adapter;

import joshie.harvest.api.quests.Quest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class QuestAdapter extends SerializeAdapter<Quest> {
    @Override
    public void writeToNBT(Quest object, NBTTagCompound tag) {
        tag.setString("Quest", object.getRegistryName().toString());
    }

    @Override
    public Quest readFromNBT(NBTTagCompound tag) {
        return  Quest.REGISTRY.getValue(new ResourceLocation(tag.getString("Quest")));
    }
}
