package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.GastronomySounds;
import uk.joshiejack.gastronomy.tile.base.TileCookingHeatable;
import uk.joshiejack.penguinlib.block.interfaces.ITankProvider;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("pot")
public class TilePot extends TileCookingHeatable implements ITankProvider {
    public TilePot() {
        super(Appliance.POT, GastronomySounds.POT);
    }
}
