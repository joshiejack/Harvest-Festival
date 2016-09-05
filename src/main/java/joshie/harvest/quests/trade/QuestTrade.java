package joshie.harvest.quests.trade;

import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Set;

public class QuestTrade extends Quest {
    public QuestTrade() {}

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public boolean isRealQuest() {
        return false;
    }
}
