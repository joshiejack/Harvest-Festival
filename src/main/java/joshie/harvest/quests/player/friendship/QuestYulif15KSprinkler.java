package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockSprinkler.Sprinkler;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendshipStore;
import net.minecraft.item.ItemStack;

import java.util.Set;

@HFQuest("friendship.yulif.sprinkler")
public class QuestYulif15KSprinkler extends QuestFriendshipStore {
    public QuestYulif15KSprinkler() {
        super(HFNPCs.BUILDER, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.YULIF_10K);
    }

    @Override
    protected Quest getQuest() {
        return Quests.SELL_SPRINKLER;
    }

    @Override
    protected ItemStack getRewardStack() {
        return HFCrops.SPRINKLER.getStackFromEnum(Sprinkler.IRON);
    }
}
