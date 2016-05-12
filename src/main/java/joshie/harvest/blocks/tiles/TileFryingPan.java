package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;

public class TileFryingPan extends TileHeatable {
    @Override
    public IUtensil getUtensil() {
        return Utensil.FRYING_PAN;
    }
}