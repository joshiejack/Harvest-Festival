package uk.joshiejack.harvestcore.ticker.tree;

import uk.joshiejack.harvestcore.database.QualityEvents;
import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.harvestcore.ticker.HasQuality;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerHelper;
import uk.joshiejack.penguinlib.ticker.TickerRegistry;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.SeasonsConfig;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class LeavesTicker extends AbstractTreeTicker implements HasQuality, INBTSerializable<NBTTagCompound> {
    private Quality quality = null;
    private int stage;
    private final SeasonHandler seasons;
    private final int days;

    public LeavesTicker(String type, SeasonHandler seasons, int days) {
        super(type);
        this.seasons = seasons;
        this.days = days;
    }

    @Override
    public DailyTicker newInstance() {
        return new LeavesTicker(getType(), seasons, days);
    }

    @Nullable
    @Override
    public Quality getQuality() {
        return quality;
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        super.tick(world, pos, state);
        stage++; //increase the current stage
        if (seasons.isValidSeason(world, pos) && stage >= days) {
            if (state.getBlock() instanceof IGrowable) {
                ((IGrowable) state.getBlock()).grow(world, world.rand, pos, state);
            }

            stage = 0; //Reset the age now that we have grown, so that we count the days again

            BlockPos down = pos.down();
            IBlockState stateDown = world.getBlockState(down);
            if (TickerRegistry.hasTickingEntry(stateDown)) {
                DailyTicker entry = TickerHelper.getTicker(world, down);
                if (entry instanceof FruitTicker) {
                    ((FruitTicker)entry).setQuality(quality);
                }
            }
        }

        quality = QualityEvents.getQualityFromScript("getQualityFromTreeAge", age, (SeasonsConfig.daysPerSeasonMultiplier * Seasons.DAYS_PER_SEASON) * 4); //4 seasons
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        tag.setInteger("Stage", stage);
        if (quality != null) {
            tag.setString("Quality", quality.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        super.deserializeNBT(tag);
        stage = tag.getInteger("Stage");
        if (tag.hasKey("Quality")) {
            quality = Quality.REGISTRY.get(new ResourceLocation(tag.getString("Quality")));
        }
    }
}
