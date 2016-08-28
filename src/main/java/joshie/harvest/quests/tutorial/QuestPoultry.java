package joshie.harvest.quests.tutorial;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFQuest;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.town.TownHelper;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.animals.blocks.BlockTray.Tray.FEEDER_EMPTY;
import static joshie.harvest.animals.item.ItemAnimalTool.Tool.CHICKEN_FEED;
import static joshie.harvest.core.lib.HFQuests.TUTORIAL_CHICKEN;
import static joshie.harvest.npc.HFNPCs.GODDESS;
import static joshie.harvest.npc.HFNPCs.POULTRY;

@HFQuest("tutorial.poultry")
public class QuestPoultry extends Quest {
    public QuestPoultry() {
        setNPCs(GODDESS, POULTRY);
    }

    @Override
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_CHICKEN);
    }

    @Override
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == 0 && player.worldObj.rand.nextFloat() < 0.25F && npc == HFNPCs.GODDESS) {
            if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.POULTRY_FARM)) {
                //If the barn exists the goddess will tell the player to go and talk to ashlee
                return "reminder.talk";
            }

            //Goddess reminds the player that you should go and build a poultry farm
            //So that you can get further chickens
            return "reminder.poultry";
        } else if (quest_stage == 0 && npc == POULTRY) {
            complete(player);

            //The poultry owner welcomes you, tells you their name is ashlee
            //Gives a little backstory about their life
            //Then says it's great to be in this town
            //Then she tells the player about breeding chickens
            //If they buy an incubator they can place an egg in it
            //And eventually a chick will hatch
            //They say thanks for being the first customer and then gives them eggs, and feed
            return "welcome";
        }

        return null;
    }

    @Override
    public void claim(EntityPlayer player) {
        rewardItem(player, new ItemStack(HFAnimals.TOOLS, 64, CHICKEN_FEED.ordinal()));
        rewardItem(player, SizeableHelper.getSizeable(HFAnimals.EGG, 3, Size.LARGE));
        rewardItem(player, HFAnimals.TRAY.getStackFromEnum(FEEDER_EMPTY));
    }
}