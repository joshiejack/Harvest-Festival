package joshie.harvest.api.quest;

import java.util.Collection;

public interface IQuestRegistry {
    Collection<IQuest> getQuests();

    /** Registers a new quest, using the default system **/
    IQuest register(String uniqueName, IQuest quest);

    /** Grab a quest from it's unique name **/
    IQuest get(String uniqueName);
}