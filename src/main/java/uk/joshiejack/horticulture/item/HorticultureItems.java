package uk.joshiejack.horticulture.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uk.joshiejack.horticulture.block.HorticultureBlocks;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import static uk.joshiejack.horticulture.Horticulture.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class HorticultureItems {
    public static final ItemCrop CROP = null;
    public static final ItemSeeds SEEDS = null;
    public static final ItemFood MEAL = null;
    public static final ItemDrink DRINK = null;
    public static final ItemWateringCan WATERING_CAN = null;
    public static final ItemSpores SPORES = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemCrop(), new ItemSeeds(), new ItemFood(),
                                        new ItemDrink(), new ItemWateringCan(), new ItemSpores());
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static void init() {
        StackHelper.registerSynonym(MEAL, DRINK, CROP);
        for (ItemCrop.Crops crop : ItemCrop.Crops.values()) {
            StackHelper.registerSynonym(crop.getName() + "_seeds", SEEDS, crop.ordinal());
        }

        for (ItemCrop.Crops crop : ItemCrop.Crops.values()) {
            OreDictionary.registerOre("crop" + StringHelper.convertEnumToOreName(crop.getName()), CROP.getStackFromEnum(crop));
        }

        OreDictionary.registerOre("treeSapling", new ItemStack(HorticultureBlocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE));
    }
}
