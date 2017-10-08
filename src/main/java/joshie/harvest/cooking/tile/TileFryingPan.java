package joshie.harvest.cooking.tile;

import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TileFryingPan extends TileHeatable {
    @Override
    public Utensil getUtensil() {
        return HFCooking.FRYING_PAN;
    }

    @Override
    public void animate() {
        super.animate();

        //Play the sound effect
        if (getCookTimer() == 1) world.playSound(null, getPos(), HFSounds.FRYING_PAN, SoundCategory.BLOCKS, 1F, 1F);
    }
}