package joshie.harvest.player.town;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class GatheringData {
    private Set<GatheringLocation> locations = new HashSet();
    
    public ItemStack getRandomBlock() {
        return new ItemStack(Blocks.obsidian, 1, 0);
    }

    public void newDay(Collection<TownBuilding> buildings) {
        Set<GatheringLocation> previous = new HashSet(locations);
        locations = new HashSet();

        //Remove all previous locations
        for (GatheringLocation location : previous) {
            World world = DimensionManager.getWorld(location.dimension);
            if (world.getBlock(location.x, location.y, location.z) == location.block) {
                if (world.getBlockMetadata(location.x, location.y, location.z) == location.meta) {
                    world.setBlockToAir(location.x, location.y, location.z);
                }
            }
        }

        //Create some new spawn spots based on where we have buildings
        for (TownBuilding building: buildings) {
            World world = DimensionManager.getWorld(building.dimension);
            int placed = 0;
            for (int i = 0; i < 64 && placed < 10; i++) {
                int x = building.xCoord + 32 - world.rand.nextInt(64);
                int y = building.yCoord + 4 - world.rand.nextInt(8);
                int z = building.zCoord + 32 - world.rand.nextInt(64); 
                if (world.getBlock(x, y, z) == Blocks.grass && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z)) {
                    ItemStack random = getRandomBlock();
                    Block block = Block.getBlockFromItem(random.getItem());
                    int meta = random.getItemDamage();
                    if(world.setBlock(x, y + 1, z, block, meta, 2)) {
                        locations.add(new GatheringLocation(block, meta, building.dimension, x, y + 1, z));
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
