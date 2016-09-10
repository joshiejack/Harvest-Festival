package joshie.harvest.quests.tutorial;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

import static joshie.harvest.animals.block.BlockTrough.Trough.WOOD;
import static joshie.harvest.core.lib.HFQuests.TUTORIAL_COW;
import static joshie.harvest.npc.HFNPCs.ANIMAL_OWNER;
import static joshie.harvest.npc.HFNPCs.BUILDER;

@HFQuest("tutorial.barn")
public class QuestBarn extends Quest {
    private static final int START = 0;

    public QuestBarn() {
        setNPCs(BUILDER, ANIMAL_OWNER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_COW);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == START && player.worldObj.rand.nextFloat() < 0.25F && npc == HFNPCs.BUILDER) {
            if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BARN)) {
                //If the barn exists yulif will tell the player to go and talk to jim
                return getLocalized("reminder.talk");
            }

            //Goddess reminds the player that you should go and build a ranch
            //So that you can get additional animals
            return getLocalized("reminder.barn");
        } else if (quest_stage == START && npc == ANIMAL_OWNER) {
            //Jim mentions that he's happy to arrive in the town,
            //And as the first customer he has a treat
            //He then tells you quickly that you can breed larger animals
            //By using a miracle potion, they will be pregnant for a while
            //Then eventually give birth
            return getLocalized("welcome");
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == START && npc == ANIMAL_OWNER) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCrops.GRASS.getCropStack(64));
        rewardItem(player, HFAnimals.MILK.getStackOfSize(Size.LARGE, 3));
        rewardItem(player, HFAnimals.TROUGH.getStackFromEnum(WOOD));
    }
}