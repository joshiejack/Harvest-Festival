package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Translate;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.HashSet;

public abstract class RelationshipData {
    public void talkTo(EntityPlayer player, IRelatable relatable) {};
    public void gift(EntityPlayer player, IRelatable relatable, int amount) {};
    public void affectRelationship(EntityPlayer player, IRelatable relatable, int amount) {};
    public void setRelationship(IRelatable relatable, short value) {}
    public void setMarriageState(IRelatable relatable, boolean divorce) {};
    
    protected HashMap<IRelatable, Short> relationships = new HashMap();
    protected HashSet<IRelatable> marriedTo = new HashSet();

    public void clear(IRelatable animal) {
        relationships.remove(animal);
        HFTrackers.markDirty();
    }
       
    protected short getRelationship(IRelatable relatable) {
        if (relationships.containsKey(relatable)) {
            return relationships.get(relatable);
        }

        relationships.put(relatable, Short.MIN_VALUE);
        return Short.MIN_VALUE;
    }

    public boolean propose(EntityPlayer player, IRelatable relatable) {
        if (!marriedTo.contains(relatable)) {
            short value = getRelationship(relatable);
            if (value >= NPC.REAL_MARRIAGE_REQUIREMENT) {
                marriedTo.add(relatable);
                affectRelationship(player, relatable, 1000);
                return true;
            } else {
                affectRelationship(player, relatable, -500);
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
