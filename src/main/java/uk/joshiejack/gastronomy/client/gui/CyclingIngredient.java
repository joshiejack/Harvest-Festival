package uk.joshiejack.gastronomy.client.gui;

import uk.joshiejack.gastronomy.cooking.IngredientStack;
import uk.joshiejack.penguinlib.client.gui.CyclingStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CyclingIngredient extends CyclingStack {
    private final IngredientStack ingredient;

    public CyclingIngredient(IngredientStack ingredient) {
        super(ingredient.getAsStacks());
        this.ingredient = ingredient;
    }

    public IngredientStack getIngredient() {
        return ingredient;
    }
}
