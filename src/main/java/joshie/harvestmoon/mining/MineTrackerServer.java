package joshie.harvestmoon.mining;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.generic.ServerHelper.getWorld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import joshie.harvestmoon.blocks.BlockDirt;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.npc.EntityNPCMiner;
import joshie.harvestmoon.util.IData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class MineTrackerServer implements IData {
    private HashSet<MineBlockLocation> mineBlocks = new HashSet();
    private HashSet<MineLocation> mineLocation = new HashSet();

    private MineBlockLocation getKey(World world, int x, int y, int z, int level) {
        return new MineBlockLocation(world.provider.dimensionId, x, y, z, level);
    }

    private MineLocation getKey(World world, int x, int y, int z, int level, String name) {
        return new MineLocation(world.provider.dimensionId, x, y, z, level, name);
    }

    public boolean newDay() {        
        Iterator<MineBlockLocation> it = mineBlocks.iterator();
        while (it.hasNext()) {
            MineBlockLocation location = it.next();
            int level = location.getLevel();
            if (level >= 0) {
                World world = getWorld(location.dimension);
                ArrayList<Integer> metas = BlockDirt.getMeta(level);
                int meta = metas.get(world.rand.nextInt(metas.size()));
                world.setBlock(location.x, location.y, location.z, HMBlocks.dirt, meta, 2);
            }
        }

        return true;
    }

    public void addMineBlock(World world, int x, int y, int z, int level) {
        mineBlocks.add(getKey(world, x, y, z, level));
        handler.getServer().markDirty();
    }

    /** Call this to add a new mine at the location under the following name **/
    public void addMineLevel(World world, int x, int y, int z, String name, EntityNPCMiner npc) {
        MineLocation key = getKey(world, x, y, z, 0, name);
        MineLocation location = null;
        for (MineLocation local : mineLocation) {
            if (local.equals(key)) {
                location = local;
                break;
            }
        }

        if (location == null) {
            location = key;
            mineLocation.add(location);
        } else location.addLevel();

        npc.startBuild(world, x, y, z, location.getLevel());
        handler.getServer().markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList mineBlock = nbt.getTagList("MineBlocks", 10);
        for (int i = 0; i < mineBlock.tagCount(); i++) {
            NBTTagCompound tag = mineBlock.getCompoundTagAt(i);
            MineBlockLocation location = new MineBlockLocation();
            location.readFromNBT(tag);
            mineBlocks.add(location);
        }

        NBTTagList locations = nbt.getTagList("MineLocations", 10);
        for (int i = 0; i < locations.tagCount(); i++) {
            NBTTagCompound tag = locations.getCompoundTagAt(i);
            MineLocation location = new MineLocation();
            location.readFromNBT(tag);
            mineLocation.add(location);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList mineBlock = new NBTTagList();
        for (MineBlockLocation location : mineBlocks) {
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            mineBlock.appendTag(tag);
        }

        nbt.setTag("MineBlocks", mineBlock);

        NBTTagList locations = new NBTTagList();
        for (MineLocation location : mineLocation) {
            NBTTagCompound tag = new NBTTagCompound();
            location.writeToNBT(tag);
            locations.appendTag(tag);
        }

        nbt.setTag("MineLocations", locations);
    }
}
