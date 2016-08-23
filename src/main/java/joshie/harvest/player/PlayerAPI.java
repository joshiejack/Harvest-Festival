package joshie.harvest.player;

import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.player.IQuestHelper;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.player.relationships.RelationshipHelper;
import joshie.harvest.quests.QuestHelper;

@HFApiImplementation
public class PlayerAPI implements IPlayerHelper {
    public static final PlayerAPI INSTANCE = new PlayerAPI();
    private final IQuestHelper quests = new QuestHelper();
    private final IRelationships relations = new RelationshipHelper();

    private PlayerAPI() {}

    @Override
    public IRelationships getRelationshipHelper() {
        return relations;
    }

    @Override
    public IQuestHelper getQuestHelper() {
        return quests;
    }
}
