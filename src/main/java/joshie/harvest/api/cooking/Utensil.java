package joshie.harvest.api.cooking;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

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
    private ItemStack burntItem;

    public Utensil(String label) {
        this(getNextID(), label);
    }

    private Utensil(int index, String label){
        if (index >= UTENSILS.length) {
            Utensil[] tmp = new Utensil[index + 1];
            System.arraycopy(UTENSILS, 0, tmp, 0, UTENSILS.length);
            UTENSILS = tmp;
        }

        this.label = label;
        this.index = index;
        UTENSILS[index] = this;
    }

    public Utensil setBurntItem(ItemStack burntItem) {
        this.burntItem = burntItem;
        return this;
    }

    public ItemStack getBurntItem() {
        return burntItem;
    }

    /** Return the resource location to use for this utensil
     *  when a recipe is burnt */
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelForMeal() {
        return new ModelResourceLocation("harvestfestival:meal", "burnt_" + label);
    }

    /** Return the unlocalized name for this utensil when it's burnt **/
    public String getUnlocalizedName() {
        return "harvestfestival.meal.burnt." + label.replace("_", ".").toLowerCase(Locale.ENGLISH);
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
