package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.IQuestHelper;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Quest.EventType;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.quests.QuestData;
import joshie.harvest.quests.packet.PacketQuestCompleteEarly;
import joshie.harvest.quests.packet.PacketQuestIncrease;
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

@HFApiImplementation
public class QuestHelper implements IQuestHelper {
    public static final QuestHelper INSTANCE = new QuestHelper();

    private QuestHelper() {}

    @Override
    public void completeQuest(Quest quest, EntityPlayer player) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(quest, true);
    }

    @Override
    public void completeEarly(QuestQuestion quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) sendToClient(new PacketQuestCompleteEarly(quest), player);
    }

    @Override
    public void increaseStage(Quest quest, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            int previous = quest.quest_stage;
            quest.quest_stage++;
            quest.onStageChanged(player, previous, quest.quest_stage);
            sendToClient(new PacketQuestIncrease(quest, quest.writeToNBT(new NBTTagCompound())), player);
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
        ItemHelper.addToPlayerInventory(player, stack);
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
                player.worldObj.spawnEntityInWorld(theEntity);
            }
        }
    }

    private static final Set<Quest> EMPTY = new HashSet<>();

    private boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer;
    }

    @Override
    public Set<Quest> getCurrentQuests(EntityPlayer player, EventType events) {
        if (isFakePlayer(player)) return EMPTY;
        return new HashSet<>(HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getHandled(events));
    }

    public static HashSet<Quest> getCurrentQuest(EntityPlayer player) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getCurrent();
    }

    public static void rewardRelations(EntityPlayer player, INPC npc, int amount) {
        HFApi.relationships.adjustRelationship(player, npc, amount);
    }

    public static void markCompleted(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(quest, false);
    }

    public static void markAvailable(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().setAvailable(quest);
    }

    public static void markAsCurrent(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().addAsCurrent(quest);
    }

    public static void setQuestStage(EntityPlayer player, Quest quest, int stage) {
        QuestData stats = HFTrackers.getPlayerTrackerFromPlayer(player).getQuests();
        int previous = stats.getAQuest(quest).getStage();
        stats.setStage(quest, stage);
        quest.onStageChanged(player, previous, stage);
    }

    public static void startQuest(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().startQuest(quest);
    }

    public static Quest getQuest(String name) {
        try {
            return Quest.REGISTRY.getValue(new ResourceLocation(MODID, name));
        } catch (Exception e) { return null; }
    }
}