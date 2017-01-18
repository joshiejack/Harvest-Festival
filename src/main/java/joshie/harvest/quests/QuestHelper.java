package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.quests.IQuestHelper;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.api.quests.QuestType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.quests.packet.PacketQuestCompleteEarly;
import joshie.harvest.quests.packet.PacketSyncData;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToDimension;

@HFApiImplementation
public class QuestHelper implements IQuestHelper {
    public static final QuestHelper INSTANCE = new QuestHelper();

    private QuestHelper() {}

    @Override
    public void completeQuestConditionally(Quest quest, EntityPlayer player) {
        if (!hasCompleted(quest, player)) {
            if (quest.getQuestType() == QuestType.PLAYER) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(player.worldObj, player, quest, false);
            else TownHelper.getClosestTownToEntity(player).getQuests().markCompleted(player.worldObj, player, quest, false);
        }
    }

    @Override
    public void completeQuest(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (quest.getQuestType() == QuestType.PLAYER) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(player.worldObj, player, quest, true);
            else TownHelper.getClosestTownToEntity(player).getQuests().markCompleted(player.worldObj, player, quest, true);
        }
    }

    @Override
    public void completeEarly(QuestQuestion quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (quest.getQuestType() == QuestType.PLAYER) sendToClient(new PacketQuestCompleteEarly(quest), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player);
                sendToDimension(player.worldObj.provider.getDimension(), new PacketQuestCompleteEarly(quest).setUUID(data.getID()));
            }
        }
    }

    @Override
    public boolean hasCompleted(Quest quest, EntityPlayer player) {
        if (quest == null) return false;
        if (quest.getQuestType() == QuestType.PLAYER) return HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getFinished().contains(quest);
        return TownHelper.getClosestTownToEntity(player).getQuests().getFinished().contains(quest);
    }

    @Override
    public void increaseStage(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            quest.setStage(quest.getStage() + 1);
            if (quest.getQuestType() == QuestType.PLAYER) sendToClient(new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player);
                sendToDimension(player.worldObj.provider.getDimension(), new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())).setUUID(data.getID()));
            }
        }
    }

    @Override
    public void syncData(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (quest.getQuestType() == QuestType.PLAYER) sendToClient(new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player);
                sendToDimension(player.worldObj.provider.getDimension(), new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())).setUUID(data.getID()));
            }
        }
    }

    /**************************
     * REWARDS
     *****************************/
    @Override
    public void rewardItem(Quest quest, EntityPlayer player, ItemStack stack) {
        SpawnItemHelper.addToPlayerInventory(player, stack);
    }

    @Override
    public void rewardGold(EntityPlayer player, long amount) {
        if (!player.worldObj.isRemote) {
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats().addGold((EntityPlayerMP) player, amount);
        }
    }

    @Override
    public void rewardEntity(Quest quest, EntityPlayer player, String entity) {
        if (!player.worldObj.isRemote) {
            Entity theEntity = EntityList.createEntityByIDFromName(entity, player.worldObj);
            if (theEntity != null) {
                theEntity.setPosition(player.posX, player.posY, player.posZ);
                AnimalStats stats = EntityHelper.getStats(theEntity);
                if (stats != null) {
                    stats.setOwner(EntityHelper.getPlayerUUID(player));
                }

                player.worldObj.spawnEntityInWorld(theEntity);
            }
        }
    }

    private static final List<Quest> EMPTY = new ArrayList<>();

    private boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer;
    }

    @Override
    public List<Quest> getCurrentQuests(EntityPlayer player) {
        if (isFakePlayer(player)) return EMPTY;
        List<Quest> all = new ArrayList<>();
        all.addAll(HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getCurrent());
        all.addAll(TownHelper.getClosestTownToEntity(player).getQuests().getCurrent());
        Collections.sort(all, ((o1, o2) -> { return o1.getPriority().compareTo(o2.getPriority()); }));
        return all;
    }

    public static Quest getQuest(String name) {
        try {
            return Quest.REGISTRY.getValue(new ResourceLocation(MODID, name));
        } catch (Exception e) { return null; }
    }

    public static String getScript(EntityPlayer player, EntityNPC npc) {
        List<Quest> quests = HFApi.quests.getCurrentQuests(player);
        for (Quest q : quests) {
            if (q.getNPCs().contains(npc.getNPC())) {
                String script = q.getLocalizedScript(player, npc, npc.getNPC());
                if (script != null) return script;
            }
        }

        return null;
    }

    public static Quest getSelection(EntityPlayer player, EntityNPC npc) {
        List<Quest> quests = HFApi.quests.getCurrentQuests(player);
        for (Quest q : quests) {
            if (q.getNPCs().contains(npc.getNPC())) {
                if (q.getSelection(player, npc.getNPC()) != null) return q;
            }
        }

        return null;
    }

    public static Quest getSelectiomFromID(EntityPlayer player, int selection) {
        Quest toFetch = Quest.REGISTRY.getValues().get(selection);
        if (toFetch.getQuestType() == QuestType.PLAYER) return HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getAQuest(toFetch);
        else return TownHelper.getClosestTownToEntity(player).getQuests().getAQuest(toFetch);
    }
}