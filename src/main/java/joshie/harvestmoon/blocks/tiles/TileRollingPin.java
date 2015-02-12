package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.cooking.Utensil;

public class TileRollingPin extends TileCooking {
    @Override
    public Utensil getUtensil() {
        return Utensil.ROLLING_PIN;
    }
}
