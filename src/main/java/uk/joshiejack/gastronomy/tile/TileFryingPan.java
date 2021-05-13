package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.GastronomySounds;
import uk.joshiejack.gastronomy.tile.base.TileCookingHeatable;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("frying_pan")
public class TileFryingPan extends TileCookingHeatable {
    public TileFryingPan() {
        super(Appliance.FRYING_PAN, GastronomySounds.FRYING_PAN);
    }
}
