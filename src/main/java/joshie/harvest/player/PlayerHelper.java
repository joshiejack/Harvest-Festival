package joshie.harvest.player;

import joshie.harvest.api.player.IPlayerHelper;
import joshie.harvest.api.relations.IRelationships;
import joshie.harvest.player.relationships.RelationshipHelper;

public class PlayerHelper implements IPlayerHelper {
    private final IRelationships relations = new RelationshipHelper();

    @Override
    public IRelationships getRelationshipHelper() {
        return relations;
    }
}
