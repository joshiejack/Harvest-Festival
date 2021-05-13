package uk.joshiejack.husbandry.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uk.joshiejack.husbandry.HusbandryConfig;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import static uk.joshiejack.husbandry.Husbandry.MODID;
import static uk.joshiejack.husbandry.block.HusbandryBlocks.PRODUCT;


@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class HusbandryItems {
    public static final ItemFood FOOD = null;
    public static final ItemDrink DRINK = null;
    public static final ItemTool TOOL = null;
    public static final ItemFeed FEED = null;
    public static final ItemSickle SICKLE = null;
    public static final ItemTreat TREAT = null;
    public static final ItemFabric FABRIC = null;
    public static final ItemTracker TRACKER = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemFood(), new ItemDrink(), new ItemTool(), new ItemFeed(),
                                        new ItemSickle(), new ItemTreat(), new ItemFabric(), new ItemTracker());
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static void init() {
        if (HusbandryConfig.enableNoDamageSickle) SICKLE.setMaxDamage(0);
        StackHelper.registerSynonym(FOOD, DRINK, FABRIC, PRODUCT, FEED, TOOL);
        StackHelper.registerSynonymWithSuffix("treat", TREAT);
        OreDictionary.registerOre("foodButter", FOOD.getStackFromEnum(ItemFood.Food.BUTTER));
        OreDictionary.registerOre("egg", FOOD.getStackFromEnum(ItemFood.Food.SMALL_DUCK_EGG));
        OreDictionary.registerOre("egg", FOOD.getStackFromEnum(ItemFood.Food.MEDIUM_DUCK_EGG));
        OreDictionary.registerOre("egg", FOOD.getStackFromEnum(ItemFood.Food.LARGE_DUCK_EGG));
        OreDictionary.registerOre("egg", FOOD.getStackFromEnum(ItemFood.Food.SMALL_EGG));
        OreDictionary.registerOre("egg", FOOD.getStackFromEnum(ItemFood.Food.MEDIUM_EGG));
        OreDictionary.registerOre("egg", FOOD.getStackFromEnum(ItemFood.Food.LARGE_EGG));
        for (ItemFabric.Fabric fabric: ItemFabric.Fabric.values()) {
            if (fabric.name().contains("YARN")) OreDictionary.registerOre("yarn", FABRIC.getStackFromEnum(fabric));
        }
    }
}
