package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.cooking.Utensil;

public class TileOven extends TileCooking {
    @Override
    public Utensil getUtensil() {
        return Utensil.OVEN;
    }
}
