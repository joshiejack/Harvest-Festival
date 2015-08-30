package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.cooking.Utensil;

public class TileCounter extends TileCooking {
    @Override
    public short getCookingTime(IUtensil utensil) {
        return 30;
    }
    
    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public IUtensil getUtensil() {
        return Utensil.COUNTER;
    }
}
