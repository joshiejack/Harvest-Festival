package joshie.harvest.relations;

import joshie.harvest.api.relations.IRelatable;

public class RelationTrackerClient extends RelationshipTracker {
    @Override
    public void setRelationship(IRelatable relatable, short value) {
        relationships.put(relatable, value);
    }
    
    @Override
    public void setMarried(IRelatable relatable) {
        marriedTo.add(relatable);
    }
}
