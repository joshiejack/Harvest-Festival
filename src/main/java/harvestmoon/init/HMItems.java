package harvestmoon.init;

import harvestmoon.items.ItemBuilding;
import harvestmoon.items.ItemCheat;
import harvestmoon.items.ItemCrop;
import harvestmoon.items.ItemGeneral;
import harvestmoon.items.ItemHoe;
import harvestmoon.items.ItemMeal;
import harvestmoon.items.ItemSeeds;
import harvestmoon.items.ItemSickle;
import harvestmoon.items.ItemSized;
import harvestmoon.items.ItemWateringCan;
import net.minecraft.item.Item;

public class HMItems {
    public static Item crops;
    public static Item seeds;
    public static Item general;
    public static Item meal;
    public static Item sized;
    public static Item structures;

    public static Item cheat;

    //Tool Items
    public static Item hoe;
    public static Item sickle;
    public static Item wateringcan;

    public static void init() {
        crops = new ItemCrop().setUnlocalizedName("crops.item");
        seeds = new ItemSeeds().setUnlocalizedName("crops.seeds");
        general = new ItemGeneral().setUnlocalizedName("general");
        meal = new ItemMeal().setUnlocalizedName("meal");
        sized = new ItemSized().setUnlocalizedName("sizeable");
        structures = new ItemBuilding().setUnlocalizedName("structures");

        /* Tools **/
        hoe = new ItemHoe().setUnlocalizedName("hoe");
        sickle = new ItemSickle().setUnlocalizedName("sickle");
        wateringcan = new ItemWateringCan().setUnlocalizedName("wateringcan");

        cheat = new ItemCheat().setUnlocalizedName("cheat");
    }
}
