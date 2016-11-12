package joshie.harvest.gathering;

import com.google.common.cache.Cache;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.town.data.TownBuilding;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class GatheringData {
    private Set<GatheringLocation> locations = new HashSet<>();

    public void newDay(World world, BlockPos townCentre, Collection<TownBuilding> buildings, Cache<BlockPos, Boolean> isFar) {
        Set<GatheringLocation> previous = new HashSet<>(locations);
        locations = new HashSet<>();
        //Remove all previous locations
        for (GatheringLocation location : previous) {
            if (world.isBlockLoaded(location.pos)) {
                IBlockState state = world.getBlockState(location.pos);
                if (state.getBlock() == location.block && state.getBlock().getMetaFromState(state) == location.meta) {
                    if (state.getBlock().getMetaFromState(state) == location.meta) {
                        world.setBlockToAir(location.pos);
                    }
                }
            } else locations.add(location); //Add them back
        }

        //Create some new spawn spots based on where we have buildings
        Season season = HFApi.calendar.getDate(world).getSeason();
        int placed = 0;

        for (int i = 0; i < 2048 && placed < 256; i++) {
            BlockPos pos = world.getTopSolidOrLiquidBlock(townCentre.add(128 - world.rand.nextInt(256), 64, 128 - world.rand.nextInt(256)));
            if (world.isBlockLoaded(pos) && world.getBlockState(pos.down()).getBlock() == Blocks.GRASS && world.isAirBlock(pos) && world.canBlockSeeSky(pos) &&
                    isAtLeast32BlocksAwayFromEverything(isFar, buildings, pos)) {
                IBlockState random = HFApi.gathering.getRandomStateForSeason(world, season);
                if (random != null && world.setBlockState(pos, random, 2)) {
                    locations.add(new GatheringLocation(random, pos));
                    placed++;
                }
            }
        }
    }

    private boolean isAtLeast32BlocksAwayFromEverything(Cache<BlockPos, Boolean> isFar, Collection<TownBuilding> buildings, BlockPos pos) {
        try {
            return isFar.get(pos, () -> {
                for (TownBuilding building: buildings) {
                    if (building.pos.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 32) return false;
                }

                return true;
            });
        } catch (ExecutionException ex) { return false; }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("GatheringLocations", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            GatheringLocation location = new GatheringLocation();
            location.readFromNBT(tag);
            locations.add(location);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (GatheringLocation location : locations) {
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("GatheringLocations", list);
    }
}