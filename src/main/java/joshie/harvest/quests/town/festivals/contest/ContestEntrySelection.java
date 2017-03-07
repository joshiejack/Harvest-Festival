package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Selection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

public class ContestEntrySelection<E> extends Selection<QuestContest> {
    private final String translate;

    public ContestEntrySelection(String prefix) {
        this.translate = "harvestfestival.quest.festival." + prefix + ".nostalls";
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public String[] getText(@Nonnull EntityPlayer player, QuestContest quest) {
        List<Pair<String, Integer>> entries = quest.getEntries().getNames();
        int max = Math.min(4, entries.size());
        if (max <= 0) return new String[] { I18n.translateToLocal(translate) };
        else {
            String[] string = new String[max];
            for (int i = 0; i < max; i++) {
                string[i] = "@" + entries.get(i).getKey();
            }

            return string;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestContest quest, int option) {
        List<Pair<E, Integer>> entries = quest.getEntries().getAvailableEntries(player);
        int index = option - 1;
        if (index >= entries.size()) return Result.DENY;
        else {
            quest.getEntries().enter(player, entries.get(index).getKey(), entries.get(index).getValue());
            quest.syncData(player);
            return Result.ALLOW;
        }
    }
}
