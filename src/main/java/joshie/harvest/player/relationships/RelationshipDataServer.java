package joshie.harvest.player.relationships;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.npc.NPCStatus;
import joshie.harvest.core.achievements.HFAchievements;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packet.PacketSyncGifted;
import joshie.harvest.player.packet.PacketSyncRelationsConnect;
import joshie.harvest.player.packet.PacketSyncRelationship;
import joshie.harvest.player.packet.PacketSyncStatusReset;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

public class RelationshipDataServer extends RelationshipData {
    private final PlayerTrackerServer master;

    public RelationshipDataServer(PlayerTrackerServer master) {
        this.master = master;
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
            syncStatus((EntityPlayerMP) player, key, NPCStatus.TALKED, true);
        }

        //Add this so we will always have a key for something
        if (!statuses.contains(NPCStatus.MET)) {
            statuses.add(NPCStatus.MET);
            syncStatus((EntityPlayerMP) player, key, NPCStatus.MET, true);
        }
    }

    @Override
    public boolean gift(EntityPlayer player, UUID key, int amount) {
        Collection<NPCStatus> statuses = status.get(key);
        if (!statuses.contains(NPCStatus.GIFTED)) {
            if (amount == 0) return true;
            affectRelationship(key, amount);
            statuses.add(NPCStatus.GIFTED);
            syncStatus((EntityPlayerMP) player, key, NPCStatus.GIFTED, true);
            master.getTracking().addGift();
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
        PacketHandler.sendToClient(new PacketSyncRelationsConnect(writeToNBT(new NBTTagCompound())), player);
    }

    public void syncRelationship(EntityPlayerMP player, UUID key, int value, boolean particles) {
        PacketHandler.sendToClient(new PacketSyncRelationship(key, value, particles), player);
    }

    public void syncStatus(EntityPlayerMP player, UUID key, NPCStatus status, boolean value) {
        PacketHandler.sendToClient(new PacketSyncGifted(key, status, value), player);
    }

    public void resetStatus(CalendarDate yesterday, EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncStatusReset(yesterday), player);
    }
}