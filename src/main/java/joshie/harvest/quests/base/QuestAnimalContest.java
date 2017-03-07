package joshie.harvest.quests.base;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.quests.town.festivals.contest.QuestContest;
import joshie.harvest.quests.town.festivals.contest.animal.AnimalContestEntries;
import net.minecraft.entity.passive.EntityAnimal;

public abstract class QuestAnimalContest<E extends EntityAnimal> extends QuestContest<AnimalContestEntries> {
    public QuestAnimalContest(NPC npc, String prefix) {
        super(npc, prefix);

    }

    @Override
    @SuppressWarnings("unchecked")
    public AnimalContestEntries<E> getEntries() {
        return entries;
    }
}
