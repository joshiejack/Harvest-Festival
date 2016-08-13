package joshie.harvest.cooking.blocks;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TileFryingPan extends TileHeatable {
    @Override
    public IUtensil getUtensil() {
        return Utensil.FRYING_PAN;
    }

    @Override
    public void animate() {
        super.animate();

        //Play the sound effect
        if (getCookTimer() == 1) worldObj.playSound(null, getPos(), HFSounds.FRYING_PAN, SoundCategory.BLOCKS, 1F, 1F);
    }
}