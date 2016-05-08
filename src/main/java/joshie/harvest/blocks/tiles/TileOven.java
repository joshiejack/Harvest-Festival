package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.blocks.tiles.TileCooking.TileCookingTicking;
import joshie.harvest.cooking.Utensil;

public class TileOven extends TileCookingTicking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.OVEN;
    }
}