package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TilePot extends TileHeatable {
    @Override
    public Utensil getUtensil() {
        return Utensil.POT;
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.OVEN);
    }

    @Override
    public void animate() {
        super.animate();

        if (getCookTimer() == 1) worldObj.playSound(null, getPos(), HFSounds.POT, SoundCategory.BLOCKS, 1F, 1F);
    }
}