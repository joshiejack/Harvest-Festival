package joshie.harvest.player.relationships;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RelationshipDataClient extends RelationshipData {
    public void setGifted(UUID key, boolean gifted) {
        if (gifted) this.gifted.add(key);
        else this.gifted.remove(key);
    }

    @Override
    public void setRelationship(UUID key, int value) {
        relationships.put(key, value);
    }

    @Override
    public void setMarriageState(UUID key, boolean divorce) {
        if (divorce) {
            marriedTo.remove(key);
        } else marriedTo.add(key);
    }

    @Override
    public boolean gift(EntityPlayer player, UUID key, int amount) {
        if (!gifted.contains(key)) {
            gifted.add(key);
            return true;
        }

        return false;
    }
}
