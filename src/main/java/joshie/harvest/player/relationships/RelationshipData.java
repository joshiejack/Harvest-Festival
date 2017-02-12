package joshie.harvest.player.relationships;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.api.player.IRelations;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npcs.NPCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public abstract class RelationshipData implements IRelations {
    public boolean gift(EntityPlayer player, NPC key, int amount) { return false; }

    protected final HashMap<NPC, Integer> relationships = new HashMap<>();
    protected final Multimap<NPC, RelationStatus> status = HashMultimap.create();

    public void clear(NPC key) {
        relationships.remove(key);
    }

    @Override
    public boolean isStatusMet(NPC npc, RelationStatus key) {
        Collection<RelationStatus> statuses = status.get(npc);
        return statuses.contains(key);
    }

    public void newDay(CalendarDate yesterday, CalendarDate today) {
        Set<NPC> keys = new HashSet<>(status.keySet());
        for (NPC npc: keys) {
            for (RelationStatus stat: RelationStatus.values()) {
                if ((stat.isSeasonal() && yesterday.getSeason() != today.getSeason()) || !stat.isPermenant()) {
                    status.get(npc).remove(stat);
                }
            }
        }
    }

    @Override
    public int getRelationship(NPC key) {
        if (relationships.containsKey(key)) {
            return relationships.get(key);
        }

        //If we don't have a relationship yet, return 0
        relationships.put(key, 0);
        return 0;
    }

    //If we have the npc friendship requirement and we propose then we become married, if
    //We don't they shall hate us!
    //TODO: Reenable in 1.0 when I readd marriage
    /*
    public boolean propose(EntityPlayer player, UUID key) {
        Collection<RelationStatus> statuses = status.get(key);
        if (!statuses.contains(RelationStatus.MARRIED)) {
            int value = getRelationship(key);
            if (value >= HFNPCs.MARRIAGE_REQUIREMENT) {
                statuses.add(RelationStatus.MARRIED);
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
    }*/

    public String getLover() {
        return TextHelper.translate("nolover");
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Reading Relations
        NBTTagList relationList = nbt.getTagList("Relationships", 10);
        for (int i = 0; i < relationList.tagCount(); i++) {
            NBTTagCompound tag = relationList.getCompoundTagAt(i);
            //TODO: Remove in 0.7+
            if (tag.hasKey("UUID")) {
                UUID key = UUID.fromString(tag.getString("UUID"));
                int value = tag.getInteger("Value");
                NPC npc = NPCHelper.getNPCFromUUID(key);
                if (npc != null) {
                    relationships.put(npc, value);
                }
            } else if (tag.hasKey("NPC")) {
                NPC npc = NPC.REGISTRY.getValue(new ResourceLocation(tag.getString("NPC")));
                int value = tag.getInteger("Value");
                relationships.put(npc, value);
            }
        }

        NBTTagList statusList = nbt.getTagList("Statuses", 10);
        for (int i = 0; i < statusList.tagCount(); i++) {
            NBTTagCompound tag = statusList.getCompoundTagAt(i);
            //TODO: Remove in 0.7+
            if (tag.hasKey("UUID")) {
                UUID key = UUID.fromString(tag.getString("UUID"));
                NPC npc = NPCHelper.getNPCFromUUID(key);
                if (npc != null) {
                    Collection<RelationStatus> collection = status.get(npc);
                    NBTTagList statuses = tag.getTagList("Status", 8);
                    for (int j = 0; j < statuses.tagCount(); j++) {
                        collection.add(RelationStatus.valueOf(statuses.getStringTagAt(j)));
                    }
                }
            } else if (tag.hasKey("NPC")) {
                NPC npc = NPC.REGISTRY.getValue(new ResourceLocation(tag.getString("NPC")));
                Collection<RelationStatus> collection = status.get(npc);
                NBTTagList statuses = tag.getTagList("Status", 8);
                for (int j = 0; j < statuses.tagCount(); j++) {
                    collection.add(RelationStatus.valueOf(statuses.getStringTagAt(j)));
                }
            }
        }

        //TODO: Remove in 0.7
        if (nbt.hasKey("TalkedTo") || nbt.hasKey("Gifted") || nbt.hasKey("MarriedTo")) {
            Set<UUID> talked = NBTHelper.readUUIDSet(nbt, "TalkedTo");
            Set<UUID> gifted = NBTHelper.readUUIDSet(nbt, "Gifted");
            Set<UUID> marriedTo = NBTHelper.readUUIDSet(nbt, "MarriedTo");
            for (UUID uuid: talked) {
                NPC npc = NPCHelper.getNPCFromUUID(uuid);
                if (npc != null) {
                    status.get(npc).add(RelationStatus.TALKED);
                }
            }

            for (UUID uuid: gifted) {
                NPC npc = NPCHelper.getNPCFromUUID(uuid);
                if (npc != null) {
                    status.get(npc).add(RelationStatus.GIFTED);
                }
            }

            for (UUID uuid: marriedTo) {
                NPC npc = NPCHelper.getNPCFromUUID(uuid);
                if (npc != null) {
                    status.get(npc).add(RelationStatus.MARRIED);
                }
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        //Saving Relationships
        NBTTagList relationList = new NBTTagList();
        for (Map.Entry<NPC, Integer> entry : relationships.entrySet()) {
            if (entry == null || entry.getKey() == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("NPC", entry.getKey().getRegistryName().toString());
            tag.setInteger("Value", entry.getValue());
            relationList.appendTag(tag);
        }

        nbt.setTag("Relationships", relationList);

        //Save the Statuses
        NBTTagList statusList = new NBTTagList();
        for (NPC npc: status.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("NPC", npc.getRegistryName().toString());
            NBTTagList list = new NBTTagList();
            for (RelationStatus stat: status.get(npc)) {
                list.appendTag(new NBTTagString(stat.name()));
            }

            tag.setTag("Status", list);
            statusList.appendTag(tag);
        }

        nbt.setTag("Statuses", statusList);
        return nbt;
    }
}