package joshie.harvest.api.cooking;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.creativetab.CreativeTabs.CREATIVE_TAB_ARRAY;

public class Utensil {
    public static Utensil[] UTENSILS = new Utensil[5];

    public static final Utensil COUNTER = new Utensil(0, "counter");
    public static final Utensil POT = new Utensil(1, "pot");
    public static final Utensil FRYING_PAN = new Utensil(2, "frying_pan");
    public static final Utensil MIXER = new Utensil(3, "mixer");
    public static final Utensil OVEN = new Utensil(4, "oven");

    private final String label;
    private final int index;

    public Utensil(String label) {
        this(getNextID(), label);
    }

    private Utensil(int index, String label){
        if (index >= UTENSILS.length) {
            Utensil[] tmp = new Utensil[index + 1];
            for (int x = 0; x < UTENSILS.length; x++) {
                tmp[x] = UTENSILS[x];
            }

            UTENSILS = tmp;
        }

        this.label = label;
        this.index = index;
        UTENSILS[index] = this;
    }

    /** Return the resource location to use for this utensil
     *  when a recipe is burnt */
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelForMeal() {
        return new ModelResourceLocation(new ResourceLocation("harvestfestival", "meals/burnt_" + label), "inventory");
    }

    /** Return the unlocalized name for this utensil when it's burnt **/
    public String getUnlocalizedName() {
        return "harvestfestival.meal.burnt." + label.replace("_", ".").toLowerCase();
    }

    public final int getIndex() {
        return index;
    }

    public static Utensil getUtensilFromIndex(int index) {
        return UTENSILS[Math.max(0, Math.min(UTENSILS.length - 1, index))];
    }

    private static int getNextID() {
        return CREATIVE_TAB_ARRAY.length;
    }
}
