package joshie.harvest.player;

import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.player.IQuestHelper;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.player.relationships.RelationshipHelper;
import joshie.harvest.quests.QuestHelper;

public class PlayerAPI implements IPlayerHelper {
    private final IQuestHelper quests = new QuestHelper();
    private final IRelationships relations = new RelationshipHelper();

    @Override
    public IRelationships getRelationshipHelper() {
        return relations;
    }

    @Override
    public IQuestHelper getQuestHelper() {
        return quests;
    }
}
