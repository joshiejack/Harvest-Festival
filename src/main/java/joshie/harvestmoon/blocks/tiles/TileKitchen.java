package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.cooking.Utensil;

public class TileKitchen extends TileCooking {
    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public Utensil getUtensil() {
        return Utensil.KITCHEN;
    }
}
