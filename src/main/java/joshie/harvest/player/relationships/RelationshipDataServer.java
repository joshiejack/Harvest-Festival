package joshie.harvest.player.relationships;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPCStatus;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packet.PacketSyncGifted;
import joshie.harvest.player.packet.PacketSyncMarriage;
import joshie.harvest.player.packet.PacketSyncRelationship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nullable;
import java.util.*;

public class RelationshipDataServer extends RelationshipData {
    private final PlayerTrackerServer master;

    public RelationshipDataServer(PlayerTrackerServer master) {
        this.master = master;
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

    public boolean hasGivenBirthdayGift(UUID uuid) {
        return status.get(uuid).contains(NPCStatus.BIRTHDAY_GIFT);
    }

    public void setHasGivenBirthdayGift(UUID uuid) {
        status.get(uuid).add(NPCStatus.BIRTHDAY_GIFT);
    }

    @Override
    public void talkTo(EntityPlayer player, UUID key) {
        Collection<NPCStatus> statuses = status.get(key);
        if (!statuses.contains(NPCStatus.TALKED)) {
            statuses.add(NPCStatus.TALKED);
            affectRelationship(key, 100);
        }

        //Add this so we will always have a key for something
        if (!statuses.contains(NPCStatus.MET)) statuses.add(NPCStatus.MET);
    }

    @Override
    public boolean gift(EntityPlayer player, UUID key, int amount) {
        Collection<NPCStatus> statuses = status.get(key);
        if (!statuses.contains(NPCStatus.GIFTED)) {
            if (amount == 0) return true;
            syncGifted((EntityPlayerMP) player, key, true);
            affectRelationship(key, amount);
            statuses.add(NPCStatus.GIFTED);
            return true;
        }

        return false;
    }

    @Override
    public void affectRelationship(UUID key, int amount) {
        int newValue = Math.max(0, Math.min(HFNPCs.MARRIAGE_REQUIREMENT, getRelationship(key) + amount));
        relationships.put(key, newValue);
        EntityPlayerMP player = master.getAndCreatePlayer();
        if (player != null) {
            if (newValue >= 5000) player.addStat(HFAchievements.friend);
            syncRelationship(player, key, newValue, true);
        }
    }

    @Override
    public void copyRelationship(@Nullable EntityPlayer player, int adult, UUID baby, double percentage) {
        int newValue = (int)(adult * (percentage / 100D));
        relationships.put(baby, newValue);
        if (player != null) {
            syncRelationship((EntityPlayerMP) player, baby, newValue, true);
        }
    }

    public void sync(EntityPlayerMP player) {
        for (UUID key : relationships.keySet()) {
            syncRelationship(player, key, relationships.get(key), false);
        }


        for (UUID key: status.keySet()) {
            Collection<NPCStatus> statuses = status.get(key);
            syncGifted(player, key, statuses.contains(NPCStatus.GIFTED));
            syncMarriage(player, key, statuses.contains(NPCStatus.MARRIED));
        }
    }

    public void syncMarriage(EntityPlayerMP player, UUID key, boolean divorce) {
        PacketHandler.sendToClient(new PacketSyncMarriage(key, divorce), player);
    }

    public void syncRelationship(EntityPlayerMP player, UUID key, int value, boolean particles) {
        PacketHandler.sendToClient(new PacketSyncRelationship(key, value, particles), player);
    }

    public void syncGifted(EntityPlayerMP player, UUID key, boolean value) {
        PacketHandler.sendToClient(new PacketSyncGifted(key, value), player);
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