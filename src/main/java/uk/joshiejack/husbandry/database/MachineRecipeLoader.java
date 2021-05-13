package uk.joshiejack.husbandry.database;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.block.BlockIncubator;
import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.tile.*;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.DatabaseHelper;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = Husbandry.MODID)
public class MachineRecipeLoader {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        DatabaseHelper.registerSimpleMachine(event, "fermenter", TileFermenter.registry);
        DatabaseHelper.registerSimpleMachine(event, "spinning_wheel", TileSpinningWheel.registry);
        DatabaseHelper.registerSimpleMachine(event, "oil_maker", TileOilMaker.registry);
        DatabaseHelper.registerSimpleMachine(event, "hive", TileHive.registry);
        event.table("incubator").rows().forEach(row-> {
            Holder holder = Holder.getFromString(row.get("input"));
            ResourceLocation output = new ResourceLocation(row.get("output"));
            BlockIncubator.Fill fill = BlockIncubator.Fill.valueOf(row.get("fill").toString().toUpperCase(Locale.ENGLISH));
            if (!holder.isEmpty()) {
                TileIncubator.ITEM_REGISTRY.register(holder, output);
                TileIncubator.FILL_REGISTRY.register(holder, fill);
                TileNest.FILL_REGISTRY.register(holder, getTrayFromFill(fill));
            }
        });
    }

    private static BlockTray.Tray getTrayFromFill(BlockIncubator.Fill fill) {
        switch (fill) {
            case SMALL:
                return BlockTray.Tray.NEST_SMALL;
            case MEDIUM:
                return BlockTray.Tray.NEST_MEDIUM;
            case LARGE:
                return BlockTray.Tray.NEST_LARGE;
            default:
                return BlockTray.Tray.NEST_EMPTY;
        }
    }
}
