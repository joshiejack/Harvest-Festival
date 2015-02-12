package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.cooking.Utensil;

public class TileKnife extends TileCooking {
    @Override
    public Utensil getUtensil() {
        return Utensil.KNIFE;
    }
}
