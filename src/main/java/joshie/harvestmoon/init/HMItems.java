package joshie.harvestmoon.init;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.items.ItemBuilding;
import joshie.harvestmoon.items.ItemCheat;
import joshie.harvestmoon.items.ItemCrop;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.items.ItemHoe;
import joshie.harvestmoon.items.ItemMeal;
import joshie.harvestmoon.items.ItemNPCSpawner;
import joshie.harvestmoon.items.ItemSeeds;
import joshie.harvestmoon.items.ItemSickle;
import joshie.harvestmoon.items.ItemSized;
import joshie.harvestmoon.items.ItemTreat;
import joshie.harvestmoon.items.ItemWateringCan;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.text.WordUtils;

public class HMItems {
    public static Item seeds;
    public static Item general;
    public static Item meal;
    public static Item sized;
    public static Item structures;
    public static Item spawner;
    public static Item treats;

    public static Item cheat;

    //Tool Items
    public static Item hoe;
    public static Item sickle;
    public static Item wateringcan;

    public static void init() {
        //Add a new crop item for things that do not have an item yet :D
        for (ICrop crop : Crop.crops) {
            if (!crop.hasItemAssigned()) {
                crop.setItem(new ItemCrop(crop).setUnlocalizedName("crop." + crop.getUnlocalizedName()));
                ItemStack clone = crop.getCropStack().copy();
                clone.setItemDamage(OreDictionary.WILDCARD_VALUE);
                OreDictionary.registerOre("crop" + WordUtils.capitalizeFully(crop.getUnlocalizedName().replace("_", "")), clone);
            }
        }

        seeds = new ItemSeeds().setUnlocalizedName("crops.seeds");
        general = new ItemGeneral().setUnlocalizedName("general.item");
        meal = new ItemMeal().setUnlocalizedName("meal");
        sized = new ItemSized().setUnlocalizedName("sizeable");
        treats = new ItemTreat().setUnlocalizedName("treat");
        
        /* Tools **/
        hoe = new ItemHoe().setUnlocalizedName("hoe");
        sickle = new ItemSickle().setUnlocalizedName("sickle");
        wateringcan = new ItemWateringCan().setUnlocalizedName("wateringcan");

        if (General.DEBUG_MODE) {
            cheat = new ItemCheat().setUnlocalizedName("cheat");
            spawner = new ItemNPCSpawner().setUnlocalizedName("spawner");
            structures = new ItemBuilding().setUnlocalizedName("structures");
        }
    }

}
