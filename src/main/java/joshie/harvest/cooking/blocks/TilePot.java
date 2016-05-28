package joshie.harvest.cooking.blocks;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;

public class TilePot extends TileHeatable {
    @Override
    public IUtensil getUtensil() {
        return Utensil.POT;
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.OVEN);
    }
}