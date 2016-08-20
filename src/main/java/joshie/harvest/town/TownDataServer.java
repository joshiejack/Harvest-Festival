package joshie.harvest.town;

import joshie.harvest.gathering.GatheringData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TownDataServer extends TownData {
    public GatheringData gathering = new GatheringData();

    public TownDataServer() {}
    public TownDataServer(BlockPos pos) {
        townCentre = pos;
        uuid = UUID.randomUUID();
    }

    @Override
    public void newDay(World world) {
        gathering.newDay(world, buildings.values());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        gathering.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        gathering.writeToNBT(nbt);
    }
}
