package joshie.harvest.cooking.blocks;

import joshie.harvest.cooking.Utensil;

public class TileCounter extends TileCooking {
    @Override
    public short getCookingTime() {
        return 30;
    }

    @Override
    public Utensil getUtensil() {
        return Utensil.COUNTER;
    }
}