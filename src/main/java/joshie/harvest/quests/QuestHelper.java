package joshie.harvest.quests;

import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.quests.IQuestHelper;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.api.quests.QuestType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.quests.packet.PacketQuestCompleteEarly;
import joshie.harvest.quests.packet.PacketQuestIncrease;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataClient;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.network.PacketHandler.sendToClient;
import static joshie.harvest.core.network.PacketHandler.sendToDimension;

@HFApiImplementation
public class QuestHelper implements IQuestHelper {
    public static final QuestHelper INSTANCE = new QuestHelper();

    private QuestHelper() {}

    @Override
    public void completeQuest(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (quest.getQuestType() == QuestType.PLAYER) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(player, quest, true);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player);
                if (data != null) {
                    data.getQuests().markCompleted(player, quest, true);
                }
            }
        }
    }

    @Override
    public void completeEarly(QuestQuestion quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (quest.getQuestType() == QuestType.PLAYER) sendToClient(new PacketQuestCompleteEarly(quest), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player);
                if (data != null) {
                    sendToDimension(player.worldObj.provider.getDimension(), new PacketQuestCompleteEarly(quest).setUUID(data.getID()));
                }
            }
        }
    }

    @Override
    public void increaseStage(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            quest.quest_stage++;
            if (quest.getQuestType() == QuestType.PLAYER) sendToClient(new PacketQuestIncrease(quest, quest.writeToNBT(new NBTTagCompound())), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player);
                if (data != null) {
                    sendToDimension(player.worldObj.provider.getDimension(), new PacketQuestIncrease(quest, quest.writeToNBT(new NBTTagCompound())).setUUID(data.getID()));
                }
            }
        }
    }

    /**************************
     * REWARDS
     *****************************/
    @Override
    public void takeHeldStack(EntityPlayer player, int amount) {
        player.inventory.decrStackSize(player.inventory.currentItem, amount);
    }

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

    private static final Set<Quest> EMPTY = new HashSet<>();

    private boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer;
    }

    @Override
    public Set<Quest> getCurrentQuests(EntityPlayer player) {
        if (isFakePlayer(player)) return EMPTY;
        Set<Quest> all = new HashSet<>();
        all.addAll(HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getCurrent());
        TownData data = TownHelper.getClosestTownToEntity(player);
        if (data != null) {
            all.addAll(data.getQuests().getCurrent());
        }

        return all;
    }

    public static Quest getQuest(String name) {
        try {
            return Quest.REGISTRY.getValue(new ResourceLocation(MODID, name));
        } catch (Exception e) { return null; }
    }

    public static String getScript(EntityPlayer player, EntityNPC npc) {
        String script = HFTrackers.getClientPlayerTracker().getQuests().getScript(player, npc);
        if (script != null) return script;
        else {
            TownDataClient data = TownHelper.getClosestTownToEntity(player);
            if (data != null) {
                return data.getQuests().getScript(player, npc);
            } else return null;
        }
    }

    public static Quest getSelection(EntityPlayer player, EntityNPC npc) {
        Quest quest = HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getSelection(player, npc);
        if (quest != null) return quest;
        else {
            TownDataServer data = TownHelper.getClosestTownToEntity(player);
            if (data != null) {
                return data.getQuests().getSelection(player, npc);
            } else return null;
        }
    }

    public static Quest getSelectiomFromID(EntityPlayer player, int selection) {
        Quest toFetch = Quest.REGISTRY.getValues().get(selection);
        if (toFetch.getQuestType() == QuestType.PLAYER) return HFTrackers.getClientPlayerTracker().getQuests().getAQuest(toFetch);
        else return TownHelper.getClosestTownToEntity(player).getQuests().getAQuest(toFetch);
    }
}