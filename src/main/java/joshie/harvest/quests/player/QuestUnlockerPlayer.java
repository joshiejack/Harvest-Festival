package joshie.harvest.quests.player;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.quests.base.QuestUnlocked;
import joshie.harvest.quests.base.QuestUnlocker;
import joshie.harvest.quests.data.QuestData;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Set;

@HFQuest("unlocker.player")
public class QuestUnlockerPlayer extends QuestUnlocker {
    public static final Set<QuestUnlocked> quests = new HashSet<>();
    public static final Set<INPC> npcs = new HashSet<>();

    @Override
    public final Set<INPC> getNPCs() {
        return npcs;
    }

    @Override
    public Set<QuestUnlocked> getQuests() {
        return quests;
    }

    @Override
    public QuestData getQuestData(EntityPlayer player) {
        return HFTrackers.getPlayerTrackerFromPlayer(player).getQuests();
    }
}
