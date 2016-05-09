package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.config.General;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.lib.SizeableMeta;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.EnumMap;

public class HFItems {
    //Sized Map
    private static final EnumMap<SizeableMeta, Item> SIZED = new EnumMap<SizeableMeta, Item>(SizeableMeta.class);

    //Sizeables
    public static final Item EGG = getSizedItem(SizeableMeta.EGG);
    public static final Item MILK = getSizedItem(SizeableMeta.MILK);
    public static final Item MAYONNAISE = getSizedItem(SizeableMeta.MAYONNAISE);
    public static final Item WOOL = getSizedItem(SizeableMeta.WOOL);

    //Tools
    public static final Item HOE = new ItemHoe().setUnlocalizedName("hoe");
    public static final Item SICKLE = new ItemSickle().setUnlocalizedName("sickle");
    public static final Item WATERING_CAN = new ItemWateringCan().setUnlocalizedName("wateringcan");
    public static final Item HAMMER = new ItemHammer().setUnlocalizedName("hammer");
    public static final Item AXE = new ItemAxe().setUnlocalizedName("axe");

    //Misc
    public static final Item GENERAL = new ItemGeneral().setUnlocalizedName("general.item");
    public static final Item MEAL = new ItemMeal().setUnlocalizedName("meal");
    public static final Item ANIMAL = new ItemAnimal().setUnlocalizedName("animal");
    public static final ItemTreat TREATS = (ItemTreat) new ItemTreat().setUnlocalizedName("treat");
    public static final Item SEEDS = new ItemHFSeeds().setUnlocalizedName("crops.seeds");

    //Misc
    public static final Item STRUCTURES = new ItemBuilding().setUnlocalizedName("structures");
    public static final Item SPAWNER_NPC = new ItemNPCSpawner().setUnlocalizedName("spawner.npc");

    public static void preInit() {
        //Add a new crop item for things that do not have an item yet :D
        for (ICrop crop : HFApi.CROPS.getCrops()) {
            if (!crop.hasItemAssigned()) {
                crop.setItem(new ItemStack(new ItemCrop(crop).setUnlocalizedName("crop." + crop.getResource().getResourcePath()), 1, 0));
            }

            //Register always in the ore dictionary
            ItemStack clone = crop.getCropStack().copy();
            clone.setItemDamage(OreDictionary.WILDCARD_VALUE);
            String name = "crop" + WordUtils.capitalizeFully(crop.getResource().getResourcePath().replace("_", ""));
            if (!isInDictionary(name, clone)) {
                OreDictionary.registerOre(name, clone);
            }
        }

        //Add the debug item
        if (General.DEBUG_MODE) {
            new ItemCheat().setUnlocalizedName("cheat");
        }
    }

    private static boolean isInDictionary(String name, ItemStack stack) {
        for (ItemStack check: OreDictionary.getOres(name)) {
            if (check.getItem() == stack.getItem() && (check.getItemDamage() == OreDictionary.WILDCARD_VALUE || check.getItemDamage() == stack.getItemDamage())) {
                return true;
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        ItemColors colors = Minecraft.getMinecraft().getItemColors();

        colors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex) {
                if (!stack.hasTagCompound()) return -1;
                ICrop crop = SeedHelper.getCropFromSeed(stack);
                return crop != null ? crop.getColor() : -1;
            }
        }, HFItems.SEEDS);
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