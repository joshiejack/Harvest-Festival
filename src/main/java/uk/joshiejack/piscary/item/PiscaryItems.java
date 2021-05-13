package uk.joshiejack.piscary.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.piscary.PiscaryConfig;

import static uk.joshiejack.piscary.Piscary.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class PiscaryItems {
    public static final ItemBait BAIT = null;
    public static final ItemFish FISH = null;
    public static final ItemLoot LOOT = null;
    public static final ItemMeal MEAL = null;
    public static final ItemFishingRod FISHING_ROD = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemBait(), new ItemFish(), new ItemLoot(), new ItemMeal(), new ItemFishingRod());
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static void init() {
        if (PiscaryConfig.enableNoDamageFishingRod) FISHING_ROD.setMaxDamage(0);
        OreDictionary.registerOre("fish", new ItemStack(FISH, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("fishCod", FISH.getStackFromEnum(ItemFish.Fish.COD, 1));
        OreDictionary.registerOre("fishSalmon", FISH.getStackFromEnum(ItemFish.Fish.SALMON, 1));
        OreDictionary.registerOre("fishClownfish", FISH.getStackFromEnum(ItemFish.Fish.CLOWN, 1));
        OreDictionary.registerOre("fishPufferfish", FISH.getStackFromEnum(ItemFish.Fish.PUFFER, 1));
        StackHelper.registerSynonym(FISH, MEAL, LOOT);
        StackHelper.registerSynonymWithSuffix("bait", BAIT);
    }
}
