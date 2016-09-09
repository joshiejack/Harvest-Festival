package joshie.harvest.player.relationships;

import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.player.packet.PacketSyncGifted;
import joshie.harvest.player.packet.PacketSyncMarriage;
import joshie.harvest.player.packet.PacketSyncRelationship;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RelationshipDataServer extends RelationshipData {
    private Set<UUID> talked = new HashSet<>();
    private Set<UUID> temp;

    public void newDay() {
        talked = new HashSet<>();
        temp = gifted; //Clone it
        gifted = new HashSet<>();
    }

    @Override
    public void talkTo(EntityPlayer player, UUID key) {
        if (!talked.contains(key)) {
            affectRelationship(player, key, 100);
            talked.add(key);
        }
    }

    @Override
    public boolean gift(EntityPlayer player, UUID key, int amount) {
        if (!gifted.contains(key)) {
            if (amount == 0) return true;
            syncGifted((EntityPlayerMP) player, key, true);
            affectRelationship(player, key, amount);
            gifted.add(key);
            return true;
        }

        return false;
    }

    @Override
    public void affectRelationship(EntityPlayer player, UUID key, int amount) {
        int newValue = Math.max(0, Math.min(HFNPCs.MARRIAGE_REQUIREMENT, getRelationship(key) + amount));
        relationships.put(key, newValue);
        syncRelationship((EntityPlayerMP) player, key, newValue, true);
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
        for (UUID key : marriedTo) {
            syncMarriage(player, key, true);
        }

        for (UUID key : relationships.keySet()) {
            syncRelationship(player, key, relationships.get(key), false);
        }

        //
        //IF we didn't clone yesterdays, then sync the current, otherwise, destroy yesterdays after syncing
        if (temp == null) {
            for (UUID key : gifted) {
                syncGifted(player, key, true);
            }
        } else {
            for (UUID key: temp) {
                syncGifted(player, key, false);
            }

            temp = null;
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

        talked = NBTHelper.readUUIDSet(nbt, "TalkedTo");
        gifted = NBTHelper.readUUIDSet(nbt, "Gifted");
        marriedTo = NBTHelper.readUUIDSet(nbt, "MarriedTo");
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
        nbt.setTag("TalkedTo", NBTHelper.writeUUIDSet(talked));
        nbt.setTag("Gifted", NBTHelper.writeUUIDSet(gifted));
        nbt.setTag("MarriedTo", NBTHelper.writeUUIDSet(marriedTo));
        return nbt;
    }
}