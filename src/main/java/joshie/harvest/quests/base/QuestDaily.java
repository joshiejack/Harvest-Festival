package joshie.harvest.quests.base;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.town.Town;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;

import java.util.Random;
import java.util.Set;

public abstract class QuestDaily extends Quest {
    protected static final Random rand = new Random();
    private NPC npc;

    public QuestDaily(NPC npc) {
        this.npc = npc;
        setNPCs(npc);
        setTownQuest();
    }

    @Override
    public EventPriority getPriority() {
        return EventPriority.HIGHEST;
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return false;
    }

    @Override
    public boolean canStartDailyQuest(Town town, World world, BlockPos pos) {
        return town.hasNPC(npc);
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
