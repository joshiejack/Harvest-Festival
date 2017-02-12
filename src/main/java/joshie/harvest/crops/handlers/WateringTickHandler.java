package joshie.harvest.crops.handlers;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.crops.CropHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;

import static net.minecraft.world.chunk.Chunk.NULL_BLOCK_STORAGE;

@HFEvents
@SuppressWarnings("unused")
public class WateringTickHandler extends DailyTickableBlock {
    public WateringTickHandler() {
        super(Phases.MAIN);
    }

    @SubscribeEvent
    public void onChunkData(ChunkDataEvent.Load event) {
        ExtendedBlockStorage[] array = event.getChunk().getBlockStorageArray();
        Chunk chunk = event.getChunk();
        World world = chunk.getWorld();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    ExtendedBlockStorage extendedblockstorage = array[y >> 4];
                    if (extendedblockstorage != NULL_BLOCK_STORAGE) {
                        IBlockState state = extendedblockstorage.get(x, y & 15, z);
                        BlockPos pos = new BlockPos((chunk.xPosition * 16) + x, y, (chunk.zPosition * 16) + z);
                        WateringHandler handler = CropHelper.getWateringHandler(world, pos, state);
                        if (handler != null) {
                            HFApi.tickable.addTickable(world, pos, this);
                            if (!handler.isWet(world, pos, state) && world.isRaining()) {
                                extendedblockstorage.set(x, y & 15, z, handler.hydrate(world, pos, state));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onDirtTilled(UseHoeEvent event) {
        if (!event.getWorld().isRemote && !event.isCanceled()) {
            if (HFTrackers.getCalendar(event.getWorld()).getTodaysWeather().isRain() && CropHelper.isRainingAt(event.getWorld(), event.getPos().up(2))) {
                MinecraftForge.EVENT_BUS.register(new RainingSoil(event.getEntityPlayer(), event.getWorld(), event.getPos()));
            }

            HFApi.tickable.addTickable(event.getWorld(), event.getPos(), this);
        }
    }

    private static class RainingSoil {
        private int existence;
        private final EntityPlayer player;
        private final World world;
        private final BlockPos pos;

        RainingSoil(EntityPlayer player, World world, BlockPos pos) {
            this.player = player;
            this.world = world;
            this.pos = pos;
        }

        @SubscribeEvent
        public void onTick(TickEvent.WorldTickEvent event) {
            if (event.world != world) return;
            boolean remove = existence >= 30;
            if (remove) {
                HFApi.crops.hydrateSoil(player, world, pos);
                MinecraftForge.EVENT_BUS.unregister(this);
            }

            existence++;
        }
    }

    @Override
    public boolean isStateCorrect(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return CropHelper.getWateringHandler(world, pos, state) != null;
    }

    @Override
    public void newDay(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        WateringHandler handler = CropHelper.getWateringHandler(world, pos, state);
        if (handler != null) {
            if (CropHelper.isRainingAt(world, pos.up(2))) {
                if (!handler.isWet(world, pos, state)) {
                    world.setBlockState(pos, handler.hydrate(world, pos, state));
                }
            } else handler.dehydrate(world, pos, state);
        }
    }
}
