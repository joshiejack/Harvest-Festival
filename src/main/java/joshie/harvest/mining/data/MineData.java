package joshie.harvest.mining.data;

import joshie.harvest.core.util.IData;
import joshie.harvest.mining.MineTrackerServer;
import net.minecraft.nbt.NBTTagCompound;

public class MineData implements IData {
    private Mine mine;
    private MineLevel level;
    
    public MineData() {}
    public MineData(String name) {
        mine = new Mine(name);
        MineTrackerServer.mines.add(mine);
        level = null;
    }

    public MineData(Mine mine, MineLevel level) {
        this.mine = mine;
        this.level = level;
    }

    public Mine getMine() {
        return mine;
    }

    public MineLevel getLevel() {
        return level;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        String name = nbt.getString("MineName");
        int level = nbt.getInteger("Level");
        for (Mine mine : MineTrackerServer.mines) {
            if (mine.getName().equals(name)) {
                this.mine = mine;
                if (nbt.hasKey("Level")) {
                    this.level = this.mine.getLevel(level);
                }
                
                break;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setString("MineName", mine.getName());
        if (level != null) {
            nbt.setInteger("Level", level.get());
        }
    }
}
