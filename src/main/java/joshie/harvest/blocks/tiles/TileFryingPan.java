package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;

public class TileFryingPan extends TileHeatable {
    @Override
    public void animate(IUtensil utensil) {
        super.animate(utensil);
    }

    @Override
    public IUtensil getUtensil() {
        return Utensil.FRYING_PAN;
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.OVEN);
    }
}