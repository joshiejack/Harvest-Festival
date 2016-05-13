package joshie.harvest.npc.town;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GatheringData {
    private Set<GatheringLocation> locations = new HashSet<>();

    public ItemStack getRandomBlock() {
        return new ItemStack(Blocks.OBSIDIAN, 1, 0);
    }

    public void newDay(Collection<TownBuilding> buildings) {
        /*
        Set<GatheringLocation> previous = new HashSet<GatheringLocation>(locations);
        locations = new HashSet<>();

        //Remove all previous locations
        for (GatheringLocation location : previous) {
            World world = DimensionManager.getWorld(location.dimension);
            IBlockState state = world.getBlockState(location.position);
            if (state.getBlock() == location.block) {
                if (state.getBlock().getMetaFromState(state) == location.meta) {
                    world.setBlockToAir(location.position);
                }
            }
        }

        //Create some new spawn spots based on where we have buildings
        for (TownBuilding building : buildings) {
            World world = DimensionManager.getWorld(building.dimension);
            int placed = 0;
            for (int i = 0; i < 64 && placed < 10; i++) {
                BlockPos pos = building.pos.add(32 - world.rand.nextInt(64), 4 - world.rand.nextInt(8), 32 - world.rand.nextInt(64));
                if (world.getBlockState(pos).getBlock() == Blocks.GRASS && world.isAirBlock(pos.up())) {
                    ItemStack random = getRandomBlock();
                    Block block = Block.getBlockFromItem(random.getItem());
                    int meta = random.getItemDamage();
                    if (world.setBlockState(pos.up(), block.getStateFromMeta(meta), 2)) {
                        locations.add(new GatheringLocation(block, meta, building.dimension, pos.up()));
                        placed++;
                    }
                }
            }
        } */
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