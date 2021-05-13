package uk.joshiejack.harvestcore.world;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.ticker.growable.SpreadableTicker;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerHelper;

import java.util.Set;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class SpreadableNotifier {
    private static final Int2ObjectMap<Set<BlockPos>> spreadables = new Int2ObjectOpenHashMap<>();
    public static void markAsSpreadable(World world, BlockPos pos) {
            getSet(world).add(pos);
    }

    static Set<BlockPos> getSet(World world) {
        if (!spreadables.containsKey(world.provider.getDimension())) {
            spreadables.put(world.provider.getDimension(), Sets.newHashSet());
        }

        return spreadables.get(world.provider.getDimension());
    }

    @SubscribeEvent
    public static void onNotifying(BlockEvent.NeighborNotifyEvent event) {
        Set<BlockPos> set = getSet(event.getWorld());
        if (set.contains(event.getPos())) {
            DailyTicker entry = TickerHelper.getTicker(event.getWorld(), event.getPos());
            if (entry instanceof SpreadableTicker) {
                ((SpreadableTicker) entry).setStarter();
            }

            set.remove(event.getPos()); //Remove it no matter what
        }
    }
}
