package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.block.BlockFloating.Floating;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendshipStore;
import net.minecraft.item.ItemStack;

import java.util.Set;

@HFQuest("friendship.jacob.hatchery")
public class QuestJacob15KHatchery extends QuestFriendshipStore {
    public QuestJacob15KHatchery() {
        super(HFNPCs.FISHERMAN, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.JACOB_10K);
    }

    @Override
    protected Quest getQuest() {
        return Quests.SELL_HATCHERY;
    }

    @Override
    protected ItemStack getRewardStack() {
        return HFFishing.FLOATING_BLOCKS.getStackFromEnum(Floating.HATCHERY);
    }
}
