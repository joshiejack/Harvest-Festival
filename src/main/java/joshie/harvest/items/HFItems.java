package joshie.harvest.items;

import joshie.harvest.core.config.General;
import joshie.harvest.core.lib.SizeableMeta;
import net.minecraft.item.Item;

import java.util.EnumMap;

public class HFItems {
    //Sized Map
    private static final EnumMap<SizeableMeta, Item> SIZED = new EnumMap<SizeableMeta, Item>(SizeableMeta.class);

    //Sizeables
    public static final Item EGG = getSizedItem(SizeableMeta.EGG);
    public static final Item MILK = getSizedItem(SizeableMeta.MILK);
    public static final Item MAYONNAISE = getSizedItem(SizeableMeta.MAYONNAISE);
    public static final Item WOOL = getSizedItem(SizeableMeta.WOOL);

    public static final ItemBaseTool HAMMER = (ItemBaseTool) new ItemHammer().setUnlocalizedName("hammer");
    public static final ItemBaseTool AXE = (ItemBaseTool) new ItemAxe().setUnlocalizedName("axe");

    //Misc
    public static final Item GENERAL = new ItemGeneral().setUnlocalizedName("general.item");
    public static final ItemMeal MEAL = (ItemMeal) new ItemMeal().setUnlocalizedName("meal");
    public static final Item ANIMAL = new ItemAnimal().setUnlocalizedName("animal");
    public static final ItemTreat TREATS = (ItemTreat) new ItemTreat().setUnlocalizedName("treat");

    //Misc
    public static final ItemBuilding STRUCTURES = (ItemBuilding) new ItemBuilding().setUnlocalizedName("structures");
    public static final Item SPAWNER_NPC = new ItemNPCSpawner().setUnlocalizedName("spawner.npc");

    public static void preInit() {
        //Add the debug item
        if (General.DEBUG_MODE) {
            new ItemCheat().setUnlocalizedName("cheat");
        }
    }

    public static Item getSizedItem(SizeableMeta size) {
        if (SIZED.containsKey(size)) return SIZED.get(size);
        else {
            Item item = size.getOrCreateStack();
            SIZED.put(size, item);
            return item;
        }
    }
}