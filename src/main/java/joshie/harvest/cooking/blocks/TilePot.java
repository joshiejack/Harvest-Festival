package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;

public class TilePot extends TileHeatable {
    @Override
    public Utensil getUtensil() {
        return Utensil.POT;
    }

    @Override
    public boolean hasPrerequisites() {
        return isAbove(Utensil.OVEN);
    }
}