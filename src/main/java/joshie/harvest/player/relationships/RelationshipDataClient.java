package joshie.harvest.player.relationships;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

@SideOnly(Side.CLIENT)
public class RelationshipDataClient extends RelationshipData {
    //Sets a status
    public void setStatus(NPC npc, RelationStatus theStatus, boolean add) {
        if (add) status.get(npc).add(theStatus);
        else status.get(npc).remove(theStatus);
    }

    //Sets a relationship value
    public void setRelationship(NPC npc, int value) {
        relationships.put(npc, value);
    }

    @Override
    public boolean gift(EntityPlayer player, NPC npc, int amount) {
        Collection<RelationStatus> statuses = status.get(npc);
        if (!statuses.contains(RelationStatus.GIFTED)) {
            statuses.add(RelationStatus.GIFTED);
            return true;
        }

        return false;
    }
}
