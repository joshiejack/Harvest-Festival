package joshie.harvest.quests.base;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestType;
import joshie.harvest.quests.player.QuestUnlockerPlayer;
import joshie.harvest.quests.town.QuestUnlockerTown;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

public abstract class QuestUnlocked extends Quest {
    @Override
    public void onRegistered() {
        if (getQuestType() == QuestType.TOWN) {
            QuestUnlockerTown.quests.add(this);
            QuestUnlockerTown.npcs.addAll(getNPCs());
        } else {
            QuestUnlockerPlayer.quests.add(this);
            QuestUnlockerPlayer.npcs.addAll(getNPCs());
        }
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        ((QuestUnlocked)Quest.REGISTRY.getValue(getRegistryName())).updateData(player);
    }

    public abstract boolean canUnlock(EntityPlayer player, EntityLiving entity, INPC npc);
    public void updateData(EntityPlayer player) {}
    public NBTTagCompound getPermenantData() { return null; }
    public void readPermenantData(NBTTagCompound tag) {}
}
