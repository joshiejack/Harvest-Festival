package joshie.harvest.core.util;

import joshie.harvest.buildings.render.PreviewEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFCaches {
    @SideOnly(Side.CLIENT)
    public static void clearClient() {
        PreviewEvent.CACHE.invalidateAll();
    }
}
