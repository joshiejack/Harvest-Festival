package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TileCounter extends TileCooking {
    @Override
    public short getCookingTime() {
        return 30;
    }

    @Override
    public Utensil getUtensil() {
        return Utensil.COUNTER;
    }

    @Override
    public void animate() {
        super.animate();

        if (getCookTimer() %5 == 0) worldObj.playSound(null, getPos(), HFSounds.COUNTER, SoundCategory.BLOCKS, 1F, 1F);
    }
}