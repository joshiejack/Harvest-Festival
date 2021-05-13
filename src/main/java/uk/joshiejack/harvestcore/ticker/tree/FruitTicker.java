package uk.joshiejack.harvestcore.ticker.tree;

import uk.joshiejack.harvestcore.registry.Quality;
import uk.joshiejack.harvestcore.ticker.HasQuality;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class FruitTicker extends AbstractTreeTicker implements HasQuality, INBTSerializable<NBTTagCompound> {
    private final boolean[] skip;
    private final boolean[] skipped;
    private final SeasonHandler seasons;
    private final int maximum;

    private Quality quality = null;

    public FruitTicker(String type, SeasonHandler seasons, int maximum, boolean[] skip) {
        super(type);
        this.seasons = seasons;
        this.maximum = maximum;
        this.skip = skip;
        this.skipped = new boolean[skip.length];
    }

    @Override
    public DailyTicker newInstance() {
        return new FruitTicker(getType(), seasons, maximum, skip);
    }

    @Nullable
    @Override
    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        if (!seasons.isValidSeason(world, pos)) {
            world.setBlockToAir(pos);
        } else {
            if (age < maximum) {
                super.tick(world, pos, state);
                if (skip[age] && !skipped[age]) {
                    ((IGrowable) state.getBlock()).grow(world, world.rand, pos, state);
                    ((IGrowable) state.getBlock()).grow(world, world.rand, pos, world.getBlockState(pos));
                    skipped[age] = true;
                } else ((IGrowable) state.getBlock()).grow(world, world.rand, pos, state);
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        for (int i = 0; i < skipped.length; i++) {
            tag.setBoolean("Skipped" + i, skipped[i]);
        }

        if (quality != null) {
            tag.setString("Quality", quality.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        super.deserializeNBT(tag);
        for (int i = 0; i < skipped.length; i++) {
            skipped[i] = tag.getBoolean("Skipped" + i);
        }

        if (tag.hasKey("Quality")) {
            quality = Quality.REGISTRY.get(new ResourceLocation(tag.getString("Quality")));
        }
    }
}
