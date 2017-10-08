package joshie.harvest.cooking.tile;

import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TilePot extends TileHeatable {
    @Override
    public Utensil getUtensil() {
        return HFCooking.POT;
    }

    @Override
    public void animate() {
        super.animate();

        if (getCookTimer() == 1) world.playSound(null, getPos(), HFSounds.POT, SoundCategory.BLOCKS, 1F, 1F);
    }
}