package joshie.harvest.npc.town.gathering;

import joshie.harvest.api.HFApi;
import joshie.harvest.npc.town.TownBuilding;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GatheringData {
    private Set<GatheringLocation> locations = new HashSet<>();

    public void newDay(World world, Collection<TownBuilding> buildings) {
        Set<GatheringLocation> previous = new HashSet<>(locations);
        locations = new HashSet<>();
        //Remove all previous locations
        for (GatheringLocation location : previous) {
            IBlockState state = world.getBlockState(location.pos);
            if (state.getBlock() == location.block) {
                if (state.getBlock().getMetaFromState(state) == location.meta) {
                    world.setBlockToAir(location.pos);
                }
            }
        }

        //Create some new spawn spots based on where we have buildings
        for (TownBuilding building : buildings) {
            int placed = 0;
            for (int i = 0; i < 64 && placed < 10; i++) {
                BlockPos pos = building.pos.add(32 - world.rand.nextInt(64), 4 - world.rand.nextInt(8), 32 - world.rand.nextInt(64));
                if (world.getBlockState(pos).getBlock() == Blocks.GRASS && world.isAirBlock(pos.up())) {
                    IBlockState random = HFApi.gathering.getRandomStateForSeason(world, HFApi.calendar.getDate(world).getSeason());
                    if (world.setBlockState(pos.up(), random, 2)) {
                        locations.add(new GatheringLocation(random, pos.up()));
                        placed++;
                    }
                }
            }
        }
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