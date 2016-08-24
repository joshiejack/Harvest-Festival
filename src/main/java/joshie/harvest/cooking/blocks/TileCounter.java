package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.lib.HFSounds;
import net.minecraft.util.SoundCategory;

public class TileCounter extends TileCooking {
    private long lastHit;

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

        long thisHit = System.currentTimeMillis();
        if (thisHit - lastHit >= 201 || getCookTimer() %3 == 0) worldObj.playSound(null, getPos(), HFSounds.COUNTER, SoundCategory.BLOCKS, 1F, 1F);
        lastHit = System.currentTimeMillis();
    }
}