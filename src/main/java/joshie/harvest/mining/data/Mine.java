package joshie.harvest.mining.data;

import joshie.harvest.buildings.placeable.blocks.PlaceableBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Mine {
    private List<MineLevel> levels = new ArrayList<MineLevel>();
    private String name;

    public String getName() {
        return name;
    }

    public Mine() {
    }

    public Mine(String name) {
        this.name = name;
    }

    public void newDay() {
        for (MineLevel level : levels) {
            level.newDay();
        }
    }

    public MineLevel getLevel(int level) {
        return levels.get(level);
    }

    public int getLevels() {
        return levels.size();
    }

    public int addLevel() {
        //If there are any caved in levels return them
        for (MineLevel level : levels) {
            if (level.isCavedIn()) {
                return level.get();
            }
        }

        levels.add(new MineLevel(levels.size()));
        return levels.size() - 1;
    }

    public void complete(World world, BlockPos pos, ArrayList<PlaceableBlock> blocks) {
        levels.get(levels.size() - 1).complete(world, pos, this, blocks);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        name = nbt.getString("MineName");
        NBTTagList level = nbt.getTagList("Levels", 10);
        for (int i = 0; i < level.tagCount(); i++) {
            NBTTagCompound tag = level.getCompoundTagAt(i);
            MineLevel l = new MineLevel();
            l.readFromNBT(tag);
            levels.add(l);
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("MineName", name);
        NBTTagList level = new NBTTagList();
        for (MineLevel l : levels) {
            NBTTagCompound tag = new NBTTagCompound();
            l.writeToNBT(tag);
            level.appendTag(tag);
        }

        nbt.setTag("Levels", level);
    }
}