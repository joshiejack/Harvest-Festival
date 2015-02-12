package joshie.harvestmoon.player;

import static joshie.harvestmoon.network.PacketHandler.sendToClient;

import java.util.UUID;

import joshie.harvestmoon.helpers.generic.EntityHelper;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.network.PacketSyncRelations;
import joshie.harvestmoon.npc.NPC;
import joshie.harvestmoon.util.IData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class Relatable implements IData {
    private UUID uuid;
    private NPC npc;

    public Relatable() {}
    
    public Relatable(UUID uuid) {
        this.uuid = uuid;
        this.npc = null;
    }

    public Relatable(NPC npc) {
        this.npc = npc;
        this.uuid = null;
    }

    public void sendPacket(EntityPlayerMP player, int relations, boolean particles) {
        if(uuid != null) {
            sendToClient(new PacketSyncRelations(EntityHelper.getEntityIDFromUUID(uuid), relations, true), player);
        } else if (npc != null) {
            sendToClient(new PacketSyncRelations(npc, relations, true), player);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o.getClass() != this.getClass()) return false;
        if (uuid != null) {
            return uuid.equals(((Relatable)o).uuid);
        } else if (npc != null) {
            return npc.equals(((Relatable)o).npc);
        } else return false;
    }

    @Override
    public int hashCode() {
        if (uuid != null) {
            return uuid.hashCode();
        } else if (npc != null) {
            return npc.hashCode();
        } else return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        boolean isUUID = nbt.getBoolean("IsUUID");
        if(isUUID) {
            uuid = new UUID(nbt.getLong("UUIDMost"), nbt.getLong("UUIDLeast"));
        } else {
            npc = HMNPCs.get(nbt.getString("NPC"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if(uuid != null) {
            nbt.setBoolean("IsUUID", true);
            nbt.setLong("UUIDMost", uuid.getMostSignificantBits());
            nbt.setLong("UUIDLeast", uuid.getLeastSignificantBits());
        } else if (npc != null) {
            nbt.setBoolean("IsUUID", false);
            nbt.setString("NPC", npc.getUnlocalizedName());
        }
    }
}
