package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.api.cooking.IUtensil;
import joshie.harvestmoon.cooking.Utensil;

public class TileOven extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.OVEN;
    }
}
