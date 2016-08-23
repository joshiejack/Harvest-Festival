package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.cooking.blocks.TileCooking.TileCookingTicking;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TileOven extends TileCookingTicking {
    @Override
    public Utensil getUtensil() {
        return Utensil.OVEN;
    }

    @Override
    public void animate() {
        super.animate();

        if (getCookTimer() == 1) worldObj.playSound(null, getPos(), HFSounds.OVEN, SoundCategory.BLOCKS, 1F, 1F);
        else if (getCookTimer() >= getCookingTime() - 1) {
            worldObj.playSound(null, getPos(), HFSounds.OVEN_DONE, SoundCategory.BLOCKS, 1F, 1F);
        }
    }
}