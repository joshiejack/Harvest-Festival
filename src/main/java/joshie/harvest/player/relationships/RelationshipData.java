package joshie.harvest.player.relationships;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.npc.NPCStatus;
import joshie.harvest.api.player.IRelations;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.*;

public abstract class RelationshipData implements IRelations {
    public void talkTo(EntityPlayer player, UUID key) {}
    public boolean gift(EntityPlayer player, UUID key, int amount) { return false; }
    public void copyRelationship(@Nullable EntityPlayer player, int adult, UUID baby, double percentage) {}

    protected final HashMap<UUID, Integer> relationships = new HashMap<>();
    protected final Multimap<UUID, NPCStatus> status = HashMultimap.create();

    public void clear(UUID key) {
        relationships.remove(key);
    }

    @Override
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
        Collection<NPCStatus> statuses = status.get(key);
        if (!statuses.contains(NPCStatus.MARRIED)) {
            int value = getRelationship(key);
            if (value >= HFNPCs.MARRIAGE_REQUIREMENT) {
                statuses.add(NPCStatus.MARRIED);
                player.addStat(HFAchievements.marriage);
                affectRelationship(key, 1000);
                return true;
            } else {
                affectRelationship(key, -500);
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
        return TextHelper.translate("nolover");
    }
}