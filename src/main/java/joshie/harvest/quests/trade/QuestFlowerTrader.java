package joshie.harvest.quests.trade;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.InventoryHelper.SearchType;
import joshie.harvest.core.lib.HFQuests;
import joshie.harvest.crops.HFCrops;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.npc.HFNPCs.SEED_OWNER;

@HFQuest("trade.seeds")
public class QuestFlowerTrader extends QuestTrade {
    public QuestFlowerTrader() {
        setNPCs(SEED_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(HFQuests.TUTORIAL_CROPS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        ItemStack held = player.getHeldItemMainhand();
        if (held != null && held.stackSize >= 10 && InventoryHelper.SPECIAL.matches(held, SearchType.FLOWER)) {
            Season season = HFApi.calendar.getSeasonAtCoordinates(entity.worldObj, new BlockPos(entity));
            if (season == SPRING || season == SUMMER || season == AUTUMN) {
                complete(player); //Complete the quest
                //Jade informs the player that she will happily trade flowers
                //For a bag of seeds
                return "complete.yes";
                //Jade Informs the player that she doesn't have in stock this time of year
            } else return "complete.no";
        }

         return null;
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        takeHeldStack(player, 1); //Take everything
        Season season = HFApi.calendar.getDate(player.worldObj).getSeason();
        if (season == SPRING) rewardItem(player, HFCrops.TURNIP.getSeedStack());
        else if (season == SUMMER) rewardItem(player, HFCrops.ONION.getSeedStack());
        else if (season == AUTUMN) rewardItem(player, HFCrops.CARROT.getSeedStack());
    }
}
