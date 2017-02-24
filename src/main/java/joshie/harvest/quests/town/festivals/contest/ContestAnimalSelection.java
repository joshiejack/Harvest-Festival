package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.quests.base.QuestAnimalContest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

public class ContestAnimalSelection<E extends EntityAnimal> extends Selection<QuestAnimalContest> {
    private final String translate;

    public ContestAnimalSelection(String prefix) {
        this.translate = "harvestfestival.quest.festival." + prefix + ".nostalls";
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public String[] getText(@Nonnull EntityPlayer player, QuestAnimalContest quest) {
        List<Pair<String, Integer>> animals = quest.getEntries().getNames();
        int max = Math.min(4, animals.size());
        if (max <= 0) return new String[] { I18n.translateToLocal(translate) };
        else {
            String[] string = new String[max];
            for (int i = 0; i < max; i++) {
                string[i] = "@" + animals.get(i).getKey();
            }

            return string;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestAnimalContest quest, int option) {
        List<Pair<E, Integer>> animals = quest.getEntries().getAvailableEntries(player);
        int index = option - 1;
        if (index >= animals.size()) return Result.DENY;
        else {
            quest.getEntries().enter(player, animals.get(index).getKey(), animals.get(index).getValue());
            quest.syncData(player);
            return Result.ALLOW;
        }
    }
}
