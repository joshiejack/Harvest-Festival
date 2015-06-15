package joshie.harvest.player;

import java.util.UUID;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.util.IData;
import net.minecraft.nbt.NBTTagCompound;

public class Relatable implements IData {
    private UUID uuid;
    private INPC npc;

    public Relatable() {}
    
    public Relatable(UUID uuid) {
        this.uuid = uuid;
        this.npc = null;
    }

    public Relatable(INPC npc) {
        this.npc = npc;
        this.uuid = null;
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
            npc = HFApi.NPC.get(nbt.getString("NPC"));
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
