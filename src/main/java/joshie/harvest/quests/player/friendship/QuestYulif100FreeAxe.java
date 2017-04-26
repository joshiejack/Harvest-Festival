package joshie.harvest.quests.player.friendship;

import com.google.common.collect.Sets;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.knowledge.HFNotes;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import joshie.harvest.tools.HFTools;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Set;

@HFQuest("friendship.yulif.axe")
public class QuestYulif100FreeAxe extends QuestFriendship {
    public QuestYulif100FreeAxe() {
        super(HFNPCs.CARPENTER, 100);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.YULIF_MEET);
    }

    @Override
    public Set<Note> getNotes() {
        return Sets.newHashSet(HFNotes.AXE);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Nonnull
    protected ItemStack getRewardStack() {
        return HFTools.AXE.getStack(ToolTier.BASIC);
    }
}
