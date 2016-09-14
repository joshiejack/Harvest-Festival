package joshie.harvest.quests;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.quests.IQuestHelper;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.player.PlayerTrackerServer;
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
        if (!player.worldObj.isRemote) HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().markCompleted(quest);
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
                if (theEntity instanceof IAnimalTracked) {
                    ((IAnimalTracked)theEntity).getData().setOwner(EntityHelper.getPlayerUUID(player));
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
        return new HashSet<>(HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().getCurrent());
    }

    public static void markAvailable(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().setAvailable(quest);
    }

    public static void markAsCurrent(EntityPlayer player, Quest quest) {
        HFTrackers.getPlayerTrackerFromPlayer(player).getQuests().addAsCurrent(quest);
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