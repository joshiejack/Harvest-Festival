package uk.joshiejack.harvestcore.ticker.crop;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import uk.joshiejack.harvestcore.database.QualityEvents;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.harvestcore.stage.StageHandler;
import uk.joshiejack.harvestcore.ticker.HasQuality;
import uk.joshiejack.harvestcore.ticker.QualityPlaced;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.seasons.handlers.SeasonHandler;

import javax.annotation.Nullable;
import java.util.*;

public class CropTicker extends SoilTicker implements HasQuality, INBTSerializable<NBTTagCompound> {
    public static final Map<Block, CropOverride> OVERRIDE_MAP = new HashMap<>();
    public static final List<IBlockState> RUBBISH = Lists.newArrayList();
    private final StageHandler<?> handler;
    private final SeasonHandler seasons;
    private final boolean water;
    private Quality quality = null;
    private int stage;

    public CropTicker(String type, StageHandler<?> handler, SeasonHandler seasons, boolean water) {
        super(type);
        this.handler = handler;
        this.seasons = seasons;
        this.stage = 0;
        this.water = water;
    }

    @Override
    public DailyTicker newInstance() {
        return new CropTicker(getType(), handler, seasons, water);
    }

    private boolean grow(Random random) {
        if (stage < handler.getMaximumStage()) {
            stage++;
        }

        if (stage >= handler.getMaximumStage()) {
            quality = QualityEvents.getQualityFromScript("getQualityFromFertilizer", fertilizer.getQuality());
        }


        return handler.grow(stage);
    }

    //Speed = 10 and 25
    //Quality = 30 and 40
    private boolean fertilised() {
        int mod = 100 / fertilizer.getSpeed();
        return (stage - 1) %mod == 0;
    }

    @Override
    public EventPriority getPriority() {
        return EventPriority.HIGH;
    }

    @Nullable
    @Override
    public Quality getQuality() {
        return quality;
    }

    @Override
    public void onAdded(World world, Chunk chunk, BlockPos pos, IBlockState state) {
        DailyTicker entry = TickerHelper.getTicker(world, pos.down());
        if (entry instanceof SoilTicker) {
            SoilTicker soil = (SoilTicker) entry; //We newInstance the stats, to save us from checking every time
            fertilizer = soil.fertilizer; //Copy the stats of the soil underneath us
        }
    }

    protected boolean isValidSoil(World world, BlockPos pos, IBlockState state) {
        return state == BlockStates.WET_SOIL;
    }

    private boolean isValidSeason(World world, BlockPos pos, IBlockState state) {
        return seasons.isValidSeason(world, pos);
    }

    @Override
    public void onChanged(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        int newMeta = newState.getBlock().getMetaFromState(newState);
        int oldMeta = oldState.getBlock().getMetaFromState(oldState);
        if (oldMeta > newMeta) {
            stage = 0; //Reset the stage to 0
            int toMatch = 0; //We need to match this with the metadatavalue
            while (toMatch != newMeta) {
                if (handler.grow(stage)) {
                    toMatch++;
                }

                stage++;
            }
        }
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        BlockPos down = pos.down();
        IBlockState soil = world.getBlockState(down);
        if ((water && !isValidSoil(world, down, soil))) return;
        if (!isValidSeason(world, down, soil)) {
            world.setBlockState(pos, RUBBISH.get(world.rand.nextInt(RUBBISH.size())));
            soil.getBlock().onFallenUpon(world, down, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos), 1F);
            DailyTicker entry = TickerHelper.getTicker(world, pos.down());
            if (entry != null && entry.getClass() == SoilTicker.class) {
                ((SoilTicker)entry).setFertilizer((WorldServer) world, pos, Fertilizer.NONE);
            }
        } else {
            if (grow(world.rand)) {
                EnumMap<EnumFacing, IBlockState> surroundings = new EnumMap<>(EnumFacing.class);
                for (EnumFacing facing: EnumFacing.HORIZONTALS) {
                    surroundings.put(facing, world.getBlockState(pos.offset(facing)));
                }

                world.setLightFor(EnumSkyBlock.BLOCK, pos, 15);
                CropOverride override = OVERRIDE_MAP.get(state.getBlock());
                if (override != null) override.tickCrop(world, pos, state);
                else world.immediateBlockTick(pos, state, world.rand);
                //Check the surrounding blocks to see if they are relevant?
                for (EnumFacing facing: EnumFacing.HORIZONTALS) {
                    if (world.getBlockState(pos.offset(facing)) != surroundings.get(facing)) {
                        DailyTicker entry = TickerHelper.getTicker(world, pos.offset(facing));
                        if (entry instanceof QualityPlaced) {
                            ((QualityPlaced)entry).setQuality(quality); //If something has grown, reset this plants stage
                            stage = 0; //Reset
                        }
                    }
                }
            }

            if (fertilizer.getSpeed() > 0) {
                int growths = 0;
                while (fertilised() && grow(world.rand)) {
                    world.scheduleBlockUpdate(pos, state.getBlock(), 10, 1);
                    growths++;

                    if (growths >= handler.getMaximumStage()) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        tag.setByte("Stage", (byte) stage);
        if (quality != null) {
            tag.setString("Quality", quality.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        super.deserializeNBT(tag);
        stage = tag.getByte("Stage");
        if (tag.hasKey("Quality")) {
            quality = Quality.REGISTRY.get(new ResourceLocation(tag.getString("Quality")));
        }
    }
}
