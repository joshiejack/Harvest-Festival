package uk.joshiejack.harvestcore.world.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import uk.joshiejack.harvestcore.database.WildernessRegistry;
import uk.joshiejack.harvestcore.event.WildernessCheckSpawn;
import uk.joshiejack.harvestcore.HCConfig;
import uk.joshiejack.harvestcore.world.storage.WildernessData;
import uk.joshiejack.harvestcore.world.storage.WildernessPos;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import uk.joshiejack.seasons.world.storage.WorldDataServer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class WildernessGenerator {
    private final Random seed = new Random();

    private boolean isValidLocation(World world, BlockPos target) {
        WildernessCheckSpawn event = new WildernessCheckSpawn(world, target);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() != Event.Result.DENY && (event.getResult() == Event.Result.ALLOW || world.isAirBlock(target) && world.canSeeSky(target));
    }

    private int randomOffset() {
        return seed.nextInt(HCConfig.maxWildernessDistance) - (HCConfig.maxWildernessDistance / 2);
    }

    private boolean validLocation(World world, BlockPos pos) {
        if (!(world.isBlockLoaded(pos) && isValidLocation(world, pos)))
            return false;
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            if (!world.isBlockLoaded(pos.offset(facing))) return false;
            if (!world.isAirBlock(pos.offset(facing))) return false;
        }

        return true;
    }

    @SuppressWarnings("all")
    public void onNewDay(World world, WildernessData data) {
        data.getLocations(world).removeIf(w -> {
            if (world.isBlockLoaded(w) && world.getBlockState(w) == w.getState()) {
                return world.setBlockToAir(w);
            } else return false;
        });

        WorldDataServer server = SeasonsSavedData.getWorldData(world);
        List<BlockPos> list = data.getPotentialSpawnLocations(world);
        if (!list.isEmpty()) {
            seed.setSeed(world.rand.nextInt() + world.getTotalWorldTime());
            list.forEach(centre -> {
                AtomicInteger placed = new AtomicInteger();
                IntStream.rangeClosed(0, HCConfig.maxWildernessSpawns).forEach(i -> {
                    BlockPos target = world.getTopSolidOrLiquidBlock(centre.add(randomOffset(), 0, randomOffset()));
                    if (validLocation(world, target)) {
                        Season season = server.getSeasonAt(world, target, AbstractWorldData.CheckMoreThanBiome.NO).get(0);
                        IBlockState down = world.getBlockState(target.down());
                        IBlockState state = WildernessRegistry.getRegistry(world.getBiome(target), season).get(seed);
                        if (state != null) {
                            boolean iplantable = state.getBlock() instanceof IPlantable;
                            if ((!iplantable && down.isSideSolid(world, target.down(), EnumFacing.UP))
                                    || (iplantable && down.getBlock().canSustainPlant(down, world, target.down(), EnumFacing.UP, (IPlantable) state.getBlock()))) {
                                world.setBlockState(target, state);
                                state.getBlock().onBlockPlacedBy(world, target, state, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, target),
                                        state.getBlock().getItem(world, target, state));
                                data.getLocations(world).add(new WildernessPos(state, target));
                            }
                        }
                    }
                });
            });
        }
    }
}
