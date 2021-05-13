package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.GastronomySounds;
import uk.joshiejack.gastronomy.tile.base.TileCookingFluids;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.SoundCategory;

@PenguinLoader("counter")
public class TileCounter extends TileCookingFluids {
    private long lastHit;

    public TileCounter() {
        super(Appliance.COUNTER, 30);
    }

    @Override
    public void animate() {
        long thisHit = System.currentTimeMillis();
        if (thisHit - lastHit >= 201 || cookTimer % 3 == 0) {
            world.playSound(getPos().getX(), getPos().getY(), getPos().getZ(), GastronomySounds.COUNTER, SoundCategory.BLOCKS, 1F, 1F, false);
        }

        lastHit = System.currentTimeMillis();
    }
}
