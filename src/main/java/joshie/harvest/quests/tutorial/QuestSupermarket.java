package joshie.harvest.quests.tutorial;

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

import static joshie.harvest.quests.Quests.TUTORIAL_CROPS;
import static joshie.harvest.npc.HFNPCs.*;

@HFQuest("tutorial.supermarket")
public class QuestSupermarket extends Quest {
    private static final int START = 0;

    public QuestSupermarket() {
        setNPCs(SEED_OWNER, GS_OWNER, MILKMAID);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_CROPS);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == START && npc == SEED_OWNER) {
            if (player.worldObj.rand.nextFloat() < 0.15F) {
                if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.SUPERMARKET)) {
                    //If the supermarket exists
                    //Jade will tell you to go and talk to one of the new residents of the supermarket,
                    //She informs you that they have a gift
                    return getLocalized("reminder.talk");
                }

                //Jade tells the player that they should be attempting to have a supermarket built
                //So that they can expand collection
                return getLocalized("reminder.supermarket");
            } else return null;
        } else if (quest_stage == START && (npc == GS_OWNER || npc == MILKMAID)) {
            //Jenni says hey there I'm the owner, welcome to the supermarket, here you can buy all kinds of things
            //From seeds to chocolate, If you need anything, just ask me
            //She then says we're open every weekday except wednesday from 9am to 5pm
            //We're also open on saturdays but only from 11am to 3pm
            //She says you're our first customer so here's a free gift

            //If Candice, Jenni says welcome to the supermarket, jenni is the owner, I'm a guest!
            //It's really lovely here, they sell seeds or even flour
            //Even open on saturdays from 11am to 3pm, although normal openings are
            //mon-tue,thu-fri 9am to 5pm
            //She says jenni asked me to give you this as the first customer
            return npc == HFNPCs.GS_OWNER ? getLocalized("welcome.owner") : getLocalized("welcome.guest");
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == START && npc != HFNPCs.SEED_OWNER) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, HFCrops.POTATO.getSeedStack(5));
    }
}