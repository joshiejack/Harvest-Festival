package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendshipStore;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Set;

@HFQuest("friendship.brandon.ore")
public class QuestBrandon15KOres extends QuestFriendshipStore {
    public QuestBrandon15KOres() {
        super(HFNPCs.MINER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.BRANDON_10K);
    }

    @Override
    protected Quest getQuest() {
        return Quests.SELL_ORES;
    }

    @Override
    @Nonnull
    protected ItemStack getRewardStack() {
        return new ItemStack(Items.DIAMOND, 1, 3);
    }
}
