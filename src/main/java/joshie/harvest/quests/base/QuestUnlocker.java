package joshie.harvest.quests.base;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.quests.data.QuestData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

public abstract class QuestUnlocker extends Quest {
    public abstract Set<QuestUnlocked> getQuests();

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }

    public abstract QuestData getQuestData(EntityPlayer player);

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        if (!player.worldObj.isRemote) {
            QuestData questData = getQuestData(player);
            for (QuestUnlocked unlocked : getQuests()) {
                if (unlocked.getNPCs().contains(npc) && unlocked.canUnlock(player, entity, npc)) {
                    questData.startQuest(unlocked);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        for (QuestUnlocked quest: getQuests()) {
            if (nbt.hasKey(quest.getRegistryName().toString())) {
                quest.readPermenantData(nbt.getCompoundTag(quest.getRegistryName().toString()));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        for (QuestUnlocked quest: getQuests()) {
            NBTTagCompound tag = quest.getPermenantData();
            if (tag != null) {
                nbt.setTag(quest.getRegistryName().toString(), tag);
            }
        }

        return nbt;
    }
}
