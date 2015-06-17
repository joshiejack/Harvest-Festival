package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatable;

public class RelationshipDataClient extends RelationshipData {
    @Override
    public void setRelationship(IRelatable relatable, short value) {
        relationships.put(relatable, value);
    }

    @Override
    public void setMarriageState(IRelatable relatable, boolean divorce) {
        if (divorce) {
            marriedTo.remove(relatable);
        } else marriedTo.add(relatable);
    }
}
