package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Set;

@HFQuest("friendship.goddess.strawberry")
public class QuestGoddess15KStrawberries extends QuestFriendship {
    public QuestGoddess15KStrawberries() {
        super(HFNPCs.GODDESS, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.GODDESS_10K);
    }

    @Override
    @Nonnull
    protected ItemStack getRewardStack() {
        return HFCrops.STRAWBERRY.getCropStack(64);
    }
}
