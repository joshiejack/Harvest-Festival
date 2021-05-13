package uk.joshiejack.settlements.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.network.book.PacketSyncInformation;
import uk.joshiejack.penguinlib.events.UniversalGuidePacketEvent;

import static uk.joshiejack.settlements.Settlements.MODID;

@GameRegistry.ObjectHolder(Settlements.MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class AdventureItems {
    public static final ItemNPCSpawner NPC_SPAWNER = null;
    public static final ItemRandomNPC CUSTOM_NPC_SPAWNER = null;
    public static final ItemBuilding BUILDING = null;
    public static final ItemBlueprint BLUEPRINT = null;
    public static final ItemDestroy DESTROY = null;
    public static final ItemJournal JOURNAL = null;
    public static ItemBuildingRenderer previewer;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemNPCSpawner(), new ItemRandomNPC(), new ItemBuilding(),
                                        new ItemBlueprint(), new ItemDestroy(), new ItemJournal());
            //TODO AdventureItems.previewer = null; //Register the building render item
    }

    @SubscribeEvent
    public static void onUniversalGuideOpened(UniversalGuidePacketEvent event) {
        event.setPacket(new PacketSyncInformation(AdventureDataLoader.get(event.getEntityPlayer().world)
                .getInformation(event.getEntityPlayer())));
    }
}