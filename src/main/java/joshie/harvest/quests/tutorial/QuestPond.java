package joshie.harvest.quests.tutorial;

import joshie.harvest.api.HFQuest;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.crops.HFCrops;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.core.lib.HFQuests.TUTORIAL_UPGRADING;
import static joshie.harvest.npc.HFNPCs.GODDESS;

@HFQuest("tutorial.pond")
public class QuestPond extends Quest {
    public QuestPond() {
        setNPCs(GODDESS);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_UPGRADING);
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.GODDESS_POND)) {
            //The goddess thanks you for your hard work in reestablishing the town
            //SHe is ever so grateful for her new home too, and thanks you a lot
            //She also has a reward!
            return "thanks";
        }

        //The goddess tells you that she is really happy with how the town is growing
        //And that she would like a permenant place too, she asks that you build her a goddess pond
        return "please";
    }

    @Override
    public void claim(EntityPlayer player) {
        rewardItem(player, new ItemStack(HFCrops.STRAWBERRY.getCropStack().getItem(), 64, HFCrops.STRAWBERRY.getCropStack().getItemDamage()));
        rewardGold(player, 5000);
    }
}