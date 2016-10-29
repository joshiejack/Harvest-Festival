package joshie.harvest.quests.player.tutorial;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.quests.Quests.TUTORIAL_UPGRADING;
import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("tutorial.pond")
public class QuestPond extends Quest {
    public QuestPond() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_UPGRADING);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            //The goddess thanks you for your hard work in reestablishing the town
            //SHe is ever so grateful for her new home too, and thanks you a lot
            //She also has a reward!
            return player.worldObj.rand.nextDouble() <= 0.1D ? getLocalized("thanks") : null;
        }

        //The goddess tells you that she is really happy with how the town is growing
        //And that she would like a permenant place too, she asks that you build her a goddess pond
        return getLocalized("please");
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCrops.STRAWBERRY.getCropStack(64));
        rewardGold(player, 5000);
    }
}