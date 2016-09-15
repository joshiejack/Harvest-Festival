package joshie.harvest.player.relationships;

import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.util.Text;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class RelationshipData {
    public void talkTo(EntityPlayer player, UUID key) {}
    public boolean gift(EntityPlayer player, UUID key, int amount) { return false; }
    public void affectRelationship(EntityPlayer player, UUID key, int amount) {}
    public void copyRelationship(@Nullable EntityPlayer player, int adult, UUID baby, double percentage) {}
    public void setRelationship(UUID key, int value) {}
    public void setMarriageState(UUID key, boolean divorce) {}

    protected HashMap<UUID, Integer> relationships = new HashMap<>();
    protected Set<UUID> marriedTo = new HashSet<>();
    protected Set<UUID> gifted = new HashSet<>();

    public void clear(UUID key) {
        relationships.remove(key);
    }

    public int getRelationship(UUID key) {
        if (relationships.containsKey(key)) {
            return relationships.get(key);
        }

        //If we don't have a relationship yet, return 0
        relationships.put(key, 0);
        return 0;
    }

    //If we have the npc friendship requirement and we propose then we become married, if
    //We don't they shall hate us!
    public boolean propose(EntityPlayer player, UUID key) {
        if (!marriedTo.contains(key)) {
            int value = getRelationship(key);
            if (value >= HFNPCs.MARRIAGE_REQUIREMENT) {
                marriedTo.add(key);
                player.addStat(HFAchievements.marriage);
                affectRelationship(player, key, 1000);
                return true;
            } else {
                affectRelationship(player, key, -500);
            }
        }

        return false;
    }

    public boolean isEllegibleToMarry() {
        for (UUID key : relationships.keySet()) {
            int value = getRelationship(key);
            if (value >= HFNPCs.MARRIAGE_REQUIREMENT) {
                return true;
            }
        }

        return false;
    }

    public String getLover() {
        return Text.translate("nolover");
    }
}