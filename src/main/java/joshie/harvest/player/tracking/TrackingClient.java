package joshie.harvest.player.tracking;

import joshie.harvest.core.util.holders.ItemStackHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class TrackingClient extends Tracking {
    public void setObtained(Set<ItemStackHolder> obtained) {
        this.obtained = obtained;
    }
}