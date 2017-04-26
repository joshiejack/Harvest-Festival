package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.IQuestHelper;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.TargetType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.player.PlayerTrackerServer;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            if (quest.getQuestType() == TargetType.PLAYER) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(player.world, player, quest, false);
            else TownHelper.getClosestTownToEntity(player, false).getQuests().markCompleted(player.world, player, quest, false);
        }
    }

    @Override
    public void completeQuest(Quest quest, EntityPlayer player) {
        if (!player.world.isRemote) {
            if (quest.getQuestType() == TargetType.PLAYER) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(player.world, player, quest, true);
            else TownHelper.getClosestTownToEntity(player, false).getQuests().markCompleted(player.world, player, quest, true);
        }
    }

    @Override
    public boolean hasCompleted(Quest quest, EntityPlayer player) {
        if (quest == null) return false;
        if (quest.getQuestType() == TargetType.PLAYER) return HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getFinished().contains(quest);
        return TownHelper.getClosestTownToEntity(player, false).getQuests().getFinished().contains(quest);
    }

    @Override
    public void increaseStage(Quest quest, EntityPlayer player) {
        if (!player.world.isRemote) {
            quest.setStage(quest.getStage() + 1);
            if (quest.getQuestType() == TargetType.PLAYER) sendToClient(new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player, false);
                sendToDimension(player.world.provider.getDimension(), new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())).setUUID(data.getID()));
                HFTrackers.markTownsDirty();
            }
        }
    }

    @Override
    public void syncData(Quest quest, EntityPlayer player) {
        if (!player.world.isRemote) {
            if (quest.getQuestType() == TargetType.PLAYER) sendToClient(new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())), player);
            else {
                TownDataServer data = TownHelper.getClosestTownToEntity(player, false);
                sendToDimension(player.world.provider.getDimension(), new PacketSyncData(quest, quest.writeToNBT(new NBTTagCompound())).setUUID(data.getID()));
                HFTrackers.markTownsDirty();
            }
        }
    }

    /**************************
     * REWARDS
     *****************************/
    @Override
    public void rewardItem(Quest quest, EntityPlayer player, @Nonnull ItemStack stack) {
        SpawnItemHelper.addToPlayerInventory(player, stack);
    }

    @Override
    public void rewardGold(EntityPlayer player, long amount) {
        if (!player.world.isRemote) {
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats().addGold((EntityPlayerMP) player, amount);
        }
    }

    @Override
    public void rewardEntity(Quest quest, EntityPlayer player, String entity) {
        if (!player.world.isRemote) {
            ResourceLocation resource = entity.contains(":") ? new ResourceLocation(entity) : new ResourceLocation(MODID, entity);
            Entity theEntity = EntityList.createEntityByIDFromName(resource, player.world);
            if (theEntity != null) {
                theEntity.setPosition(player.posX, player.posY, player.posZ);
                player.world.spawnEntity(theEntity);
            }
        }
    }

    private static final List<Quest> EMPTY = new ArrayList<>();

    private boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer;
    }

    @Override
    public List<Quest> getCurrentQuests(@Nonnull EntityPlayer player) {
        if (isFakePlayer(player)) return EMPTY;
        List<Quest> all = new ArrayList<>();
        all.addAll(HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getCurrent());
        all.addAll(TownHelper.getClosestTownToEntity(player, false).getQuests().getCurrent());
        Collections.sort(all, (Comparator.comparing(o -> o.getRegistryName().toString())));
        Collections.sort(all, (Comparator.comparing(Quest::getPriority)));
        return all;
    }

    public static Quest getQuest(String name) {
        try {
            return Quest.REGISTRY.getValue(new ResourceLocation(MODID, name));
        } catch (Exception e) { return null; }
    }

    @Nullable
    public static Quest getCurrentQuest(EntityPlayer player, EntityNPC npc) {
        List<Quest> quests = HFApi.quests.getCurrentQuests(player);
        for (Quest quest: quests) {
            if (quest.isNPCUsed(player, npc)) return quest;
        }

        return null;
    }

    public static Quest getSelectiomFromID(EntityPlayer player, int selection) {
        Quest toFetch = Quest.REGISTRY.getValues().get(selection);
        if (toFetch.getQuestType() == TargetType.PLAYER) return HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getAQuest(toFetch);
        else return TownHelper.getClosestTownToEntity(player, false).getQuests().getAQuest(toFetch);
    }
}