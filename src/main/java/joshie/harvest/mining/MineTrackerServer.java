package joshie.harvest.mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.IData;
import joshie.harvest.npc.entity.EntityNPCMiner;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class MineTrackerServer extends MineTracker implements IData {
    public static HashMap<WorldLocation, MineData> map = new HashMap(); // Block > Mine and Level Mappings
    public static HashSet<Mine> mines = new HashSet(); //List of all mines in the world, for iteration purposes

    @Override
    public void newDay() {
        for (Mine mine : mines) {
            mine.newDay();
        }
    }

    private WorldLocation getKey(World world, int x, int y, int z) {
        return new WorldLocation(world.provider.dimensionId, x, y, z);
    }

    public void addToMine(World world, int x, int y, int z, EntityNPCMiner npc, String name) {
        WorldLocation key = getKey(world, x, y, z);
        MineData data = map.get(key);
        if (data == null) {
            data = new MineData(name);
            map.put(key, data);
        }

        int level = data.getMine().addLevel();
        HFTrackers.markDirty();

        npc.startBuild(world, x, y, z, level);
    }

    public void completeMine(World world, int x, int y, int z, ArrayList<PlaceableBlock> blocks) {
        WorldLocation key = getKey(world, x, y, z);
        MineData data = map.get(key);
        data.getMine().complete(world, x, y, z, blocks);
        HFTrackers.markDirty();
    }

    public void destroyLevel(World world, int x, int y, int z) {        
        WorldLocation key = getKey(world, x, y, z);
        MineData data = map.get(key);
        if (data == null || data.getLevel() == null) {
            return;
        } else {
            data.getLevel().destroy();
            HFTrackers.markDirty();
        }
    }

    @Override
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

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
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
    }
}
