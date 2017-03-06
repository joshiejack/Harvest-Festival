package joshie.harvest.quests.base;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.town.Town;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public abstract class QuestWeekly extends Quest {
    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }

    @Override
    public boolean canStartDailyQuest(Town town, World world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public int getDaysBetween() {
        return 7;
    }
}
