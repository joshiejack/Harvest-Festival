package joshie.harvestmoon.init;

import java.util.EnumMap;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.core.lib.SizeableMeta;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.items.ItemAnimal;
import joshie.harvestmoon.items.ItemBuilding;
import joshie.harvestmoon.items.ItemCheat;
import joshie.harvestmoon.items.ItemCrop;
import joshie.harvestmoon.items.ItemGeneral;
import joshie.harvestmoon.items.ItemHoe;
import joshie.harvestmoon.items.ItemMeal;
import joshie.harvestmoon.items.ItemNPCSpawner;
import joshie.harvestmoon.items.ItemSeeds;
import joshie.harvestmoon.items.ItemSickle;
import joshie.harvestmoon.items.ItemTreat;
import joshie.harvestmoon.items.ItemWateringCan;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.text.WordUtils;

public class HMItems {
    public static EnumMap<SizeableMeta, Item> sized = new EnumMap(SizeableMeta.class);
    public static ItemAnimal animal;
    public static Item seeds;
    public static Item general;
    public static Item meal;
    public static Item structures;
    public static Item spawner;
    public static Item treats;
    public static Item cheat;
    
    public static Item egg;
    public static Item milk;
    public static Item mayonnaise;
    public static Item yarn;

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
        
        for (SizeableMeta size: SizeableMeta.values()) {
            if (size.ordinal() >= SizeableMeta.YOGHURT.ordinal()) continue;
            else {
                sized.put(size, size.getOrCreateStack());
            }
        }
        
        egg = sized.get(SizeableMeta.EGG);
        milk = sized.get(SizeableMeta.MILK);
        mayonnaise = sized.get(SizeableMeta.MAYONNAISE);
        yarn = sized.get(SizeableMeta.YARN);

        animal = (ItemAnimal) new ItemAnimal().setUnlocalizedName("animal");
        seeds = new ItemSeeds().setUnlocalizedName("crops.seeds");
        general = new ItemGeneral().setUnlocalizedName("general.item");
        meal = new ItemMeal().setUnlocalizedName("meal");
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
