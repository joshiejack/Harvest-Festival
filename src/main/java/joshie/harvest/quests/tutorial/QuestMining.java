package joshie.harvest.quests.tutorial;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.quests.TutorialSelection;
import joshie.harvest.tools.HFTools;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Set;

import static joshie.harvest.core.lib.HFQuests.*;
import static joshie.harvest.npc.HFNPCs.*;

@HFQuest("tutorial.upgrading")
public class QuestMining extends QuestQuestion {
    private static final int BUILD = 0;
    private static final int EXPLAIN = 1;
    private static final int FINISH = 2;

    public QuestMining() {
        super(new TutorialSelection("upgrading"));
        setNPCs(BUILDER, GODDESS, ANIMAL_OWNER, GS_OWNER, SEED_OWNER, MILKMAID, TOOL_OWNER, MINER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(TUTORIAL_BARN) && finished.contains(TUTORIAL_POULTRY) && finished.contains(TUTORIAL_SUPERMARKET);
    }

    @Override
    public Selection getSelection(EntityPlayer player, INPC npc) {
        return npc == TOOL_OWNER ? super.getSelection(player, npc) : null;
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (quest_stage == BUILD && npc != TOOL_OWNER && player.worldObj.rand.nextFloat() < 0.25F) {
            String suffix = ((joshie.harvest.npc.NPC)npc).getRegistryName().getResourcePath();
            boolean blacksmith = TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BLACKSMITH);
            //They tell the player that they should go and visit the blacksmith
            //They should all have a slight variation
            //So this is like
            //reminder.blacksmith.yulif, reminder.blacksmith.goddess, reminder.blacksmith.jim
            //reminder.blacksmith.jenni, reminder.blacksmith.candice, reminder.blacksmith.jade
            //reminder.blacksmith.brandon
            if (blacksmith) return getLocalized("reminder.blacksmith." + suffix);
            //Builder, Goddess, Barn Owner, General Store Owner, Seed Owner and Milkmaid
            //All tell the player that they should probably get a mine and a blacksmith built
            return getLocalized("blacksmith." + suffix);
        } else if  (npc == TOOL_OWNER) {
            if (isCompletedEarly) {
                return getLocalized("completed");
            } else if (quest_stage == BUILD) {
                //Danieuru thanks the player for welcoming to the town
                //He then proceeds to ask them, if they know how to upgrade tools
                return getLocalized("intro");
            } else if (quest_stage == EXPLAIN) {
                //The Blacksmith says oh well! Then let me tell you, it's a simple process
                //As you use your tools, they will gain levels, which you can see
                //When they're over 100% you can come and visit me and I will happily
                //Upgrade them to the next tier, it will cost you a bit of money, and some materials
                //And it'll take me a few days. You're going to need to get those materials somehow
                //And for that you're going to need to go down the mine, When in the mine
                //The easiest way to get materials is to smash the rocks around you
                //You're probably going to be looking for ore rocks to do this
                //You'll need a hammer (he gives you one) and the most efficient way to use it is to jump
                //And then swing the hammer at the rocks as you fall down
                //Anyway, I heard that the miner has just recently been mining and has some spare
                //ore he would like to give, you should go visit him
                return getLocalized("explain");
            } else if (quest_stage == FINISH) {
                //Blacksmith reminds you to go and see the miner for some ore
                //He also mentions that you can buy tools from him
                return getLocalized("reminder.visit");
            }
        } else if (npc == MINER && quest_stage == FINISH) {
            //Brandon tells you he's just been on a recent trip down a mine
            //He then says you can have this, he then gives the player 10 copper
            return getLocalized("complete");
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {
        if (npc == TOOL_OWNER) {
            if (isCompletedEarly || quest_stage == EXPLAIN) {
                if (isCompletedEarly) complete(player);
                else increaseStage(player);
                rewardItem(player, HFTools.HAMMER.getStack(ToolTier.BASIC));
            }
        } else if (npc == MINER && quest_stage == FINISH) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, new ItemStack(HFMining.MATERIALS, 10, Material.COPPER.ordinal()));
    }
}