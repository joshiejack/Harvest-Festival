package joshie.harvest.relations;

import java.util.HashMap;
import java.util.HashSet;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.util.Translate;

public abstract class RelationshipTracker {
    public void talkTo(IRelatable relatable) {};
    public void gift(IRelatable relatable, int amount) {};
    public void affectRelationship(IRelatable relatable, int amount) {};
    public void setRelationship(IRelatable relatable, short value) {}
    public void setMarried(IRelatable relatable) {};
    public void sync() {};
    public void markDirty() {};
    
    protected HashMap<IRelatable, Short> relationships = new HashMap();
    protected HashSet<IRelatable> marriedTo = new HashSet();

    public void clear(IRelatable animal) {
        relationships.remove(animal);
        markDirty();
    }

    protected short getRelationship(IRelatable relatable) {
        if (relationships.containsKey(relatable)) {
            return relationships.get(relatable);
        }

        relationships.put(relatable, Short.MIN_VALUE);
        return Short.MIN_VALUE;
    }

    public boolean propose(IRelatable relatable) {
        if (!marriedTo.contains(relatable)) {
            short value = getRelationship(relatable);
            if (value >= NPC.REAL_MARRIAGE_REQUIREMENT) {
                marriedTo.add(relatable);
                affectRelationship(relatable, 1000);
                return true;
            } else {
                affectRelationship(relatable, -500);
            }
        }

        return false;
    }

    public boolean isEllegibleToMarry() {
        for (IRelatable relatable : relationships.keySet()) {
            short value = getRelationship(relatable);
            if (value >= NPC.REAL_MARRIAGE_REQUIREMENT) {
                return true;
            }
        }

        return false;
    }
    
    public String getLover() {
        return Translate.translate("nolover");
    }
}
