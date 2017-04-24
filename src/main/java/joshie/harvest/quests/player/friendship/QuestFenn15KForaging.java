package joshie.harvest.quests.player.friendship;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.gathering.GatheringRegistry;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.quests.base.QuestFriendship;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

@HFQuest("friendship.fenn.plants")
public class QuestFenn15KForaging extends QuestFriendship {
    public QuestFenn15KForaging() {
        super(HFNPCs.CLOCKMAKER_CHILD, 15000);
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.FENN_10K);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onQuestCompleted(EntityPlayer player) {
        Season season = HFApi.calendar.getDate(player.world).getSeason();
        for (int i = 0; i < 7; i++) {
            IBlockState state = GatheringRegistry.INSTANCE.getRandomStateForSeason(player.world, season);
            if (state != null) {
                SpawnItemHelper.spawnByEntity(player, state.getBlock().getItem(player.world, new BlockPos(player), state));
            }
        }
    }
}
