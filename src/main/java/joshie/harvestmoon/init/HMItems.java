package joshie.harvestmoon.init;

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
import joshie.harvestmoon.items.ItemSizedEgg;
import joshie.harvestmoon.items.ItemTreat;
import joshie.harvestmoon.items.ItemWateringCan;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;

public class HMItems {
    //Overriden Items
    public static Item egg;
    
    public static Item crops;
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
        crops = new ItemCrop().setUnlocalizedName("crops.item");
        seeds = new ItemSeeds().setUnlocalizedName("crops.seeds");
        general = new ItemGeneral().setUnlocalizedName("general.item");
        meal = new ItemMeal().setUnlocalizedName("meal");
        sized = new ItemSized().setUnlocalizedName("sizeable");
        structures = new ItemBuilding().setUnlocalizedName("structures");
        spawner = new ItemNPCSpawner().setUnlocalizedName("spawner");
        treats = new ItemTreat().setUnlocalizedName("treat");

        /* Tools **/
        hoe = new ItemHoe().setUnlocalizedName("hoe");
        sickle = new ItemSickle().setUnlocalizedName("sickle");
        wateringcan = new ItemWateringCan().setUnlocalizedName("wateringcan");

        cheat = new ItemCheat().setUnlocalizedName("cheat");
        
        try {
            egg = new ItemSizedEgg().setUnlocalizedName("egg").setTextureName("egg");
            GameRegistry.addSubstitutionAlias("minecraft:egg", Type.ITEM, egg);
        } catch (ExistingSubstitutionException e) {
            e.printStackTrace();
        }
    }
}
