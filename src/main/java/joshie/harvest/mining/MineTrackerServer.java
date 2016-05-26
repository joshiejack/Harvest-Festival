package joshie.harvest.mining;

import joshie.harvest.mining.data.WorldLocation;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.mining.data.Mine;
import joshie.harvest.mining.data.MineData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MineTrackerServer extends MineTracker {
    public static HashMap<WorldLocation, MineData> map = new HashMap<WorldLocation, MineData>(); // Block > Mine and Level Mappings
    public static HashSet<Mine> mines = new HashSet<Mine>(); //List of all mines in the world, for iteration purposes

    @Override
    public void newDay() {
        for (Mine mine : mines) {
            mine.newDay();
        }
    }

    private WorldLocation getKey(World world, BlockPos pos) {
        return new WorldLocation(world.provider.getDimension(), pos);
    }

    /*public void addToMine(World world, BlockPos pos, EntityNPCMiner npc, String name) {
        WorldLocation key = getKey(world, pos);
        MineData data = map.get(key);
        if (data == null) {
            data = new MineData(name);
            map.put(key, data);
        }

        int level = data.getMine().addLevel();
        HFTrackers.markDirty();

        npc.startBuild(world, pos, level);
    }*/

    public void completeMine(World world, BlockPos pos, ArrayList<PlaceableBlock> blocks) {
        WorldLocation key = getKey(world, pos);
        MineData data = map.get(key);
        data.getMine().complete(world, pos, blocks);
        HFTrackers.markDirty(world);
    }

    public void destroyLevel(World world, BlockPos pos) {
        WorldLocation key = getKey(world, pos);
        MineData data = map.get(key);
        if (data == null || data.getLevel() == null) {
            return;
        } else {
            data.getLevel().destroy();
            HFTrackers.markDirty(world);
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        //Read in the mine data
        NBTTagList mine = nbt.getTagList("Mines", 10);
        for (int i = 0; i < mine.tagCount(); i++) {
            NBTTagCompound tag = mine.getCompoundTagAt(i);
            Mine m = new Mine();
            m.readFromNBT(tag);
            mines.add(m);
        }

        //Now that we have loaded in the Mine Data itself, we can add in the shortcut mappings
        NBTTagList mapping = nbt.getTagList("MineMappings", 10);
        for (int i = 0; i < mapping.tagCount(); i++) {
            NBTTagCompound tag = mapping.getCompoundTagAt(i);
            WorldLocation location = new WorldLocation();
            location.readFromNBT(tag);
            MineData data = new MineData();
            data.readFromNBT(tag);
            map.put(location, data);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        //Save the mine data
        NBTTagList mine = new NBTTagList();
        for (Mine m : mines) {
            NBTTagCompound tag = new NBTTagCompound();
            m.writeToNBT(tag);
            mine.appendTag(tag);
        }

        nbt.setTag("Mines", mine);

        //Now Saves the mappings
        NBTTagList mapping = new NBTTagList();
        for (Map.Entry<WorldLocation, MineData> entry : map.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getKey().writeToNBT(tag);
            entry.getValue().writeToNBT(tag);
            mapping.appendTag(tag);
        }

        nbt.setTag("MineMappings", mapping);
        return nbt;
    }
}
