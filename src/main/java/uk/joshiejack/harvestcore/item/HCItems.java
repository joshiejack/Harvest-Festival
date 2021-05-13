package uk.joshiejack.harvestcore.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.data.custom.AbstractCustomObject;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

@GameRegistry.ObjectHolder(HarvestCore.MODID)
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class HCItems {
    //public static final ItemFertilizer FERTILIZER = null;
    public static final ItemSeeds SEED_BAG = null;
    public static final ItemSeedlings SEEDLING_BAG = null;
    public static final ItemBlueprint BLUEPRINT = null;
    public static final ItemNote NOTE = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemSeeds(), new ItemSeedlings(), new ItemBlueprint(), new ItemNote());
        ResourceLoader.loadJsonForMod(AbstractCustomObject.class, "items", HarvestCore.MODID)
                .stream().map(json -> (Item) CustomLoader.build(json))
                .forEach(item ->
                        CustomLoader.getInstance().itemForgeRegistry
                                .register(item, event.getRegistry()));
    }

    @SuppressWarnings("ConstantConditions")
    public static void init() {
       // Fertilizer.REGISTRY.keySet().forEach(name -> StackHelper.registerSynonym(name.getPath() + "_fertilizer", FERTILIZER.getStackFromResource(name)));
        //Spreadable.REGISTRY.keySet().forEach(name -> StackHelper.registerSynonym(name.getPath() + "_starter", STARTER.getStackFromResource(name)));
    }
}
