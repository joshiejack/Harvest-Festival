package joshie.harvest.player.relationships;

import joshie.harvest.api.relations.IRelatable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RelationshipDataClient extends RelationshipData {
    public void setGifted(IRelatable relatable, boolean gifted) {
        if (gifted) this.gifted.add(relatable);
        else this.gifted.remove(relatable);
    }

    @Override
    public void setRelationship(IRelatable relatable, int value) {
        relationships.put(relatable, value);
    }

    @Override
    public void setMarriageState(IRelatable relatable, boolean divorce) {
        if (divorce) {
            marriedTo.remove(relatable);
        } else marriedTo.add(relatable);
    }

    @Override
    public boolean gift(EntityPlayer player, IRelatable relatable, int amount) {
        if (!gifted.contains(relatable)) {
            gifted.add(relatable);
            return true;
        }

        return false;
    }
}
