package joshie.harvest.quests.base;

import joshie.harvest.api.quests.Quest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import java.util.Random;
import java.util.Set;

public abstract class QuestDaily extends Quest {
    protected static final Random rand = new Random();

    @Override
    public EventPriority getPriority() {
        return EventPriority.HIGHEST;
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }

    @Override
    public boolean canStartDailyQuest(World world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public int getDaysBetween() {
        return 1;
    }
}
