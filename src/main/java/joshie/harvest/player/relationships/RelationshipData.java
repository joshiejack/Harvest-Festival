package joshie.harvest.player.relationships;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPCStatus;
import joshie.harvest.api.player.IRelations;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npc.HFNPCs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

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

    public void newDay(CalendarDate yesterday, CalendarDate today) {
        Set<UUID> keys = new HashSet<>(status.keySet());
        for (UUID uuid: keys) {
            for (NPCStatus stat: NPCStatus.values()) {
                if ((stat.isSeasonal() && yesterday.getSeason() != today.getSeason()) || !stat.isPermenant()) {
                    status.get(uuid).remove(stat);
                }
            }
        }
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

    public void readFromNBT(NBTTagCompound nbt) {
        //Reading Relations
        NBTTagList relationList = nbt.getTagList("Relationships", 10);
        for (int i = 0; i < relationList.tagCount(); i++) {
            NBTTagCompound tag = relationList.getCompoundTagAt(i);
            if (tag.hasKey("UUID")) {
                UUID key = UUID.fromString(tag.getString("UUID"));
                int value = tag.getInteger("Value");
                relationships.put(key, value);
            }
        }

        NBTTagList statusList = nbt.getTagList("Statuses", 10);
        for (int i = 0; i < statusList.tagCount(); i++) {
            NBTTagCompound tag = statusList.getCompoundTagAt(i);
            if (tag.hasKey("UUID")) {
                UUID key = UUID.fromString(tag.getString("UUID"));
                Collection<NPCStatus> collection = status.get(key);
                NBTTagList statuses = tag.getTagList("Status", 8);
                for (int j = 0; j < statuses.tagCount(); j++) {
                    collection.add(NPCStatus.valueOf(statuses.getStringTagAt(j)));
                }
            }
        }

        //TODO: Remove in 0.7
        if (nbt.hasKey("TalkedTo") || nbt.hasKey("Gifted") || nbt.hasKey("MarriedTo")) {
            Set<UUID> talked = NBTHelper.readUUIDSet(nbt, "TalkedTo");
            Set<UUID> gifted = NBTHelper.readUUIDSet(nbt, "Gifted");
            Set<UUID> marriedTo = NBTHelper.readUUIDSet(nbt, "MarriedTo");
            for (UUID uuid: talked) status.get(uuid).add(NPCStatus.TALKED);
            for (UUID uuid: gifted) status.get(uuid).add(NPCStatus.GIFTED);
            for (UUID uuid: marriedTo) status.get(uuid).add(NPCStatus.MARRIED);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        //Saving Relationships
        NBTTagList relationList = new NBTTagList();
        for (Map.Entry<UUID, Integer> entry : relationships.entrySet()) {
            if (entry == null || entry.getKey() == null) continue;
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", entry.getKey().toString());
            tag.setInteger("Value", entry.getValue());
            relationList.appendTag(tag);
        }

        nbt.setTag("Relationships", relationList);

        //Save the Statuses
        NBTTagList statusList = new NBTTagList();
        for (UUID uuid: status.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("UUID", uuid.toString());
            NBTTagList list = new NBTTagList();
            for (NPCStatus stat: status.get(uuid)) {
                list.appendTag(new NBTTagString(stat.name()));
            }

            tag.setTag("Status", list);
            statusList.appendTag(tag);
        }

        nbt.setTag("Statuses", statusList);
        return nbt;
    }
}