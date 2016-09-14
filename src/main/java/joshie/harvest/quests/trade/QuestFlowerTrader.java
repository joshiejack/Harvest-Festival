package joshie.harvest.quests.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.InventoryHelper.SearchType;
import joshie.harvest.quests.Quests;
import joshie.harvest.crops.HFCrops;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.helpers.InventoryHelper.SPECIAL;
import static joshie.harvest.npc.HFNPCs.SEED_OWNER;

@HFQuest("trade.seeds")
public class QuestFlowerTrader extends QuestTrade {
    private Season season;

    public QuestFlowerTrader() {
        setNPCs(SEED_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.TUTORIAL_CROPS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (InventoryHelper.getHandItemIsIn(player, SPECIAL, SearchType.FLOWER, 5) != null) {
            if (season == SPRING || season == SUMMER || season == AUTUMN) {
                //Jade informs the player that she will happily trade flowers
                //For a bag of seeds
                return getLocalized("complete.yes");
                //Jade Informs the player that she doesn't have in stock this time of year
            } else return getLocalized("complete.no");
        }

         return null;
    }

    @Override
    public void onChatOpened(EntityPlayer player, EntityLiving entity, INPC npc) {
        season = HFApi.calendar.getSeasonAtCoordinates(entity.worldObj, new BlockPos(entity));
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (InventoryHelper.takeItemsIfHeld(player, SPECIAL, SearchType.FLOWER, 5) != null) {
            if (season == SPRING || season == SUMMER || season == AUTUMN) {
                if (season == SPRING) rewardItem(player, HFCrops.TURNIP.getSeedStack(1));
                else if (season == SUMMER) rewardItem(player, HFCrops.ONION.getSeedStack(1));
                else if (season == AUTUMN) rewardItem(player, HFCrops.CARROT.getSeedStack(1));
            }
        }
    }
}
