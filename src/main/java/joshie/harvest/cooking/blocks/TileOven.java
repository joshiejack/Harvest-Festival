package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;
import joshie.harvest.cooking.blocks.TileCooking.TileCookingTicking;

public class TileOven extends TileCookingTicking {
    @Override
    public Utensil getUtensil() {
        return Utensil.OVEN;
    }
}