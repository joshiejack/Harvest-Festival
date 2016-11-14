package joshie.harvest.player.relationships;

import joshie.harvest.api.npc.RelationStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RelationshipDataClient extends RelationshipData {
    //Sets a status
    public void setStatus(UUID uuid, RelationStatus theStatus, boolean add) {
        if (add) status.get(uuid).add(theStatus);
        else status.get(uuid).remove(theStatus);
    }

    //Sets a relationship value
    public void setRelationship(UUID key, int value) {
        relationships.put(key, value);
    }

    @Override
    public boolean gift(EntityPlayer player, UUID key, int amount) {
        Collection<RelationStatus> statuses = status.get(key);
        if (!statuses.contains(RelationStatus.GIFTED)) {
            statuses.add(RelationStatus.GIFTED);
            return true;
        }

        return false;
    }
}
