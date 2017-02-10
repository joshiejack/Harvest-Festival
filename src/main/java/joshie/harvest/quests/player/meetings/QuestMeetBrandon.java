package joshie.harvest.quests.player.meetings;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.QuestQuestion;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.quests.selection.TutorialSelection;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;
import static joshie.harvest.npcs.HFNPCs.MINER;
import static joshie.harvest.quests.Quests.JENNI_MEET;

@HFQuest("tutorial.mining")
public class QuestMeetBrandon extends QuestQuestion {
    private static final int BUILD = 0;
    private static final int EXPLAIN = 1;
    private static final int ORE = 2;

    public QuestMeetBrandon() {
        super(new TutorialSelection("mining"));
        setNPCs(MINER);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(JENNI_MEET);
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return super.getSelection(player, npc);
    }

    @Override
    public String getDescription(World world, EntityPlayer player) {
        if (quest_stage == ORE) return getLocalized("description.ore");
        else return super.getDescription(world, player);
    }

    @Override
    public String getLocalizedScript(EntityPlayer player, NPC npc) {
        if (isCompletedEarly()) {
            return getLocalized("completed");
        } else if (quest_stage == BUILD) {
            //Brandon asks if the player knows how to mine
            return getLocalized("intro");
        } else if (quest_stage == EXPLAIN) {
            //Brandon explains mining, and asks the player to bring him 3 junk rocks
            return getLocalized("explain");
        } else if (quest_stage == ORE) {
            if (InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFMining.MATERIALS.getStackFromEnum(Material.JUNK), 3) != null) {
                return getLocalized("complete");
            } else return getLocalized("reminder");
        }

        return null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPC npc) {
        if (quest_stage == EXPLAIN) {
            rewardItem(player, HFTools.HAMMER.getStack(ToolTier.BASIC));
            increaseStage(player);
        } else if ((quest_stage == ORE && InventoryHelper.getHandItemIsIn(player, ITEM_STACK, HFMining.MATERIALS.getStackFromEnum(Material.JUNK), 3) != null)) {
            complete(player);
        }
    }

    @Override
    public void onQuestCompleted(EntityPlayer player) {
        //Early finishes
        if (isCompletedEarly()) {
            rewardItem(player, HFTools.HAMMER.getStack(ToolTier.BASIC));
        }

        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.MINING);
        HFApi.player.getTrackingForPlayer(player).learnNote(HFNotes.HAMMER);
        rewardItem(player, new ItemStack(HFMining.MATERIALS, 10, Material.JUNK.ordinal()));
    }
}