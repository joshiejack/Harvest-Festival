package uk.joshiejack.gastronomy.tile.base;

import uk.joshiejack.gastronomy.api.Appliance;
import net.minecraft.util.ITickable;

public class TileCookingTicking extends TileCookingFluids implements ITickable {
    public TileCookingTicking(Appliance appliance, int timeRequired) {
        super(appliance, timeRequired);
    }
    public TileCookingTicking(Appliance appliance) {
        super(appliance);
    }

    @Override
    public void update() {
        updateTick();
    }
}
