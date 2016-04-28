package joshie.harvest.api.quest;

public interface IQuestRegistry {
    /** Registers a new quest, using the default system **/
    public IQuest register(String uniqueName, IQuest quest);

    /** Grab a quest from it's unique name **/
    public IQuest get(String uniqueName);
}