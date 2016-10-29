package joshie.harvest.quests.town;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.quests.base.QuestUnlocked;
import joshie.harvest.quests.base.QuestUnlocker;
import joshie.harvest.quests.data.QuestData;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Set;

@HFQuest("unlocker.town")
public class QuestUnlockerTown extends QuestUnlocker {
    public static final Set<QuestUnlocked> quests = new HashSet<>();
    public static final Set<INPC> npcs = new HashSet<>();

    public QuestUnlockerTown() {
        setTownQuest();
    }

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
        return TownHelper.getClosestTownToEntity(player).getQuests();
    }
}
