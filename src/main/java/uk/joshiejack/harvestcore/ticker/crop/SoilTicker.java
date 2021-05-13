package uk.joshiejack.harvestcore.ticker.crop;

import uk.joshiejack.harvestcore.network.fertilizer.PacketAddFertilizer;
import uk.joshiejack.harvestcore.network.fertilizer.PacketRemoveFertilizer;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.world.SeasonsWorldProvider;
import uk.joshiejack.seasons.world.weather.Weather;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

import static uk.joshiejack.penguinlib.util.BlockStates.DRY_SOIL;
import static uk.joshiejack.penguinlib.util.BlockStates.WET_SOIL;

@PenguinLoader("soil")
public class SoilTicker extends DailyTicker implements INBTSerializable<NBTTagCompound> {
    public Fertilizer fertilizer = Fertilizer.NONE;

    public SoilTicker(String type) {
        super(type);
    }

    public SoilTicker() {
        super("soil");
    }

    @Override
    public DailyTicker newInstance() {
        return new SoilTicker();
    }

    @Nonnull
    public Fertilizer getFertilizer() {
        return fertilizer == null ? Fertilizer.NONE : fertilizer;
    }

    public void setFertilizer(WorldServer world, BlockPos pos, Fertilizer fertilizer) {
        this.fertilizer = fertilizer;
        if (fertilizer.equals(Fertilizer.NONE)) {
            PenguinNetwork.sendToNearby(world, pos, new PacketRemoveFertilizer(fertilizer, pos));
        } else PenguinNetwork.sendToNearby(world, pos, new PacketAddFertilizer(fertilizer, pos));
    }

    @Override
    public void onAdded(World world, Chunk chunk, BlockPos pos, IBlockState state) {
        if (world.isRainingAt(pos.up())) {
            if (state != WET_SOIL) {
                world.setBlockState(pos, WET_SOIL);
            }
        }
    }

    //Skip the vanilla isRainingAt and check the weather instead
    private boolean isRainingAt(World world, BlockPos position) {
        Weather weather = SeasonsWorldProvider.getWorldData(world).getWeather();
        if (weather == Weather.CLEAR || weather == Weather.FOGGY) {
            return false;
        } else if (!world.canSeeSky(position)) {
            return false;
        } else if (world.getPrecipitationHeight(position).getY() > position.getY()) {
            return false;
        } else {
            Biome biome = world.getBiome(position);
            if (biome.getEnableSnow()) {
                return false;
            } else {
                return !world.canSnowAt(position, false) && biome.canRain();
            }
        }
    }

    @Override
    public void tick(World world, BlockPos pos, IBlockState state) {
        if (isRainingAt(world, pos.up())) {
            if (state != WET_SOIL) {
                world.setBlockState(pos, WET_SOIL);
            }
        } else {
            if (state == WET_SOIL) world.setBlockState(pos, DRY_SOIL);
            else if (fertilizer == Fertilizer.NONE && !(world.getBlockState(pos.up()).getBlock() instanceof IGrowable)) {
                world.setBlockState(pos, BlockStates.DIRT); //If we have no crops, then set this to dirt
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Fertilizer", fertilizer.getRegistryName().toString());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        fertilizer = Fertilizer.REGISTRY.getOrDefault(new ResourceLocation(tag.getString("Fertilizer")), Fertilizer.NONE);
    }
}
