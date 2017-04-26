package joshie.harvest.quests.player.meetings;

import com.google.common.collect.Sets;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestMeetingTutorial;
import joshie.harvest.quests.selection.TutorialSelection;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Set;

@HFQuest("tutorial.upgrading")
public class QuestMeetDanieru extends QuestMeetingTutorial {
    private static final int BUILD = 0;
    private static final int EXPLAIN = 1;

    public QuestMeetDanieru() {
        super(new TutorialSelection("upgrading"), HFBuildings.BLACKSMITH, HFNPCs.BLACKSMITH);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (quest_stage == BUILD && TownHelper.getClosestTownToEntity(player, false).hasBuildings(building.getRequirements())) {
            return hasBuilding(player) ? getLocalized("description.talk") : getLocalized("description.build");
        } else return null;
    }

    @Override
    @Nonnull
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        return hasBuilding(player) ? primary : buildingStack;
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, NPC npc) {
        if (isCompletedEarly()) {
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

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPC npc) {
        if (quest_stage == EXPLAIN) complete(player);
    }

    @Override
    public Set<Note> getNotes() {
        return Sets.newHashSet(HFNotes.UPGRADING, HFNotes.REPAIRING);
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        rewardItem(player, new ItemStack(HFMining.MATERIALS, 10, Material.COPPER.ordinal()));
    }
}