package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.quests.selection.TutorialSelection;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

import static joshie.harvest.npcs.HFNPCs.*;
import static joshie.harvest.quests.Quests.BRANDON_MEET;

@HFQuest("tutorial.upgrading")
public class QuestMeetDanieru extends QuestQuestion {
    private static final ItemStack BLACKSMITH_STACK = HFBuildings.BLACKSMITH.getSpawner();
    private static final int BUILD = 0;
    private static final int EXPLAIN = 1;

    public QuestMeetDanieru() {
        super(new TutorialSelection("upgrading"));
        setNPCs(BLACKSMITH, CARPENTER, GODDESS, BARN_OWNER, GS_OWNER, FLOWER_GIRL, MILKMAID, MINER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(BRANDON_MEET);
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return npc == BLACKSMITH ? super.getSelection(player, npc) : null;
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (quest_stage == BUILD && !TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.BLACKSMITH)) return getLocalized("description.build");
        else return super.getDescription(world, player);
    }

    @Override
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        if (!TownHelper.getClosestTownToEntity(player).hasBuilding(HFBuildings.BLACKSMITH)) return BLACKSMITH_STACK;
        else return super.getCurrentIcon(world, player);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        if (quest_stage == BUILD && npc != BLACKSMITH && player.worldObj.rand.nextFloat() < 0.25F) {
            String suffix = ((NPC)npc).getRegistryName().getResourcePath();
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
        } else if  (npc == BLACKSMITH) {
            if (!TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BLACKSMITH)) return null;
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
                //Gives the player copper ores
                return getLocalized("explain");
            }
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean isSneaking) {
        if (npc == BLACKSMITH && (isCompletedEarly || quest_stage == EXPLAIN)) {
            if (TownHelper.getClosestTownToEntity(entity).hasBuilding(HFBuildings.BLACKSMITH)) {
                complete(player);
            }
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.UPGRADING);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.REPAIRING);
        rewardItem(player, new ItemStack(HFMining.MATERIALS, 10, Material.COPPER.ordinal()));
    }
}