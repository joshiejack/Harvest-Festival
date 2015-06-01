package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;

public class TileOven extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.OVEN;
    }
}
