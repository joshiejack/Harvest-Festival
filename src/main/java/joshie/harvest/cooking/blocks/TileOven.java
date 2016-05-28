package joshie.harvest.cooking.blocks;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.blocks.TileCooking.TileCookingTicking;
import joshie.harvest.cooking.Utensil;

public class TileOven extends TileCookingTicking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.OVEN;
    }
}