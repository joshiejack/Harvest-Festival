package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.config.NPC;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.Translate;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.HashSet;

public abstract class RelationshipData {
    public void talkTo(EntityPlayer player, IRelatable relatable) {}
    public boolean gift(EntityPlayer player, IRelatable relatable, int amount) { return false; }
    public void affectRelationship(EntityPlayer player, IRelatable relatable, int amount) {}
    public void setRelationship(IRelatable relatable, int value) {}
    public void setMarriageState(IRelatable relatable, boolean divorce) {}

    protected HashMap<IRelatable, Integer> relationships = new HashMap<>();
    protected HashSet<IRelatable> marriedTo = new HashSet<>();
    protected HashSet<IRelatable> gifted = new HashSet<>();

    public void clear(IRelatable animal) {
        relationships.remove(animal);
        HFTrackers.markDirty();
    }

    protected int getRelationship(IRelatable relatable) {
        if (relationships.containsKey(relatable)) {
            return relationships.get(relatable);
        }

        //If we don't have a relationship yet, return 0
        relationships.put(relatable, 0);
        return 0;
    }

    //If we have the npc friendship requirement and we propose then we become married, if
    //We don't they shall hate us!
    public boolean propose(EntityPlayer player, IRelatable relatable) {
        if (!marriedTo.contains(relatable)) {
            int value = getRelationship(relatable);
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
            int value = getRelationship(relatable);
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